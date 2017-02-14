package com.horstmann.violet.product.diagram.classes.edge;

/**
 * Created by Narin on 04.11.2016.
 */
public class CommonRelationEdgeBeanInfo extends ClassRelationshipEdgeBeanInfo {

    protected CommonRelationEdgeBeanInfo(final Class<?> beanClass) {
        super(beanClass);
    }

    public CommonRelationEdgeBeanInfo() {
        this(AssociationEdge.class);
    }
}
