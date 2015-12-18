package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class OneLineStringDecorator extends OneLineString {
    public OneLineStringDecorator(OneLineString decoratedOneLineString)
    {
        this.decoratedOneLineString = decoratedOneLineString;
    }

    public String getHTML()
    {
        return decoratedOneLineString.getHTML();
    }
    public String getText()
    {
        return decoratedOneLineString.getText();
    }

    public OneLineStringDecorator clone() {
        return new OneLineStringDecorator(this.decoratedOneLineString.clone());
    }

    protected OneLineString decoratedOneLineString;
}