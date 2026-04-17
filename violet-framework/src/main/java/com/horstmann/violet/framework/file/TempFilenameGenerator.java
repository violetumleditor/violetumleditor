package com.horstmann.violet.framework.file;

import java.util.Random;

/**
 * Generates random funny temporary filenames such as "funky-heron-326" or "stormy-fish-426".
 * 32 adjectives × 32 animals = 1024 combinations, each suffixed with a 3-digit random number.
 */
public final class TempFilenameGenerator
{
    private static final String[] ADJECTIVES = {
        "funky",  "stormy", "fuzzy",  "breezy",
        "grumpy", "jumpy",  "spunky", "bouncy",
        "sneaky", "zippy",  "fluffy", "goofy",
        "peppy",  "wacky",  "quirky", "cheeky",
        "sassy",  "dizzy",  "spiffy", "nifty",
        "flashy", "punchy", "zesty",  "frisky",
        "groovy", "jazzy",  "perky",  "snappy",
        "lanky",  "plucky", "slinky", "dandy"
    };

    private static final String[] ANIMALS = {
        "heron",  "fish",   "otter",  "badger",
        "crane",  "lemur",  "gecko",  "panda",
        "koala",  "fox",    "moose",  "bison",
        "eagle",  "sloth",  "coyote", "llama",
        "quail",  "ferret", "wombat", "ibis",
        "dingo",  "tapir",  "kiwi",   "finch",
        "marmot", "frog",   "lynx",   "raven",
        "robin",  "vole",   "stoat",  "wren"
    };

    private static final Random RANDOM = new Random();

    /**
     * Generates a random name such as "funky-heron-326".
     *
     * @return a temporary base filename (no extension)
     */
    public static String generate()
    {
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
        String animal    = ANIMALS[RANDOM.nextInt(ANIMALS.length)];
        int    number    = 100 + RANDOM.nextInt(900);
        return adjective + "-" + animal + "-" + number;
    }

    private TempFilenameGenerator() {}
}
