package com.horstmann.violet.product.diagram.common.node;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.util.BeanInfo;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public abstract class NodeBeanInfo extends BeanInfo
{
    protected NodeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
        addResourceBundle(ResourceBundleConstant.NODE_AND_EDGE_STRINGS);
    }
}
