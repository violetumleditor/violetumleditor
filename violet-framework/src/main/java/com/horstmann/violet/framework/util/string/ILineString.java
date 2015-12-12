package com.horstmann.violet.framework.util.string;

import java.io.Serializable;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public interface ILineString extends Serializable, Cloneable{
    String toHTML();
    String toLabel();
}
