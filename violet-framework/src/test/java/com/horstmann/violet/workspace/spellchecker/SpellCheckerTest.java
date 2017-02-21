package com.horstmann.violet.workspace.spellchecker;

import org.junit.Assert;
import org.junit.Test;


public class SpellCheckerTest {


    @Test
    public void shouldReturnFalseWhenWordIsInCorrect() throws Exception {

        final String spellMistake = "Somethinf";
        final boolean result = SpellChecker.isCorrectWord(spellMistake);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenWordIsCorrect() throws Exception {

        final String correctString = "Something";
        final boolean result = SpellChecker.isCorrectWord(correctString);
        Assert.assertTrue(result);
    }

}