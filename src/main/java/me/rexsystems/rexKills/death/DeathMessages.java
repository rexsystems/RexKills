package me.rexsystems.rexKills.death;

import java.util.Locale;
import java.util.Map;

public final class DeathMessages {

    private DeathMessages() {
    }

    public static String select(String cause, Map<String, String> causes, String fallback) {
        String safeFallback = fallback == null ? "" : fallback;
        if (cause == null || cause.isBlank() || causes == null || causes.isEmpty()) {
            return safeFallback;
        }
        String specific = causes.get(cause.toUpperCase(Locale.ROOT));
        if (specific == null || specific.isBlank()) {
            return safeFallback;
        }
        return specific;
    }
}
