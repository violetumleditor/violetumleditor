package com.horstmann.violet.framework.util.string;

import com.horstmann.violet.framework.util.string.decorator.OneLineString;

/**
 * Created by Adrian Bobrowski on 17.12.2015.
 */
public interface Converter extends Cloneable {
    OneLineString convertTextToLineString(String text);
}
