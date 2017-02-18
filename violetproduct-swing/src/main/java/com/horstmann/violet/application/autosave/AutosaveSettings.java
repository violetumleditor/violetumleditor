package com.horstmann.violet.application.autosave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AutosaveSettings
{

    private static final String AUTOSAVE_ENABLED = "autosave_enabled";
    private static final String AUTOSAVE_INTERVAL = "autosave_interval";
    private static final String AUTOSAVE_PATH = "autosave_path";
    private static final String AUTOSAVE_CONFIGURATION_FILE = "config.properties";
    private Properties properties = new Properties();
    private InputStream input = null;
    private boolean loadingDialog = false;
    private boolean autosaveEnabled = false;
    private int autosaveInterval;
    private String autosavePath= "";

    /**
     * AutoSave settings
     */
    public AutosaveSettings()
    {
        loadSettings();
    }

    /**
     * Check is loading dialog
     * @return boolean
     */
    public boolean isLoadingDialog()
    {
        return this.loadingDialog;
    }

    /**
     * Check is enable autosave
     * @return boolean
     */
    public boolean isEnableAutosave()
    {
        return this.autosaveEnabled;
    }

    /**
     * Set enable autosave
     * @param enable autosave
     */
    public void setEnableAutosave(boolean enableAutosave)
    {
        this.autosaveEnabled = enableAutosave;
    }

    /**
     * Get autosave interval
     * @return autosave interval
     */
    public int getAutosaveInterval()
    {
        return this.autosaveInterval;
    }

    /**
     * Set autosave interval
     * @param autosave interval
     */
    public void setAutosaveInterval(int autosaveInterval)
    {
        this.autosaveInterval = autosaveInterval;
    }

    /**
     * Get autosave patch
     * @return string autosave path
     */
    public String getAutosavePath()
    {
        return this.autosavePath;
    }

    /**
     * Set autosave path
     * @param autosave path
     */
    public void setAutosavePath(String autosavePath)
    {
        this.autosavePath = autosavePath;
    }

    /**
     * Save settings
     */
    public void saveSettings()
    {
        try
        {
            FileOutputStream output = new FileOutputStream(AUTOSAVE_CONFIGURATION_FILE);
            this.properties.setProperty(AUTOSAVE_ENABLED, String.valueOf(isEnableAutosave()));
            this.properties.setProperty(AUTOSAVE_INTERVAL, String.valueOf(getAutosaveInterval()));
            this.properties.setProperty(AUTOSAVE_PATH, getAutosavePath());
            this.properties.store(output, null);

        }
        catch (FileNotFoundException ex)
        {
            System.err.println("File not found");
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
        	ex.printStackTrace();
        }
        finally
        {
        	this.loadingDialog = false;
             	if (this.input != null)
             	{
                try
                {
                	this.input.close();
                }
                catch (IOException e)
                {
                	System.err.println("Unable to close file");
                	e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Load settings
     */
    private void loadSettings()
    {
    	this.loadingDialog = true;
        try
        {
        	readSettingsFromFile();
            this.properties.load(this.input);
            setEnableAutosave(Boolean.parseBoolean(this.properties.getProperty(AUTOSAVE_ENABLED, "true")));
            setAutosaveInterval(Integer.parseInt((this.properties.getProperty(AUTOSAVE_INTERVAL, "10"))));
            setAutosavePath(this.properties.getProperty(AUTOSAVE_PATH, System.getProperty("user.home")+ File.separator + "VioletUML"));
        }
        catch (IOException io)
        {
            System.err.println("Cannon read the settings from the file");
            io.printStackTrace();
        }
        finally
        {
        	this.loadingDialog = false;
            if (this.input != null)
            {
                try
                {
                	this.input.close();
                }
                catch (IOException exception)
                {
                	System.err.println("Unable to close file");
                    exception.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Read settings from file or create new file with settings
     */
    public void readSettingsFromFile()
    {
    	try
        {
    		this.input = new FileInputStream(AUTOSAVE_CONFIGURATION_FILE);
        }
        catch (FileNotFoundException ex)
        {
        	createNewConfigurationFile();

        }
    }

    /**
     * Create new copy of configuration file
     */
	private void createNewConfigurationFile()
    {
		try
        {
			new File(AUTOSAVE_CONFIGURATION_FILE).createNewFile();
			this.input = new FileInputStream(AUTOSAVE_CONFIGURATION_FILE);
		}
		catch (IOException e)
        {
			System.err.println("Failed to create new configuration file");
			e.printStackTrace();
		}
	}
}
