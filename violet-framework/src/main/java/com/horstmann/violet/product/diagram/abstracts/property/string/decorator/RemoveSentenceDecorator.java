package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 17.12.2015.
 */
public class RemoveSentenceDecorator extends OneLineStringDecorator {

    public RemoveSentenceDecorator(OneLineString decoratedOneLineString, String sentence)
    {
        super(decoratedOneLineString);
        this.sentence = replaceForUnification(sentence).toLowerCase();
    }

    @Override
    public String getHTML()
    {
        return removeSentence(decoratedOneLineString.getHTML());
    }

    protected String removeSentence(String text)
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