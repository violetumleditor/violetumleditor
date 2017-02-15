package com.horstmann.violet.framework.util.nodeusage;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.LineText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This class can be used to find nodes usages represented as {@link NodeUsage} objects.
 */
public class NodeUsagesFinder
{

    private static final String EMPTY_NAME = "";

    /**
     * This function finds usages of nodes. It's searching every node's 'attributes' and 'methods'
     * in order to find nodes names. If it finds any usages they are returned.
     * If no usages are found, empty list is returned.
     *
     * @param selectedNodes list of selected nodes
     * @param allNodes      list of every created node
     * @return found usages - list of {@link NodeUsage} objects
     */
    public List<NodeUsage> findNodesUsages(final List<INode> selectedNodes, final Collection<INode> allNodes)
    {
        verifyParameters(selectedNodes, allNodes);

        final List<NodeUsage> nodesUsages = new ArrayList<>();

        selectedNodes.forEach((node) ->
        {
            final Optional<NodeUsage> nodeUsageOptional = findNodeUsage(node, allNodes);
            nodeUsageOptional.ifPresent(nodesUsages::add);
        });

        return nodesUsages;
    }

    private void verifyParameters(final List<INode> selectedNodes, final Collection<INode> allNodes)
    {
        if (selectedNodes == null || allNodes == null)
        {
            throw new IllegalArgumentException("Argument can't be null");
        }
    }

    private Optional<NodeUsage> findNodeUsage(final INode nodeToVerify, final Collection<INode> allNodes)
    {
        final String nodeToVerifyName = getNodeName(nodeToVerify);
        final NodeUsage nodeUsage = new NodeUsage(nodeToVerifyName);

        for (final INode node : allNodes)
        {
            final LineText attributes = node.getAttributes();
            final LineText methods = node.getMethods();

            if (isUsingNode(nodeToVerifyName, attributes, methods))
            {
                nodeUsage.addUsage(getNodeName(node));
            }
        }

        if (!nodeUsage.hasUsages())
        {
            return Optional.empty();
        }

        return Optional.of(nodeUsage);
    }

    private boolean isUsingNode(final String nodeName, final LineText attributesLineText, final LineText methodsLineText)
    {
        return isContainingNodeName(attributesLineText, nodeName) || isContainingNodeName(methodsLineText, nodeName);
    }

    private boolean isContainingNodeName(final LineText lineText, final String nodeName)
    {
        if (lineText != null)
        {
            if (nodeName.isEmpty())
            {
                return false;
            }

            final String attributes = lineText.toString();
            if (attributes.contains(nodeName))
            {
                return true;
            }
        }

        return false;
    }

    private String getNodeName(final INode node)
    {
        final LineText nodeName = node.getName();

        if (nodeName == null)
        {
            return EMPTY_NAME;
        }

        return nodeName.toString();
    }
}
