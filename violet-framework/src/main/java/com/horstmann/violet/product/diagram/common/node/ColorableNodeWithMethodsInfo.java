package com.horstmann.violet.product.diagram.common.node;

import com.horstmann.violet.product.diagram.abstracts.node.IRenameableNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.RemoveSentenceDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.ReplaceSentenceDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.UnderlineDecorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorableNodeWithMethodsInfo extends ColorableNode implements IRenameableNode {

    public ColorableNodeWithMethodsInfo()
    {
        super();
        methods = new MultiLineText(SIGNATURE_CONVERTER);
    }

    protected ColorableNodeWithMethodsInfo(ColorableNode node) throws CloneNotSupportedException
    {
        super(node);
        methods = new MultiLineText(SIGNATURE_CONVERTER);
    }

    @Override
    public void replaceNodeOccurrences(String oldValue, String newValue) {
        if (!getMethods().toString().isEmpty()) {
            MultiLineText renamedMethods = new MultiLineText();
            renamedMethods.setText(renameMethods(oldValue, newValue));
            setMethods(renamedMethods);
        }
    }

    /**
     *  Finds all of oldValue class occurrences in methods and replaces it with newValue
     * @param oldValue old class name
     * @param newValue new class name
     * @return renamed methods
     */
    private String renameMethods(String oldValue, String newValue) {
        ArrayList<String> methods = new ArrayList<String>(Arrays.asList(getMethods().toEdit().split("\n")));
        StringBuilder renamedMethods = new StringBuilder();
        Pattern pattern = Pattern.compile(":\\s*(" + oldValue + ")[,)\\s]*");

        String separator = "";
        for(String method : methods) {
            renamedMethods.append(separator + renameOccurrencesInMethod(method, newValue, pattern));
            separator = "\n";
        }

        return renamedMethods.toString();
    }

    @Override
    public LineText getAttributes()
    {
        return null;
    }

    /**
     * Renames class occurrences in a single method with newValue
     * @param method method to rename
     * @param newValue new name of class
     * @param pattern regex pattern
     * @return renamed method
     */
    private StringBuffer renameOccurrencesInMethod(String method, String newValue, Pattern pattern)
    {
        StringBuffer methodToRename = new StringBuffer(method);
        Matcher matcher = pattern.matcher(methodToRename);
        while (matcher.find()) {
            methodToRename.replace(matcher.start(1), matcher.end(1), newValue);
            matcher = pattern.matcher(methodToRename);
        }
        return methodToRename;
    }


    /**
     * Sets the methods property value.
     *
     * @param newValue the methods of this interface
     */
    public void setMethods(LineText newValue)
    {
        methods.setText(newValue);
    }

    /**
     * Gets the methods property value.
     *
     * @return the methods of this interface
     */
    public LineText getMethods()
    {
        return methods;
    }

    /**
     * Converts text to displayed format
     */
    protected static final LineText.Converter SIGNATURE_CONVERTER = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText lineString = new OneLineText(text);

            if(lineString.contains(STATIC))
            {
                lineString = new UnderlineDecorator(new RemoveSentenceDecorator(lineString, STATIC));
            }
            for(String[] signature : SIGNATURE_REPLACE_KEYS)
            {
                lineString = new ReplaceSentenceDecorator(lineString, signature[0], signature[1]);
            }

            return lineString;
        }
    };

    protected static final String STATIC = "\u00ABstatic\u00BB";
    protected static final String[][] SIGNATURE_REPLACE_KEYS = {
            { "public ", "+ " },
            { "package ", "~ " },
            { "protected ", "# " },
            { "private ", "- " },
            { "property ", "/ " }
    };
    protected MultiLineText methods;
}