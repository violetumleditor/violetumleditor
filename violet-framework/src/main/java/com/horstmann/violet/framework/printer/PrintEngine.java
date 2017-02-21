package com.horstmann.violet.framework.printer;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

@ResourceBundleBean(resourceReference = PrintPanel.class)
public class PrintEngine
{

    public PrintEngine(IGraph graph)
    {
        BeanInjector.getInjector().inject(this);
    	ResourceBundleInjector.getInjector().inject(this);
        this.graph = graph;
    }

    public void start()
    {
        PrintPanel printPanel = new PrintPanel(this.graph);
        JOptionPane optionPane = new JOptionPane();
        optionPane.setOptions(new String[]
        {
            this.printCloseText
        });
        optionPane.setMessage(printPanel);
        optionPane.setBorder(new EmptyBorder(0, 0, 10, 0));
        this.dialogFactory.showDialog(optionPane, this.printTitle, true);
    }

    @ResourceBundleBean(key = "dialog.print.close.text", resourceReference = PrintPanel.class)
    private String printCloseText;
    
    @ResourceBundleBean(key = "dialog.print.print.text", resourceReference = PrintPanel.class)
    private String printTitle;
    
    private IGraph graph;
    
    @InjectedBean
    private DialogFactory dialogFactory;

}
