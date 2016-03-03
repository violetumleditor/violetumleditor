package com.horstmann.violet.framework.dialog;

/**
 * Indicates if dialog boxes are managed by the program or are delegated to a container
 * 
 * To understand it, consider that dialogs could be created internally or could be delegated to external
 * code. For example, it will be delegated if the program is embedded into an Eclipse plugin that uses the SWT API.
 * 
 * @author Alexandre de Pellegrin
 *
 */
public enum DialogFactoryMode
{
    INTERNAL,
    DELEGATED
}
