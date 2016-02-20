package com.horstmann.violet.framework.property.string.decorator;

/**
 * This class replace a sentence in the text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 17.12.2015
 */
public class ReplaceSentenceDecorator extends OneLineStringDecorator
{
    public ReplaceSentenceDecorator(OneLineString decoratedOneLineString, String oldSentence, String newSentence)
    {
        super(decoratedOneLineString);
        this.oldSentence = replaceForUnification(oldSentence).toLowerCase();
        this.newSentence = replaceForUnification(newSentence);
    }

    /**
     * @see OneLineString#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return replaceSentence(decoratedOneLineString.toDisplay());
    }

    /**
     * replace sentence in text
     * @param text
     * @return text with replace sentence
     */
    private String replaceSentence(String text)
    {
        String formattedText = replaceForUnification(text).toLowerCase();

        int index = formattedText.indexOf(oldSentence);
        if(-1 == index)
        {
            return text;
        }

        return text.substring(0, index) + newSentence + text.substring(index + oldSentence.length());
    }

    private String oldSentence;
    private String newSentence;
}