package me.rexsystems.rexKills.death;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LastDeaths {

    public record Snapshot(String cause, String killer, String weapon) {
    }

    private final ConcurrentHashMap<UUID, Snapshot> deaths = new ConcurrentHashMap<>();

    public void record(UUID playerId, Snapshot snapshot) {
        if (playerId == null || snapshot == null) {
            return;
        }
        deaths.put(playerId, snapshot);
    }

    public Snapshot get(UUID playerId) {
        if (playerId == null) {
            return null;
        }
        return deaths.get(playerId);
    }
}
