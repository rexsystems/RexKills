package me.rexsystems.rexKills.util;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextParserTest {

    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    @Test
    void legacyHexAndAmpersandBecomeMiniMessage() {
        String plain = PLAIN.serialize(TextParser.parse("&#ff0000Hi &lthere"));
        assertEquals("Hi there", plain);
    }

    @Test
    void hashHexDoesNotEatMiniMessageColors() {
        String normalized = TextParser.normalize("<gradient:#ff0000:#00ff00>Hi</gradient>");
        assertTrue(normalized.contains("<gradient:#ff0000:#00ff00>"));
        assertFalse(normalized.contains("<gradient:<#"));
    }

    @Test
    void brokenMiniMessageFallsBackToLegacy() {
        String plain = PLAIN.serialize(TextParser.parse("&aOkay <red"));
        assertTrue(plain.contains("Okay"));
    }

    @Test
    void insertedNamesStayLiteral() {
        String plain = PLAIN.serialize(TextParser.parse(TextParser.apply(
                "<gray>{player} died",
                Map.of("player", "<red>hack&c"))));
        assertEquals("<red>hack＆c died", plain);
    }

    @Test
    void percentTokensDoNotEatPlaceholderApiKeys() {
        String applied = TextParser.apply("Hello %player% and %player_health%", Map.of("player", "Steve"));
        assertEquals("Hello Steve and %player_health%", applied);
    }
}
