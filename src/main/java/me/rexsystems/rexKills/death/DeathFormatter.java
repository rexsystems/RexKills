package me.rexsystems.rexKills.death;

import me.rexsystems.rexKills.RexKills;
import me.rexsystems.rexKills.config.ConfigManager;
import me.rexsystems.rexKills.util.Placeholders;
import me.rexsystems.rexKills.util.TextParser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class DeathFormatter {

    private final RexKills plugin;

    public DeathFormatter(RexKills plugin) {
        this.plugin = plugin;
    }

    public Map<String, String> tokens(Player victim, String cause, String killer, String weapon) {
        ConfigManager config = plugin.getConfigManager();
        String killerText = killer == null || killer.isBlank() ? config.unknownKiller() : killer;
        String weaponText = weapon == null || weapon.isBlank() ? config.unknownWeapon() : weapon;
        Map<String, String> tokens = new HashMap<>();
        tokens.put("player", playerName(victim));
        tokens.put("killer", killerText);
        tokens.put("weapon", weaponText);
        tokens.put("cause", cause == null ? "" : cause);
        tokens.put("world", victim.getWorld().getName());
        tokens.put("x", String.valueOf(victim.getLocation().getBlockX()));
        tokens.put("y", String.valueOf(victim.getLocation().getBlockY()));
        tokens.put("z", String.valueOf(victim.getLocation().getBlockZ()));
        tokens.put("prefix", config.prefix());
        return tokens;
    }

    public Component chat(Player victim, String cause, String killer, String weapon) {
        return render(victim, plugin.getConfigManager().causeMessage(cause), cause, killer, weapon);
    }

    public Component render(Player victim, String template, String cause, String killer, String weapon) {
        if (template == null || template.isBlank()) {
            return Component.empty();
        }
        String withTokens = TextParser.apply(template, tokens(victim, cause, killer, weapon));
        return TextParser.parse(Placeholders.apply(victim, withTokens));
    }

    private String playerName(Player victim) {
        if (plugin.getConfigManager().useDisplayName()) {
            return net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                    .serialize(victim.displayName());
        }
        return victim.getName();
    }
}
