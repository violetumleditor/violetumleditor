package com.horstmann.violet.product.diagram.abstracts.edge;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.util.BeanInfo;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 06.03.2016
 */
public abstract class AbstractEdgeBeanInfo extends BeanInfo
{
    protected AbstractEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
        addResourceBundle(ResourceBundleConstant.NODE_AND_EDGE_STRINGS);
    }
}