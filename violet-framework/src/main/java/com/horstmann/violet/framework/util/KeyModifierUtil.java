package com.horstmann.violet.framework.util;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Utility class for convenient checking of modifier keys (Ctrl, Shift, Alt,
 * Meta) during mouse events.
 *
 * @author Viigoo
 *
 */
public class KeyModifierUtil {

    /**
     * Checks if CTRL key is pressed during mouse event.
     * 
     * @param event MouseEvent to be checked
     * @return true if pressed, false otherwise
     */
    public static boolean isCtrl(MouseEvent event) {
        return isEventModifierMask(event, InputEvent.CTRL_DOWN_MASK);
    }

    /**
     * Checks if SHIFT key is pressed during mouse event.
     * 
     * @param event MouseEvent to be checked
     * @return true if pressed, false otherwise
     */
    public static boolean isShift(MouseEvent event) {
        return isEventModifierMask(event, InputEvent.SHIFT_DOWN_MASK);
    }

    /**
     * Checks if ALT key is pressed during mouse event.
     * 
     * @param event MouseEvent to be checked
     * @return true if pressed, false otherwise
     */
    public static boolean isAlt(MouseEvent event) {
        return isEventModifierMask(event, InputEvent.ALT_DOWN_MASK);
    }

    /**
     * Checks if META key is pressed during mouse event.
     * 
     * @param event MouseEvent to be checked
     * @return true if pressed, false otherwise
     */
    public static boolean isMeta(MouseEvent event) {
        return isEventModifierMask(event, InputEvent.META_DOWN_MASK);
    }

    private static boolean isEventModifierMask(MouseEvent event, int modifierMask) {
        return (event.getModifiersEx() & modifierMask) != 0;
    }
}
