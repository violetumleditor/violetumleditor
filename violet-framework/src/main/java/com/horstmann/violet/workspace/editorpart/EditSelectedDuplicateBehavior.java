package com.horstmann.violet.workspace.editorpart;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.node.INamedNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import java.util.Iterator;
import org.jsoup.Jsoup;

/**
 * Class which is detecting name duplicates, by INamedNode interface
 *
 * @author Aleksander Orchowski comodsuda@gmail.com
 * @date 04.12.2016
 */
public class EditSelectedDuplicateBehavior {

    public EditSelectedDuplicateBehavior() {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
    }

    /**
     * @param edited Actually edited node
     * @param nodes Existing nodes iterator
     */
    public void detectDuplicatedNode(final INamedNode edited, final Iterator<INode> nodes,
            final String propertyName) {
        if (propertyName.equals(NAME_OF_DETECTED_PROPERTY)) {
            INode tmpNode;
            while (nodes.hasNext()) {
                tmpNode = nodes.next();
                if (tmpNode instanceof INamedNode) {
                    checkNode(edited, (INamedNode) tmpNode);
                }
            }
        }
    }

    private void checkNode(final INamedNode editedNode, final INamedNode node) {
        final boolean isDuplicate = (!node.equals(editedNode) && getNodeName(node)
                .equals(getNodeName(editedNode)));
        if (isDuplicate) {
            dialogFactory.showPopupNotificationDialog(duplicateDetectedMessage, DIALOG_WIDTH,
                    DIALOG_HEIGHT);
        }
    }

    private String getNodeName(final INamedNode node) {
        String name = node.getName().getText();
        final int stereotypeEnd;
        name = Jsoup.parse(name).text();
        stereotypeEnd = name.indexOf(STEREOTYPE_ENDING) + 1;
        name = name.substring(stereotypeEnd < 0 ? 0 : stereotypeEnd).trim();
        return name;
    }

    @InjectedBean
    private DialogFactory dialogFactory;

    @ResourceBundleBean(key = "edit.properties.duplicate_detected")
    private String duplicateDetectedMessage;
    private final int DIALOG_WIDTH = 150;
    private final int DIALOG_HEIGHT = 50;
    private final String NAME_OF_DETECTED_PROPERTY = "name";
    private final String STEREOTYPE_ENDING = "»";
}
