package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryMode;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.theme.ClassicMetalTheme;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.framework.userpreferences.DefaultUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.IUserPreferencesDao;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.Workspace;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollBar;

import static com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanFactory;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains common behavior test operations, helps with writing shorter tests.
 */
final class DragEditorPartBehaviorTestHelper
{

    private DragEditorPartBehaviorTestHelper(){

    }

    /**
     * Initializes workspace instance in fastest possible way.
     * @return diagram workspace.
     */
    static Workspace initWorkspace()
    {
        final IUserPreferencesDao userPreferencesDao = new DefaultUserPreferencesDao();
        BeanFactory.getFactory().register(IUserPreferencesDao.class, userPreferencesDao);

        final ThemeManager themeManager = new ThemeManager();
        final ITheme theme = new ClassicMetalTheme();
        final List<ITheme> themeList = new ArrayList<ITheme>();
        themeList.add(theme);
        themeManager.setInstalledThemes(themeList);
        BeanFactory.getFactory().register(ThemeManager.class, themeManager);
        themeManager.applyPreferedTheme();

        final DialogFactory dialogFactory = new DialogFactory(DialogFactoryMode.INTERNAL);
        BeanFactory.getFactory().register(DialogFactory.class, dialogFactory);

        final GraphFile graphFile = mock(GraphFile.class);
        final IGraph graph = mock(IGraph.class);
        when(graphFile.getGraph()).thenReturn(graph);

        return new Workspace(graphFile);
    }

    static MouseEvent createMiddleMouseEvent(final int posX, final int posY)
    {
        final MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getModifiers()).thenReturn(MouseEvent.BUTTON2);
        when(mouseEvent.getPoint()).thenReturn(new Point(posX, posY));
        when(mouseEvent.getModifiersEx()).thenReturn(InputEvent.BUTTON2_DOWN_MASK);
        return mouseEvent;
    }

    static void resetScrollBarState(final JScrollBar scrollBar){
        scrollBar.setValue(0);
        scrollBar.setMinimum(0);
        scrollBar.setMaximum(100);
    }
}
