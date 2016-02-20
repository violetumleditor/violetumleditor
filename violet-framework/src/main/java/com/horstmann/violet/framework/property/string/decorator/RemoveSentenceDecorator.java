package com.horstmann.violet.framework.property.string.decorator;

/**
 * This class remove a sentence from text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 17.12.2015
 */
public class RemoveSentenceDecorator extends OneLineStringDecorator
{
    public RemoveSentenceDecorator(OneLineString decoratedOneLineString, String sentence)
    {
        super(decoratedOneLineString);
        this.sentence = replaceForUnification(sentence).toLowerCase();
    }

    /**
     * @see OneLineString#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return removeSentence(decoratedOneLineString.toDisplay());
    }

    /**
     * remove sentence from text
     * @param text
     * @return text without removed sentence
     */
    private String removeSentence(String text)
    {
        String formattedText = replaceForUnification(text).toLowerCase();

        int index = formattedText.indexOf(sentence);
        if(-1 == index)
        {
            return text;
        }

        return text.substring(0, index) + text.substring(index + sentence.length());
    }

    private String sentence;
}