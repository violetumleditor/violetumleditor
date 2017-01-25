package com.horstmann.violet.product.diagram.property.text;

import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineTextDecorator;

/**
 * This class adds a prefix and suffix.
 *
 */
public class PrefixAndSuffixDecorator extends OneLineTextDecorator
{
    public PrefixAndSuffixDecorator(OneLineText decoratedOneLineString, String prefix, String suffix)
    {
        super(decoratedOneLineString);
        this.setPrefixAndSuffix(prefix, suffix);
    }

    /**
     * Sets prefix and suffix.
     * @param prefix
     * @param suffix
     */
    public final void setPrefixAndSuffix(String prefix, String suffix)
    {
        if(null == prefix)
        {
            prefix = "";
        }
        else if(null == suffix){
            suffix = "";
        }
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return prefix + " " + decoratedOneLineString.toDisplay() + " " + suffix;
    }

    private String prefix = "";
    private String suffix = "";
}