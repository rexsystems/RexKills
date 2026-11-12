package me.rexsystems.rexKills.death;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeathMessagesTest {

    @Test
    void specificCauseWins() {
        Map<String, String> causes = Map.of("FALL", "fell");
        assertEquals("fell", DeathMessages.select("fall", causes, "died"));
    }

    @Test
    void blankOrUnknownCauseUsesDefault() {
        Map<String, String> causes = Map.of("FALL", "   ");
        assertEquals("died", DeathMessages.select("FALL", causes, "died"));
        assertEquals("died", DeathMessages.select("MAGIC", causes, "died"));
        assertEquals("died", DeathMessages.select(null, causes, "died"));
    }
}
