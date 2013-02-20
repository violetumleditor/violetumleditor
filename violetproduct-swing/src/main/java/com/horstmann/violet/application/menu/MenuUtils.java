package com.horstmann.violet.application.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

public class MenuUtils
{

    public static void alterMenuBar(JMenuBar menuBar)
    {

        MenuElement[] subMenuElements = menuBar.getSubElements();
        List<MenuElement> menuEntryStack = new ArrayList<MenuElement>();
        if (subMenuElements.length > 0)
        {
            menuEntryStack.addAll(Arrays.asList(subMenuElements));
        }
        while (!menuEntryStack.isEmpty())
        {
            MenuElement menuElement = menuEntryStack.get(0);
            if (menuElement instanceof JMenuItem)
            {
                final JMenuItem menuItem = (JMenuItem) menuElement;
                menuItem.addActionListener(new ActionListener()
                {

                    public void actionPerformed(ActionEvent e)
                    {
                        System.out.println(menuItem.getName());
                    }

                });
            }
            MenuElement[] subElements = menuElement.getSubElements();
            if (subElements.length > 0)
            {
                menuEntryStack.addAll(Arrays.asList(subElements));
            }
            menuEntryStack.remove(0);
        }
    }
}
