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

package com.horstmann.violet.product.diagram.usecase.edge;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassRelationshipEdge type.
 */
public class UseCaseRelationshipEdgeBeanInfo extends SimpleBeanInfo
{
    /*
     * (non-Javadoc)
     * 
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                    new PropertyDescriptor("middleLabel", UseCaseRelationshipEdge.class),
                    new PropertyDescriptor("bentStyle", UseCaseRelationshipEdge.class),
            };
            descriptors[0].setValue("priority", new Integer(0));
            descriptors[0].setName("Text");
            descriptors[1].setValue("priority", new Integer(1));
            descriptors[1].setName("Bent style");

            return descriptors;
        }
        catch (IntrospectionException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

}
