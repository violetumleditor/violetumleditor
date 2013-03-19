/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.eclipseplugin.editors;

import java.awt.Container;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryListener;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.theme.ThemeManager;

/**
 * The dialog manager has to display dialogs sent by the Violet dialog factory (since Violet cannot display its directly when it is
 * embedded in Eclipse)
 * 
 * The manager is a singleton. Only one instance is needed for all editor instances.
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class EclipseDialogFactory
{
    /**
     * Private constructor (singleton)
     */
    private EclipseDialogFactory()
    {
        BeanInjector.getInjector().inject(this);
        dialogFactory.setListener(new DialogFactoryListener()
        {

            public void mustDisplayPanel(JOptionPane optionPane, String title, boolean isModal)
            {
                displayOptionPane(optionPane, title, isModal);
            }
        });
    }

    /**
     * Initializes singleton instance (called on pluhin startup)
     */
    public static void init()
    {
        if (dialogManager == null)
        {
            dialogManager = new EclipseDialogFactory();
        }
    }

    /**
     * Displays a swing option pane
     * 
     * @param optionPane
     * @param title
     * @param parent
     */
    private void displayOptionPane(final JOptionPane optionPane, final String title, final boolean isModal)
    {
        final Display d = Display.getDefault();
        // Note : the asyncrhonous is important to let Eclipse initialize its shell
        d.asyncExec(new Runnable()
        {
            public void run()
            {
                Shell shell = d.getActiveShell();
                // Add swing frames
                final Composite awtContainer = new Composite(shell, SWT.EMBEDDED);
                final Frame frame = SWT_AWT.new_Frame(awtContainer);
                frame.setVisible(false);
                final JDialog dialog = new JDialog(frame);
                dialog.setTitle(title);
                Container contentPane = dialog.getContentPane();
                contentPane.setBackground(ThemeManager.getInstance().getTheme().getBackgroundColor());
                contentPane.add(optionPane);
                dialog.pack();
                int x = shell.getLocation().x + shell.getBounds().width / 2 - dialog.getWidth() / 2;
                int y = shell.getLocation().y + shell.getBounds().height / 2 - dialog.getHeight() / 2;
                dialog.setLocation(x, y);
                dialog.setVisible(true);
                optionPane.addPropertyChangeListener(new PropertyChangeListener()
                {
                    public void propertyChange(PropertyChangeEvent event)
                    {
                        if (dialog.isVisible() && (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY))
                                && event.getNewValue() != null && event.getNewValue() != JOptionPane.UNINITIALIZED_VALUE)
                        {
                            dialog.dispose();
                            d.syncExec(new Runnable()
                            {
                                public void run()
                                {
                                    awtContainer.dispose();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    /**
     * Unique class instance
     */
    private static EclipseDialogFactory dialogManager;
    
    @InjectedBean
    private DialogFactory dialogFactory;
}
