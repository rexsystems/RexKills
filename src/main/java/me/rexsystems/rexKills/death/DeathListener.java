package me.rexsystems.rexKills.death;

import me.rexsystems.rexKills.RexKills;
import me.rexsystems.rexKills.config.ConfigManager;
import me.rexsystems.rexKills.util.Names;
import me.rexsystems.rexKills.util.SchedulerUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DeathListener implements Listener {

    private static final Method DEATH_SCREEN = findDeathScreen();
    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    private final RexKills plugin;
    private final DeathFormatter formatter;
    private final Set<String> unknownSounds = ConcurrentHashMap.newKeySet();

    public DeathListener(RexKills plugin) {
        this.plugin = plugin;
        this.formatter = new DeathFormatter(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        ConfigManager config = plugin.getConfigManager();
        if (!config.enabled()) {
            return;
        }

        Player victim = event.getPlayer();
        if (victim.hasPermission("rexkills.bypass")) {
            return;
        }
        if (!config.worldAllowed(victim.getWorld().getName())) {
            return;
        }
        if (config.requirePermission() && !victim.hasPermission("rexkills.custom")) {
            return;
        }

        String cause = causeOf(victim);
        Player killer = victim.getKiller();
        String killerName = killerName(victim, killer);
        String weaponName = weaponName(victim, killer);
        Component chat = formatter.chat(victim, cause, killerName, weaponName);
        if (Component.empty().equals(chat)) {
            return;
        }

        plugin.getLastDeaths().record(victim.getUniqueId(), new LastDeaths.Snapshot(cause, killerName, weaponName));

        if (!config.vanillaDeathSound()) {
            event.setShouldPlayDeathSound(false);
        }

        boolean suppressVanilla = config.cancelVanilla() || config.audience() != Viewers.ALL;
        if (suppressVanilla) {
            event.deathMessage(null);
            deliver(viewers(config.audience(), victim, killer), viewer -> viewer.sendMessage(chat));
        } else {
            event.deathMessage(chat);
        }

        applyDeathScreen(event, formatter.render(victim, config.deathScreenMessage(), cause, killerName, weaponName));
        playSound(config, victim, killer);
        showTitle(config, victim, killer, cause, killerName, weaponName);
        showActionbar(config, victim, killer, cause, killerName, weaponName);
        showBossbar(config, victim, killer, cause, killerName, weaponName);
    }

    private void applyDeathScreen(PlayerDeathEvent event, Component screen) {
        if (DEATH_SCREEN == null) {
            return;
        }
        String mode = plugin.getConfigManager().deathScreen();
        try {
            if ("hide".equals(mode)) {
                DEATH_SCREEN.invoke(event, Component.empty());
            } else if ("custom".equals(mode)) {
                DEATH_SCREEN.invoke(event, screen);
            }
        } catch (ReflectiveOperationException failure) {
            plugin.getLogger().warning("Could not set the death screen: " + failure.getMessage());
        }
    }

    private void playSound(ConfigManager config, Player victim, Player killer) {
        if (!config.soundEnabled()) {
            return;
        }
        String name = config.soundName();
        if (name == null || name.isBlank()) {
            return;
        }
        Sound sound;
        try {
            sound = Sound.valueOf(name.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            if (unknownSounds.add(name)) {
                plugin.getLogger().warning("Unknown sound '" + name + "'. Check sound.sound in config.yml.");
            }
            return;
        }
        Location at = victim.getLocation();
        float volume = config.soundVolume();
        float pitch = config.soundPitch();
        deliver(viewers(config.soundViewers(), victim, killer), viewer -> viewer.playSound(at, sound, volume, pitch));
    }

    private void showTitle(ConfigManager config, Player victim, Player killer, String cause, String killerName, String weaponName) {
        if (!config.titleEnabled()) {
            return;
        }
        Component title = formatter.render(victim, config.titleText(), cause, killerName, weaponName);
        Component subtitle = formatter.render(victim, config.subtitleText(), cause, killerName, weaponName);
        if (Component.empty().equals(title) && Component.empty().equals(subtitle)) {
            return;
        }
        Title.Times times = Title.Times.times(
                Duration.ofMillis(config.titleFadeIn() * 50L),
                Duration.ofMillis(config.titleStay() * 50L),
                Duration.ofMillis(config.titleFadeOut() * 50L));
        Title shown = Title.title(title, subtitle, times);
        deliver(viewers(config.titleViewers(), victim, killer), viewer -> viewer.showTitle(shown));
    }

    private void showActionbar(ConfigManager config, Player victim, Player killer, String cause, String killerName, String weaponName) {
        if (!config.actionbarEnabled()) {
            return;
        }
        Component line = formatter.render(victim, config.actionbarText(), cause, killerName, weaponName);
        if (Component.empty().equals(line)) {
            return;
        }
        deliver(viewers(config.actionbarViewers(), victim, killer), viewer -> viewer.sendActionBar(line));
    }

    private void showBossbar(ConfigManager config, Player victim, Player killer, String cause, String killerName, String weaponName) {
        if (!config.bossbarEnabled()) {
            return;
        }
        Component line = formatter.render(victim, config.bossbarText(), cause, killerName, weaponName);
        if (Component.empty().equals(line)) {
            return;
        }
        BossBar bar = BossBar.bossBar(line, 1.0f, color(config.bossbarColor()), overlay(config.bossbarStyle()));
        long ticks = config.bossbarSeconds() * 20L;
        deliver(viewers(config.bossbarViewers(), victim, killer), viewer -> {
            viewer.showBossBar(bar);
            SchedulerUtils.runForPlayerLater(plugin, viewer, () -> viewer.hideBossBar(bar), ticks);
        });
    }

    private void deliver(List<Player> players, java.util.function.Consumer<Player> action) {
        for (Player player : players) {
            SchedulerUtils.runForPlayer(plugin, player, () -> {
                if (player.isOnline()) {
                    action.accept(player);
                }
            });
        }
    }

    private List<Player> viewers(Viewers who, Player victim, Player killer) {
        return switch (who) {
            case VICTIM -> List.of(victim);
            case KILLER -> killer == null ? List.of() : List.of(killer);
            case INVOLVED -> involved(victim, killer);
            case WORLD -> List.copyOf(victim.getWorld().getPlayers());
            case RADIUS -> nearby(victim);
            case ALL -> List.copyOf(Bukkit.getOnlinePlayers());
        };
    }

    private List<Player> involved(Player victim, Player killer) {
        if (killer == null || killer.equals(victim)) {
            return List.of(victim);
        }
        return List.of(victim, killer);
    }

    private List<Player> nearby(Player victim) {
        double radius = plugin.getConfigManager().radius();
        if (radius <= 0) {
            return List.copyOf(victim.getWorld().getPlayers());
        }
        Collection<Player> found = victim.getWorld().getNearbyPlayers(victim.getLocation(), radius, radius, radius);
        return new ArrayList<>(found);
    }

    private static String causeOf(Player victim) {
        EntityDamageEvent last = victim.getLastDamageCause();
        if (last == null || last.getCause() == null) {
            return "CUSTOM";
        }
        return last.getCause().name();
    }

    private String killerName(Player victim, Player killer) {
        if (killer != null) {
            if (plugin.getConfigManager().useDisplayName()) {
                return PLAIN.serialize(killer.displayName());
            }
            return killer.getName();
        }
        Entity source = damageSource(victim);
        if (source == null || source.equals(victim)) {
            return "";
        }
        Component custom = source.customName();
        if (custom != null) {
            return PLAIN.serialize(custom);
        }
        return Names.enumName(source.getType().name());
    }

    private String weaponName(Player victim, Player killer) {
        EntityDamageEvent last = victim.getLastDamageCause();
        if (last instanceof EntityDamageByEntityEvent byEntity && byEntity.getDamager() instanceof Projectile projectile) {
            Component custom = projectile.customName();
            if (custom != null) {
                return PLAIN.serialize(custom);
            }
            return Names.enumName(projectile.getType().name());
        }
        LivingEntity holder = killer != null ? killer : livingSource(victim);
        if (holder == null || holder.getEquipment() == null) {
            return "";
        }
        ItemStack hand = holder.getEquipment().getItemInMainHand();
        if (hand == null || hand.getType().isAir()) {
            return "";
        }
        if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
            return PLAIN.serialize(hand.displayName());
        }
        return Names.enumName(hand.getType().name());
    }

    private static Entity damageSource(Player victim) {
        EntityDamageEvent last = victim.getLastDamageCause();
        if (!(last instanceof EntityDamageByEntityEvent byEntity)) {
            return null;
        }
        Entity damager = byEntity.getDamager();
        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Entity entity) {
                return entity;
            }
        }
        return damager;
    }

    private static LivingEntity livingSource(Player victim) {
        Entity source = damageSource(victim);
        if (source instanceof LivingEntity living) {
            return living;
        }
        return null;
    }

    private static BossBar.Color color(String raw) {
        if (raw == null || raw.isBlank()) {
            return BossBar.Color.RED;
        }
        try {
            return BossBar.Color.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return BossBar.Color.RED;
        }
    }

    private static BossBar.Overlay overlay(String raw) {
        String name = raw == null ? "" : raw.trim().toUpperCase(Locale.ROOT);
        return switch (name) {
            case "SEGMENTED_6", "NOTCHED_6" -> BossBar.Overlay.NOTCHED_6;
            case "SEGMENTED_10", "NOTCHED_10" -> BossBar.Overlay.NOTCHED_10;
            case "SEGMENTED_12", "NOTCHED_12" -> BossBar.Overlay.NOTCHED_12;
            case "SEGMENTED_20", "NOTCHED_20" -> BossBar.Overlay.NOTCHED_20;
            default -> BossBar.Overlay.PROGRESS;
        };
    }

    private static Method findDeathScreen() {
        try {
            return PlayerDeathEvent.class.getMethod("deathScreenMessage", Component.class);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }
}
