package com.horstmann.violet.framework.dialog;

public interface IRevertableProperties
{
    /**
     * Signalize implementing class that it should cache it's cache-able data at time of method call
     */
    void beforeUpdate();

    /**
     * Signalize implementing class that it should revert all changes to it's cache-able properties and assign them their
     * old cached values
     */
    void revertUpdate();
}
