package me.rexsystems.rexKills.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.rexsystems.rexKills.RexKills;
import me.rexsystems.rexKills.death.LastDeaths;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class RexKillsPlaceholders extends PlaceholderExpansion {

    private final RexKills plugin;

    public RexKillsPlaceholders(RexKills plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "rexkills";
    }

    @Override
    public @NotNull String getAuthor() {
        return "RexSystems";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        LastDeaths.Snapshot snapshot = plugin.getLastDeaths().get(player.getUniqueId());
        String key = params.toLowerCase(Locale.ROOT);
        return switch (key) {
            case "last_cause" -> snapshot == null ? "" : snapshot.cause();
            case "last_killer" -> snapshot == null || snapshot.killer() == null ? "" : snapshot.killer();
            case "last_weapon" -> snapshot == null || snapshot.weapon() == null ? "" : snapshot.weapon();
            default -> null;
        };
    }
}
