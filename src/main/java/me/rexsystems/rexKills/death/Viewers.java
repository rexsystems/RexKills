package me.rexsystems.rexKills.death;

import java.util.Locale;

public enum Viewers {
    ALL,
    WORLD,
    RADIUS,
    INVOLVED,
    VICTIM,
    KILLER;

    public static Viewers parse(String raw, Viewers fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Viewers.valueOf(raw.trim().toUpperCase(Locale.ROOT).replace('-', '_'));
        } catch (IllegalArgumentException ignored) {
            return fallback;
        }
    }
}
