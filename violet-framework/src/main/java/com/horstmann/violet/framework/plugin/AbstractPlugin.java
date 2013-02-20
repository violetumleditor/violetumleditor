package com.horstmann.violet.framework.plugin;

/**
 * This interfaces describes mandatory properties for all plugins
 * 
 * @author Alexandre de Pellegrin
 * 
 */
interface AbstractPlugin
{

    /**
     * @return very short plugin description (ex : Class diagram XMI extension)
     */
    public abstract String getDescription();

    /**
     * 
     * @return plugin's provider or authors
     */
    public abstract String getProvider();

    /**
     * @return plugin's version. Please, keep the pattern aa.bb.cc as major-version.minor-version.patch. (ex : 1.20.0)
     */
    public abstract String getVersion();

}
