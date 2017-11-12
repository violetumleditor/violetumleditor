package com.horstmann.violet.application.visualization;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.ResourceShortcutProvider;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;



import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class VisualizationDialog extends JDialog{

    @ResourceBundleBean(key = "dialog.title")
    private String dialogTitle;

    @ResourceBundleBean(key = "dialog.button.label")
    private String closeButtonLabel;

    @ResourceBundleBean(key = "dialog.visualization.image")
    private ImageIcon image;

    @ResourceBundleBean(key = "dialog.visualization.graphImage")
    private ImageIcon graphImage;

    @ResourceBundleBean(key = "dialog.visualization.warningImage")
    private ImageIcon warningImage;

    @ResourceBundleBean(key = "dialog.visualization.statisticsImage")
    private ImageIcon statisticsImage;

    private JPanel pieChartPanel;

    private JPanel averagesPanel;

    private JPanel getMoreStatisticsPanel;

    private JPanel getWarningsPanel;

    private JButton nextButton = new JButton();

    public VisualizationDialog(JFrame parent){
        super(parent);
        ResourceBundleInjector.getInjector().inject(this);
        this.setTitle(dialogTitle);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(600,600));

        JPanel buttonPanel = getButtonPanel();
        this.getContentPane().add(getPieChartPanel(), BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
        setLocation(parent);

    }

    private JPanel getButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder((new EmptyBorder(10 ,10, 10, 10)));
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        buttonPanel.add(this.nextButton, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;

        JButton closeButton = new JButton(this.closeButtonLabel);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) { dispose();}
        });

        this.getRootPane().setDefaultButton(closeButton);
        buttonPanel.add(closeButton, c);
        return buttonPanel;
    }

    private JPanel getPieChartPanel(){

        if(this.pieChartPanel == null){

            JLabel image = new JLabel(this.image);
            JLabel text = new JLabel("Pie Chart Example");
            text.setBorder(new EmptyBorder(0,0,0,4));

            this.pieChartPanel = new JPanel();
            this.pieChartPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
           this.pieChartPanel.add(image, c);


            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            this.pieChartPanel.add(text, c);
        }
        this.nextButton.setText("Averages");
        removeActionListener(this.nextButton);
        this.nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getContentPane().remove(getPieChartPanel());
                getContentPane().add(getAveragesPanel(), BorderLayout.CENTER);
                getContentPane().repaint();
            }
        });
        return this.pieChartPanel;
    }

    private JPanel getAveragesPanel(){

        if(this.averagesPanel == null){

            JLabel image = new JLabel(this.graphImage);
            JLabel text = new JLabel("Averages");
            text.setBorder(new EmptyBorder(0,0,0,4));

            this.averagesPanel = new JPanel();
            this.averagesPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            this.averagesPanel.add(image, c);

            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            this.averagesPanel.add(text, c);
        }
        this.nextButton.setText("More Statistics");
        removeActionListener(this.nextButton);
        this.nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getContentPane().remove(getAveragesPanel());
                getContentPane().add(getMoreStatisticsPanel(), BorderLayout.CENTER);
                getContentPane().repaint();
            }
        });
        return this.averagesPanel;
    }

    private JPanel getMoreStatisticsPanel(){
        if(this.getMoreStatisticsPanel == null){

            JLabel image = new JLabel(this.statisticsImage);
            JLabel text = new JLabel("More Statistics");
            text.setBorder(new EmptyBorder(0,0,0,4));

            this.getMoreStatisticsPanel = new JPanel();
            this.getMoreStatisticsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            this.getMoreStatisticsPanel.add(image, c);

            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            this.getMoreStatisticsPanel.add(text, c);
        }
        this.nextButton.setText("Warnings");
        removeActionListener(this.nextButton);
        this.nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getContentPane().remove(getMoreStatisticsPanel());
                getContentPane().add(getWarningsPanel(), BorderLayout.CENTER);
                getContentPane().repaint();
            }
        });
        return this.getMoreStatisticsPanel;
    }

    private JPanel getWarningsPanel(){
        if(this.getWarningsPanel == null){

            JLabel image = new JLabel(this.warningImage);
            JLabel text = new JLabel("Warnings");
            text.setBorder(new EmptyBorder(0,0,0,4));

            this.getWarningsPanel = new JPanel();
            this.getWarningsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            this.getWarningsPanel.add(image, c);

            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            this.getWarningsPanel.add(text, c);
        }
        this.nextButton.setText("Pie Charts");
        removeActionListener(this.nextButton);
        this.nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getContentPane().remove(getWarningsPanel());
                getContentPane().add(getPieChartPanel(), BorderLayout.CENTER);
                getContentPane().repaint();
            }
        });
        return this.getWarningsPanel;
    }

    private void setLocation(JFrame parent){
        Point point = parent.getLocationOnScreen();
        int x = (int) point.getX() + parent.getWidth() /2;
        int y = (int) point.getY() + parent.getHeight() /2;
        setLocation(x - getWidth() / 2, y - getHeight() / 2);
    }

    private void removeActionListener(JButton button){

        ActionListener[] listeners = button.getActionListeners();
        for (int i = 0; i < listeners.length; i++)
        {
            button.removeActionListener(listeners[i]);
        }
    }
}


