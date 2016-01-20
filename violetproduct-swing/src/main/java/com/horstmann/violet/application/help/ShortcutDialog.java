package com.horstmann.violet.application.help;

import com.horstmann.violet.framework.injection.bean.ManiocFramework;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.ResourceShortcutProvider;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by piter on 02.01.16.
 */
public class ShortcutDialog extends JDialog {

    public ShortcutDialog(JFrame parent){
        super(parent);
        ResourceBundleInjector.getInjector().inject(this);
        ManiocFramework.BeanInjector.getInjector().inject(this);
        this.setTitle(this.dialogTitle);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(createShortcurPanel(), BorderLayout.CENTER);
        this.setSize(400,300);
        setLocation(parent);
    }

    private JPanel createShortcurPanel(){
        if(shortcutPanel == null){
            shortcutPanel = new JPanel(new BorderLayout());
            JTextArea textArea = new JTextArea();
            String[][] data = new String[ResourceShortcutProvider.getInstance().getAllShortcuts().size()][2];
            int counter = 0;
            for(Map.Entry<String, String> entry : ResourceShortcutProvider.getInstance().getAllShortcuts().entrySet()){
                textArea.append(counter+". "+entry.getKey()+" "+entry.getValue()  +"\n");
                data[counter][0] = entry.getKey();
                data[counter][1] = entry.getValue();
                counter++;
            }
            String[] colNmaes = {"Action", "Shortcut"};

            JTable table = new JTable(data, colNmaes);
            table.setEnabled(false);
            table.setCellSelectionEnabled(false);
            table.setShowGrid(true);
            table.setGridColor(Color.black);
            JScrollPane scrollPane = new JScrollPane(table);
            shortcutPanel.add(scrollPane, BorderLayout.CENTER);
        }
        return this.shortcutPanel;
    }
    private void setLocation(JFrame parent)
    {
        Point point = parent.getLocationOnScreen();
        int x = (int) point.getX() + parent.getWidth() / 2;
        int y = (int) point.getY() + parent.getHeight() / 2;
        setLocation(x - getWidth() / 2, y - getHeight() / 2);
    }

    @ResourceBundleBean(key="dialog.title")
    private String dialogTitle;

    private JPanel shortcutPanel = null;
}
