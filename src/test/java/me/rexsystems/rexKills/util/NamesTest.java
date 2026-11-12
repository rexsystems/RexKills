package me.rexsystems.rexKills.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamesTest {

    @Test
    void enumNamesBecomeWords() {
        assertEquals("Diamond Sword", Names.enumName("DIAMOND_SWORD"));
        assertEquals("", Names.enumName("  "));
    }
}
