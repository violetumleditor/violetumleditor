package com.horstmann.violet.application.menu;

import com.horstmann.violet.product.diagram.classes.node.ClassNode;

import java.io.*;
import java.util.Properties;

/**
 * Created by Marcin on 12.01.2017.
 */
public class SettingProperties {

    private static final File propertiesFile = new File(System.getProperty("user.home") + File.separator + "user.properties");
    private static final String classNameProperties = "StartFromBig";
    private String selectedClassNameOption = "disabled";

    /**
     * Default constructor
     */
    public SettingProperties() {
        if (IsPropertiesFileExist()) {
            loadProperties();

            if (getSelectedClassNameOption().equals("enabled")) {
                ClassNode.classNameChange = true;
            }
        }

    }

    /**
     * Get classname option
     *
     * @return selected option
     */
    public String getSelectedClassNameOption() {
        return selectedClassNameOption;
    }

    /**
     * Set classname option
     * @param selectedClassNameOption
     */
    public void setSelectedClassNameOption(String selectedClassNameOption) {
        this.selectedClassNameOption = selectedClassNameOption;
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
     * Save properties to variables
     */
    public void savePropertiesToFile() {

        if (IsPropertiesFileExist()) {
            try {
                Properties props = new Properties();
                OutputStream out = new FileOutputStream(propertiesFile);
                props.setProperty(classNameProperties, selectedClassNameOption);
                props.store(out, "User properties");
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else

            createPropertiesFile();

    }

    /**
     * Load properties from File
     */
    public void loadProperties() {

        try {

            Properties props = new Properties();
            InputStream inputStream = new FileInputStream(propertiesFile);
            props.load(inputStream);
            selectedClassNameOption = props.getProperty(classNameProperties);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
