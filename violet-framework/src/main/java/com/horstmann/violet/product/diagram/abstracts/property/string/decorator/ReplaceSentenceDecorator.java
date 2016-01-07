package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 17.12.2015.
 */
public class ReplaceSentenceDecorator extends OneLineStringDecorator
{
    public ReplaceSentenceDecorator(OneLineString decoratedOneLineString, String oldSentence, String newSentence)
    {
        super(decoratedOneLineString);
        this.oldSentence = replaceForUnification(oldSentence).toLowerCase();
        this.newSentence = replaceForUnification(newSentence);
    }

    @Override
    public String toDisplay()
    {
        return replaceSentence(decoratedOneLineString.toDisplay());
    }

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