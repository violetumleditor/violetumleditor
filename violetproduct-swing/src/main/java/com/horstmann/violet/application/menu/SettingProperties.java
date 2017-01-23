package com.horstmann.violet.application.menu;

import com.horstmann.violet.product.diagram.classes.node.ClassNode;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by Marcin on 12.01.2017.
 */
public class SettingProperties {

    private static final File propertiesFile = new File(System.getProperty("user.home") + File.separator + "user.properties");
    private static final String classNameProperties = "StartFromBig";
    private String selectedClassName = "disabled";

    public SettingProperties() {
        if (IsPropertiesFileExist()) {
            loadProperties();

            if (getSelectedClassName().equals("enabled")) {
                ClassNode.nameChange = true;
            }
        }

    }



    public String getSelectedClassName() {
        return selectedClassName;
    }

    public void setSelectedClassName(String selectedClassName) {
        this.selectedClassName = selectedClassName;
    }

    /**
     * Check is properties file exist
     *
     * @return boolean
     */
    public boolean IsPropertiesFileExist() {
        return propertiesFile.exists() && !propertiesFile.isDirectory();

    }

    /**
     * Create properties file
     */
    private void createPropertiesFile() {
        try {

            OutputStream out = new FileOutputStream(propertiesFile);
            out.close();
            savePropertiesToFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save properties
     */
    public void savePropertiesToFile() {

        if (IsPropertiesFileExist()) {
            try {
                Properties props = new Properties();
                OutputStream out = new FileOutputStream(propertiesFile);
                props.setProperty(classNameProperties, selectedClassName);
                props.store(out, "User properties");
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else

            createPropertiesFile();

    }

    /**
     * Load properties
     */
    public void loadProperties() {

        try {

            Properties props = new Properties();
            InputStream in = new FileInputStream(propertiesFile);
            props.load(in);
            selectedClassName = props.getProperty(classNameProperties);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
