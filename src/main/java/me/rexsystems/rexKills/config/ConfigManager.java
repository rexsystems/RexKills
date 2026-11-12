package me.rexsystems.rexKills.config;

import me.rexsystems.rexKills.RexKills;
import me.rexsystems.rexKills.death.DeathMessages;
import me.rexsystems.rexKills.death.Viewers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfigManager {

    private final RexKills plugin;
    private FileConfiguration config;
    private Map<String, String> causes = Map.of();

    public ConfigManager(RexKills plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        new ConfigAutoUpdater(plugin).update();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        this.causes = readCauses();
    }

    public void reload() {
        load();
    }

    public boolean enabled() {
        return config.getBoolean("settings.enabled", true);
    }

    public boolean requirePermission() {
        return config.getBoolean("settings.require-permission", false);
    }

    public boolean useDisplayName() {
        return config.getBoolean("settings.use-display-name", false);
    }

    public boolean cancelVanilla() {
        return config.getBoolean("settings.cancel-vanilla", true);
    }

    public boolean vanillaDeathSound() {
        return config.getBoolean("settings.vanilla-death-sound", true);
    }

    public Viewers audience() {
        return Viewers.parse(config.getString("settings.audience"), Viewers.ALL);
    }

    public double radius() {
        double value = config.getDouble("settings.radius", 64);
        if (value <= 0) {
            return 0;
        }
        return Math.min(value, 256);
    }

    public String deathScreen() {
        String mode = config.getString("settings.death-screen", "keep");
        if (mode == null) {
            return "keep";
        }
        return mode.toLowerCase(Locale.ROOT);
    }

    public boolean worldAllowed(String worldName) {
        if (worldName == null) {
            return false;
        }
        List<String> enabled = config.getStringList("worlds.enabled");
        if (enabled != null && !enabled.isEmpty()) {
            return enabled.stream().anyMatch(world -> world.equalsIgnoreCase(worldName));
        }
        List<String> disabled = config.getStringList("worlds.disabled");
        if (disabled == null || disabled.isEmpty()) {
            return true;
        }
        return disabled.stream().noneMatch(world -> world.equalsIgnoreCase(worldName));
    }

    public String message(String path) {
        return config.getString("messages." + path, "");
    }

    public String prefix() {
        String prefix = message("prefix");
        return prefix == null ? "" : prefix;
    }

    public String prefixed(String path) {
        String raw = message(path);
        if (raw == null) {
            return "";
        }
        return raw.replace("<prefix>", prefix()).replace("{prefix}", prefix());
    }

    public List<String> helpLines() {
        List<String> lines = config.getStringList("messages.help");
        return lines == null ? List.of() : lines;
    }

    public String causeMessage(String cause) {
        return DeathMessages.select(cause, causes, message("default"));
    }

    public String unknownKiller() {
        String value = message("unknown-killer");
        return value == null || value.isBlank() ? "something" : value;
    }

    public String unknownWeapon() {
        String value = message("unknown-weapon");
        return value == null ? "" : value;
    }

    public boolean soundEnabled() {
        return config.getBoolean("sound.enabled", false);
    }

    public String soundName() {
        return config.getString("sound.sound", "");
    }

    public float soundVolume() {
        return (float) config.getDouble("sound.volume", 1.0);
    }

    public float soundPitch() {
        return (float) config.getDouble("sound.pitch", 1.0);
    }

    public Viewers soundViewers() {
        return Viewers.parse(config.getString("sound.to"), Viewers.VICTIM);
    }

    public boolean titleEnabled() {
        return config.getBoolean("title.enabled", false);
    }

    public String titleText() {
        return config.getString("title.title", "");
    }

    public String subtitleText() {
        return config.getString("title.subtitle", "");
    }

    public int titleFadeIn() {
        return Math.max(0, config.getInt("title.fade-in", 10));
    }

    public int titleStay() {
        return Math.max(0, config.getInt("title.stay", 40));
    }

    public int titleFadeOut() {
        return Math.max(0, config.getInt("title.fade-out", 10));
    }

    public Viewers titleViewers() {
        return Viewers.parse(config.getString("title.to"), Viewers.VICTIM);
    }

    public boolean actionbarEnabled() {
        return config.getBoolean("actionbar.enabled", false);
    }

    public String actionbarText() {
        return config.getString("actionbar.text", "");
    }

    public Viewers actionbarViewers() {
        return Viewers.parse(config.getString("actionbar.to"), Viewers.VICTIM);
    }

    public boolean bossbarEnabled() {
        return config.getBoolean("bossbar.enabled", false);
    }

    public String bossbarText() {
        return config.getString("bossbar.text", "");
    }

    public String bossbarColor() {
        return config.getString("bossbar.color", "RED");
    }

    public String bossbarStyle() {
        return config.getString("bossbar.style", "SOLID");
    }

    public int bossbarSeconds() {
        return Math.max(1, Math.min(60, config.getInt("bossbar.seconds", 3)));
    }

    public Viewers bossbarViewers() {
        return Viewers.parse(config.getString("bossbar.to"), Viewers.VICTIM);
    }

    public String deathScreenMessage() {
        return config.getString("death-screen.message", "");
    }

    private Map<String, String> readCauses() {
        ConfigurationSection section = config.getConfigurationSection("messages.causes");
        if (section == null) {
            return Map.of();
        }
        Map<String, String> parsed = new HashMap<>();
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) {
                continue;
            }
            String value = section.getString(key, "");
            if (key != null && value != null) {
                parsed.put(key.toUpperCase(Locale.ROOT), value);
            }
        }
        return Collections.unmodifiableMap(parsed);
    }
}
