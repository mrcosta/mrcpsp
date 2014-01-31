package mrcpsp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author mrcosta
 *
 */
public class PropertyManager {
	
	private static final String PROPERTY_MRCPSP_PATH = "conf/mrcpsp.properties";
	private static final String PROPERTY_INSTANCE_CONFIG_PATH = "conf/instancesConf/";
	private static final Logger log = Logger.getLogger(PropertyManager.class);
	
	private Properties prop;	
	private static PropertyManager instance;
	private boolean isLoaded;	
	
	public PropertyManager() {
		init();
	}
	
	private void init() {
		prop = new Properties();
		setLog4JLevel(Level.INFO);
		loadProperties();		
	}
	
	public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    public static PropertyManager newInstance() {
        instance = new PropertyManager();
        return instance;
    }

    public String getProperty(String key) {
        if (prop == null) {
        	loadProperties();
        }
        return prop.getProperty(key).trim();
    }
    
    public boolean isPropertySet(String key) {
        return prop.getProperty(key) != null;
    }
	
	public void reload() {
        log.info("Reloading properties . . .");
        loadProperties();
    }
	
	private void loadProperties() {
		loadPropertiesFromConfigFile();
		loadPropertiesFromInstanceConfigFile();
	}
	
	private void loadPropertiesFromConfigFile() {        
		try {
            String propertiesPath = PROPERTY_MRCPSP_PATH;

            InputStream is = new FileInputStream(propertiesPath);
            prop.load(is);

            log.info("Loading mm properties from file " + propertiesPath + " : DONE");                       
        } catch (IOException e) {            
            log.error(e.getMessage());
        }		
    }
	
	private void loadPropertiesFromInstanceConfigFile() {
		try {
			String pathInstanceFile = this.getProperty(PropertyConstants.INSTANCES_FOLDER);
			File file = new File(pathInstanceFile);			
			String configInstanceFile = file.getName().replace(".", "") + ".properties";
			String configInstancePath = PROPERTY_INSTANCE_CONFIG_PATH + configInstanceFile;
            
			InputStream is = new FileInputStream(configInstancePath);
            Properties propInstanceFile = new Properties();
            propInstanceFile.load(is);      
            
			prop.putAll(propInstanceFile);

            log.info("Loading mm properties from instance config file " + configInstancePath + " : DONE");
            isLoaded = true;            
        } catch (IOException e) {            
        	log.error(e.getMessage());
        }		
		
	}
	
	public void setLog4JLevel(Level level) {
		boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
			    		  getInputArguments().toString().indexOf("jdwp") >= 0;
		
	    if (isDebug) {	    	
	    	LogManager.getRootLogger().setLevel(Level.DEBUG);	    	
	    } else {
	    	LogManager.getRootLogger().setLevel(level);
	    }	    
	}
	
	public boolean isLoaded() {
        return isLoaded;
    }

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	public void setProperty(String key, String value) {
		this.prop.setProperty(key, value);
	}

}
