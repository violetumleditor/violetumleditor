/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.common.node;

import com.horstmann.violet.product.diagram.abstracts.node.AbstractNodeBeanInfo;
import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * The bean info for the ColorableNode type. This hides the bounds property.
 */
public class ColorableNodeBeanInfo extends AbstractNodeBeanInfo
{
    public ColorableNodeBeanInfo()
    {
        super(ColorableNode.class);
    }

    protected ColorableNodeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        if(displayBackgroundColor)
        {
            propertyDescriptorList.add(createPropertyDescriptor(BACKGROUND_COLOR_VAR_NAME, BACKGROUND_COLOR_LABEL_KEY, 100));
        }
        if(displayBorderColor)
        {
            propertyDescriptorList.add(createPropertyDescriptor(BORDER_COLOR_VAR_NAME, BORDER_COLOR_LABEL_KEY,101));
        }
        if(displayTextColor)
        {
            propertyDescriptorList.add(createPropertyDescriptor(TEXT_COLOR_VAR_NAME, TEXT_COLOR_LABEL_KEY,102));
        }
        return propertyDescriptorList;
    }

    protected boolean displayBackgroundColor = false;
    protected boolean displayBorderColor = false;
    protected boolean displayTextColor = false;

    protected static final String BACKGROUND_COLOR_LABEL_KEY = "color.background";
    protected static final String BORDER_COLOR_LABEL_KEY = "color.border";
    protected static final String TEXT_COLOR_LABEL_KEY = "color.text";
    private static final String BACKGROUND_COLOR_VAR_NAME = "backgroundColor";
    private static final String BORDER_COLOR_VAR_NAME = "borderColor";
    private static final String TEXT_COLOR_VAR_NAME = "textColor";
}
