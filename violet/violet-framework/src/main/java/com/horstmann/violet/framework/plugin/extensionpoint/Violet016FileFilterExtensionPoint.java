package com.horstmann.violet.framework.plugin.extensionpoint;

import java.util.Map;

/**
 * Plugin extension point. By implementing this interfaces, this diagram plugin will
 * keep compatibility with the old Violet 0.16 release.
 * 
 * @author Alexandre de Pellegrin
 *
 */
public interface Violet016FileFilterExtensionPoint
{
    /**
     * @return mapping to convert Violet 0.16 input/output streams.
     * Keys contain Violet 0.16 strings
     * Values contain the real string to read the stream.
     * So, how does it work? When reading a 0.16 document, we look for all this keys
     * and replace them all by their correct value. When saving the document, we replace
     * all values by their 0.16 key.
     */
    public Map<String, String> getMappingToKeepViolet016Compatibility();
    
}
