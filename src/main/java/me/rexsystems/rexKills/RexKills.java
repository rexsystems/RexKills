package me.rexsystems.rexKills;

import me.rexsystems.rexKills.command.KillsCommand;
import me.rexsystems.rexKills.config.ConfigManager;
import me.rexsystems.rexKills.death.DeathListener;
import me.rexsystems.rexKills.death.LastDeaths;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class RexKills extends JavaPlugin {

    private ConfigManager configManager;
    private LastDeaths lastDeaths;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.configManager.load();
        this.lastDeaths = new LastDeaths();

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        KillsCommand command = new KillsCommand(this);
        PluginCommand pluginCommand = getCommand("rexkills");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new me.rexsystems.rexKills.papi.RexKillsPlaceholders(this).register();
            getLogger().info("PlaceholderAPI expansion registered.");
        }

        getLogger().info("RexKills enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("RexKills disabled.");
    }

    public void reloadPlugin() {
        configManager.reload();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LastDeaths getLastDeaths() {
        return lastDeaths;
    }
}
