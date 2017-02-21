package com.horstmann.violet.product.diagram.abstracts;

import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Graph type registry. Each graph should be registered here to be accessible. 
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class GraphRegistry
{
    
    /**
     * Singleton instance
     */
    private static GraphRegistry instance;
    
    /**
     * Resgistry map
     */
    private Map<Class<? extends IGraph>, IDiagramPlugin> registry = new HashMap<Class<? extends IGraph>, IDiagramPlugin>();
    
    /**
     * Singleton constructor
     */
    private GraphRegistry() {
        // Singleton
    }
    
    /**
     * @return registry instance
     */
    public static GraphRegistry getInstance() {
        if (instance == null) {
            instance = new GraphRegistry();
        }
        return instance;
    }
    
    /**
     * Registers a new graph type
     * @param newGraphType t
     */
    public void registerGraphType(IDiagramPlugin newGraphType) {
            this.registry.put(newGraphType.getGraphClass(), newGraphType);
    }
    
    /**
     * @return already registered graph types
     */
    public Collection<IDiagramPlugin> getRegisteredGraphTypes() {
        Collection<IDiagramPlugin> values = this.registry.values();
        return values;
    }
    
}
