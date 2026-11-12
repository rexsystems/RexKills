package me.rexsystems.rexKills.command;

import me.rexsystems.rexKills.RexKills;
import me.rexsystems.rexKills.death.DeathFormatter;
import me.rexsystems.rexKills.util.MessageService;
import me.rexsystems.rexKills.util.Names;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class KillsCommand implements CommandExecutor, TabCompleter {

    private final RexKills plugin;

    public KillsCommand(RexKills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            return help(sender);
        }
        return switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> reload(sender);
            case "preview" -> preview(sender, args);
            default -> help(sender);
        };
    }

    private boolean help(CommandSender sender) {
        MessageService.send(sender, plugin.getConfigManager().prefixed("help-header"));
        for (String line : plugin.getConfigManager().helpLines()) {
            MessageService.send(sender, line);
        }
        return true;
    }

    private boolean reload(CommandSender sender) {
        if (!sender.hasPermission("rexkills.reload")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        plugin.reloadPlugin();
        MessageService.send(sender, plugin.getConfigManager().prefixed("reload"));
        return true;
    }

    private boolean preview(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rexkills.preview")) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("no-permission"));
            return true;
        }
        if (!(sender instanceof Player player)) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("players-only"));
            return true;
        }
        if (args.length < 2) {
            MessageService.send(sender, plugin.getConfigManager().prefixed("preview-usage"));
            return true;
        }
        String cause = args[1].toUpperCase(Locale.ROOT);
        String killer = args.length >= 3 ? args[2] : "";
        String weapon = "";
        if (!player.getInventory().getItemInMainHand().getType().isAir()) {
            weapon = Names.enumName(player.getInventory().getItemInMainHand().getType().name());
        }
        player.sendMessage(new DeathFormatter(plugin).chat(player, cause, killer, weapon));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return filter(List.of("help", "reload", "preview"), args[0]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("preview")) {
            List<String> causes = new ArrayList<>();
            for (EntityDamageEvent.DamageCause cause : EntityDamageEvent.DamageCause.values()) {
                causes.add(cause.name().toLowerCase(Locale.ROOT));
            }
            return filter(causes, args[1]);
        }
        return List.of();
    }

    private static List<String> filter(List<String> options, String prefix) {
        String needle = prefix == null ? "" : prefix.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(needle)) {
                matches.add(option);
            }
        }
        return matches;
    }
}
