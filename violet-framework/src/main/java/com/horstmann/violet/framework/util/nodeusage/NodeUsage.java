package com.horstmann.violet.framework.util.nodeusage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class associates node name with it's usages in other nodes.
 */
public class NodeUsage
{
    private final String nodeName;
    private final List<String> usages = new ArrayList<>();

    /**
     * Creates node usage object with given name
     *
     * @param nodeName
     */
    NodeUsage(final String nodeName)
    {
        this.nodeName = nodeName;
    }

    /**
     * Adds node usage - name of another node
     *
     * @param usage
     */
    void addUsage(final String usage)
    {
        usages.add(usage);
    }

    /**
     * @return true if there are usages of this node
     */
    boolean hasUsages()
    {
        return !usages.isEmpty();
    }

    /**
     * @return object converted to String
     */
    @Override
    public String toString()
    {
        return String.format("%s -> %s", nodeName, usages);
    }
}
