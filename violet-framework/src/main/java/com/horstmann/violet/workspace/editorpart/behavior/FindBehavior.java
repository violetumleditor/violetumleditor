package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.injection.bean.ManiocFramework;
import com.horstmann.violet.framework.util.FindWindowUI;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;


/**
 * Created by Bartosz Śledź on 31.12.2016.
 */
public class FindBehavior extends AbstractEditorPartBehavior {

    private final static Color ORIGINAL_COLOR = new Color(51,51,51);
    private final static Color NEW_COLOR = Color.RED;
    private final static String EMPTY_STRING = "";
    private final static int START_INDEX = 0;

    private IEditorPart editorPart;
    private int indexOfCurrentlyFoundPhrase = START_INDEX;
    private FindWindowUI findWindow;

    public FindBehavior(final IEditorPart editorPart) {
        ManiocFramework.BeanInjector.getInjector().inject(this);
        this.editorPart = editorPart;
    }

    public FindBehavior() {
    }

    /**
     * Its main function in this class.
     * This method initialize new window for find function and add listener for buttons.
     */
    public void find() {
        this.findWindow = new FindWindowUI();
        addListenersInFindWindowsComponents();
    }

    /**
     * This method add listener for all buttons in find window.
     */
    private void addListenersInFindWindowsComponents() {
        clearFieldListener();
        findButtonListener();
        closeFindFrameButtonListener();
    }

    private void findButtonListener() {
        findWindow.addFindButtonActionListener(e -> {
            refreshView();
            clearFoundText();

            final Collection<INode> allNodes = getAllNodes();
            final Collection<IEdge> allEdges = getAllEdges();
            final String searchPhrase = findWindow.getTextFromInputField();

            final List<LineText> foundPhrases = findPhrases(allNodes, allEdges, searchPhrase);
            selectFoundPhrases(foundPhrases);

            refreshView();
        });
    }

    private void clearFieldListener() {
        findWindow.addInputFindFieldActionListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                findWindow.clearFindField();
                refreshView();
            }
        });
    }

    private void closeFindFrameButtonListener() {
        findWindow.addCloseFrameActionListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                try {
                    clearFoundText();
                } finally {
                    findWindow.disposeWindow();
                }
            }
        });
    }

    /**
     * This method compare text from input field and node.
     * If node contains search text add to list and returns them all.
     *
     * @param allNodes     all available nodes.
     * @param allEdges     all available edges.
     * @param searchPhrase phrase to search.
     * @return a found list text.
     */
    public List<LineText> findPhrases(final Collection<INode> allNodes, final Collection<IEdge> allEdges, final String searchPhrase) {

        final List<LineText> foundText = new ArrayList<>();

        checkParameters(allNodes, allEdges);
        if (searchPhrase.trim().equals(EMPTY_STRING)) return foundText;

        allNodes.forEach((node) -> {
            if (nodeHasName(node) && nodeContainsSearchText(node.getName().toString(), searchPhrase)) {
                foundText.add(node.getName());
            }
            if (nodeHasAttributes(node) && nodeContainsSearchText(node.getAttributes().toString(), searchPhrase)) {
                foundText.add(node.getAttributes());
            }
            if (nodeHasMethods(node) && nodeContainsSearchText(node.getMethods().toString(), searchPhrase)) {
                foundText.add(node.getMethods());
            }
        });

        allEdges.forEach((edge) -> {
            if (edgeHasStartLabel(edge) && edgeContainsSearchText(edge.getStartLabel().toString(), searchPhrase)) {
                foundText.add(edge.getStartLabel());
            }
            if (edgeHasCenterLabel(edge) && edgeContainsSearchText(edge.getCenterLabel().toString(), searchPhrase)) {
                foundText.add(edge.getCenterLabel());
            }
            if (edgeHasEndLabel(edge) && edgeContainsSearchText(edge.getEndLabel().toString(), searchPhrase)) {
                foundText.add(edge.getEndLabel());
            }
        });
        return foundText;
    }

    /**
     * This function check income parameters is not null.
     *
     * @param allNodes collection of nodes.
     * @param allEdges collection of edges.
     */
    private void checkParameters(final Collection<INode> allNodes, final Collection<IEdge> allEdges) {
        if (allEdges == null || allNodes == null)
            throw new IllegalArgumentException("Nodes or edges cannot be null.");
    }

    /**
     * This function select found phrases one by one.
     *
     * @param foundPhrases all found phrases with searching phrases.
     */
    private void selectFoundPhrases(final List<LineText> foundPhrases) {

        final int numberOfAllFoundPhrases = foundPhrases.size();

        if (foundPhrases.isEmpty()) {
            setIndexOnFirstElementInSearchList();
            showNumberOfFoundElements(numberOfAllFoundPhrases);
            return;
        }
        if (indexOfCurrentlyFoundPhrase < numberOfAllFoundPhrases) {
            setColorInFoundText(foundPhrases.get(indexOfCurrentlyFoundPhrase), NEW_COLOR);
            indexOfCurrentlyFoundPhrase++;
            showNumberOfFoundElements(numberOfAllFoundPhrases);
        }
        if (indexOfCurrentlyFoundPhrase == numberOfAllFoundPhrases) {
            foundPhrases.clear();
            setIndexOnFirstElementInSearchList();
        }
    }

    /**
     * This method display info how many items found and current position.
     *
     * @param numberOfAllFoundPhrases int number of all found phrases.
     */
    private void showNumberOfFoundElements(final int numberOfAllFoundPhrases) {
        findWindow.setNumberOfFound(indexOfCurrentlyFoundPhrase + "/" + numberOfAllFoundPhrases);
    }

    /**
     * This method check node contains text with set input field.
     *
     * @param nodeText   text from node.
     * @param searchText search text.
     * @return true if node contains specific text.
     */
    private boolean nodeContainsSearchText(final String nodeText, final String searchText) {
        return nodeText.toLowerCase().contains(searchText);
    }

    /**
     * This method check edge contains text with set input field.
     *
     * @param edgeText   text from edge.
     * @param searchText search text.
     * @return true if edge contains specific text.
     */
    private boolean edgeContainsSearchText(final String edgeText, final String searchText) {
        return edgeText.toLowerCase().contains(searchText);
    }

    /**
     * This method clear all found text on view and set original color of text.
     */
    private void clearFoundText() {

        getAllNodes().forEach((node) -> {
            if (nodeHasName(node)) {
                node.getName().setTextColor(ORIGINAL_COLOR);
            }
            if (nodeHasAttributes(node)) {
                node.getAttributes().setTextColor(ORIGINAL_COLOR);
            }
            if (nodeHasMethods(node)) {
                node.getMethods().setTextColor(ORIGINAL_COLOR);
            }
        });

        getAllEdges().forEach((edge) -> {
            if (edgeHasStartLabel(edge)) {
                edge.getStartLabel().setTextColor(ORIGINAL_COLOR);
            }
            if (edgeHasCenterLabel(edge)) {
                edge.getCenterLabel().setTextColor(ORIGINAL_COLOR);
            }
            if (edgeHasEndLabel(edge)) {
                edge.getEndLabel().setTextColor(ORIGINAL_COLOR);
            }
        });

        refreshView();
    }

    /**
     * This method set text color on selected item.
     *
     * @param nodeText text in selected node.
     * @param color    just color.
     */
    private void setColorInFoundText(final LineText nodeText, final Color color) {
        nodeText.setTextColor(color);
    }

    /**
     * This method refresh the view after press find button.
     */
    private void refreshView() {
        editorPart.getSwingComponent().invalidate();
        editorPart.getSwingComponent().repaint();
    }

    /**
     * This function set index in list of search phrases.
     */
    private void setIndexOnFirstElementInSearchList() {
        indexOfCurrentlyFoundPhrase = START_INDEX;
    }

    /**
     * This method get all nodes.
     *
     * @return collection of nodes.
     */
    private Collection<INode> getAllNodes() {
        return editorPart.getGraph().getAllNodes();
    }

    /**
     * This method get all edges.
     *
     * @return collection of edges.
     */
    private Collection<IEdge> getAllEdges() {
        return editorPart.getGraph().getAllEdges();
    }

    /**
     * This method check if node has some name.
     *
     * @param node single node.
     * @return true if node has some name in string.
     */
    private boolean nodeHasName(final INode node) {
        return node.getName() != null;
    }

    /**
     * This method check if node has some attributes.
     *
     * @param node single node.
     * @return true if node has some attributes in string.
     */
    private boolean nodeHasAttributes(final INode node) {
        return node.getAttributes() != null;
    }

    /**
     * This method check if node has some methods.
     *
     * @param node single node.
     * @return true if node has some methods in string.
     */
    private boolean nodeHasMethods(final INode node) {
        return node.getMethods() != null;
    }

    /**
     * This method check if edge has start label.
     *
     * @param edge single edge.
     * @return true if edge has start label.
     */
    private boolean edgeHasStartLabel(final IEdge edge) {
        return edge.getStartLabel() != null;
    }

    /**
     * This method check if edge has central label.
     *
     * @param edge single edge.
     * @return true if edge has central label.
     */
    private boolean edgeHasCenterLabel(final IEdge edge) {
        return edge.getCenterLabel() != null;
    }

    /**
     * This method check if edge has end label.
     *
     * @param edge single edge.
     * @return true if edge has end label.
     */
    private boolean edgeHasEndLabel(final IEdge edge) {
        return edge.getEndLabel() != null;
    }
}