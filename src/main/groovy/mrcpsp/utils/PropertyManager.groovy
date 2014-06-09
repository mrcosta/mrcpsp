package mrcpsp.utils

import groovy.json.JsonSlurper;

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
	
	static final String PROPERTY_MRCPSP_PATH = "conf/mrcpsp.properties"
	static final String PROPERTY_INSTANCE_CONFIG_PATH = "conf/instancesConf/"
	static final Logger log = Logger.getLogger(PropertyManager.class)
	
	Properties prop
	static PropertyManager instance
	boolean isLoaded

	void init() {
		prop = new Properties()
		setLog4JLevel(Level.INFO)
		loadProperties()
	}

    void initByJson(String jsonString) {
        prop = new Properties()
        setLog4JLevel(Level.INFO)
        loadPropertiesFromJson(jsonString)
    }
	
	static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
            instance.init();
        }
        return instance;
    }

    static PropertyManager getInstance(String testConfig) {
        instance = new PropertyManager();
        instance.initByJson(testConfig)

        return instance;
    }

    static PropertyManager newInstance() {
        instance = new PropertyManager();
        return instance;
    }

    String getProperty(String key) {
        if (prop == null) {
        	loadProperties();
        }
        return prop.getProperty(key).trim();
    }
    
    /*public boolean isPropertySet(String key) {
        return prop.getProperty(key) != null;
    }

	public void reload() {
        log.info("Reloading properties . . .");
        loadProperties();
    }*/
	
	void loadProperties() {
        loadPropertiesFromConfigFile()
		loadPropertiesFromInstanceConfigFile()
	}
	
	void loadPropertiesFromConfigFile() {
		try {
            String propertiesPath = PROPERTY_MRCPSP_PATH

            InputStream is = new FileInputStream(propertiesPath)
            prop.load(is)

            log.info("Loading mm properties from file " + propertiesPath + " : DONE")
        } catch (IOException e) {            
            log.error(e.getMessage())
        }		
    }
	
	void loadPropertiesFromInstanceConfigFile() {
		try {
			String pathInstanceFile = this.getProperty(PropertyConstants.INSTANCES_FOLDER)
			File file = new File(pathInstanceFile)
			String configInstanceFile = file.getName().replace(".", "") + ".properties"
			String configInstancePath = PROPERTY_INSTANCE_CONFIG_PATH + configInstanceFile
            
			InputStream is = new FileInputStream(configInstancePath)
            Properties propInstanceFile = new Properties()
            propInstanceFile.load(is)
            
			prop.putAll(propInstanceFile);

            log.info("Loading mm properties from instance config file " + configInstancePath + " : DONE")
            isLoaded = true
        } catch (IOException e) {            
        	log.error(e.getMessage())
        }		
		
	}
	
	void setLog4JLevel(Level level) {
		boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
			    		  getInputArguments().toString().indexOf("jdwp") >= 0
		
	    if (isDebug) {	    	
	    	LogManager.getRootLogger().setLevel(Level.DEBUG)
	    } else {
	    	LogManager.getRootLogger().setLevel(level)
	    }	    
	}

    def loadPropertiesFromJson(String jsonString) {
        def config = new JsonSlurper().parseText(jsonString)

        prop.put("executionType", config.executionType)
        prop.put("instanceFolder", config.instanceFolder)
        prop.put("localSearchType", config.localSearchType)
        prop.put("testName", config.testName)
        prop.put("testDescription", config.testDescription)
        prop.put("totalExecutionTime", config.totalExecutionTime ?: "")

        prop.put("executionTimes", Integer.toString(config.generalConfig.executionTimes))
        prop.put("thread", Integer.toString(config.generalConfig.thread ?: 0))
        prop.put("concurrentPoolSize", config.generalConfig.concurrentPoolSize ?: 8)
        prop.put("instanceFile", config.generalConfig.instanceFile ?: "")
        prop.put("executeLocalSearch", Integer.toString(config.generalConfig.executeLocalSearch))
        prop.put("perturbation", Integer.toString((config.generalConfig.perturbation ?: 0)))
        prop.put("generateDiagram", Integer.toString(config.generalConfig.generateDiagram ?: 0))
        prop.put("diagramPath", config.generalConfig.diagramPath ?: "")
        prop.put("showPredecessorsInDiagram", Integer.toString(config.generalConfig.showPredecessorsInDiagram ?: 0))
        prop.put("writeLowerboundForAllInstances", Integer.toString(config.generalConfig.writeLowerboundForAllInstances ?: 0))
        prop.put("showCriticalPath", Integer.toString(config.generalConfig.showCriticalPath ?: 0))
        prop.put("showLowerBound", Integer.toString(config.generalConfig.showLowerBound ?: 0))

        prop.put("modesRankingCriteria", (config.instanceConfig.modesRankingCriteria ?: "empty"))
        prop.put("rankingJobsReverseOrder", Integer.toString((config.instanceConfig.rankingJobsReverseOrder ?: 0)))
        prop.put("startLineJobs", Integer.toString(config.instanceConfig.startLineJobs))
        prop.put("startLineModes", Integer.toString(config.instanceConfig.startLineModes))
        prop.put("startLineResourceAvailabilities", Integer.toString(config.instanceConfig.startLineResourceAvailabilities))
        prop.put("rclSize", Double.toString(config.instanceConfig.rclSize))
        prop.put("jobsMode", config.instanceConfig.jobsMode)
        prop.put("modeShorterNearToLowerNrPercentage", Integer.toString(config.instanceConfig.modeShorterNearToLowerNrPercentage ?: 0))
        prop.put("modeShorterNearToLowerNrUnit", Integer.toString(config.instanceConfig.modeShorterNearToLowerNrUnit ?: 0))
        prop.put("jobPriorityRule", config.instanceConfig.jobPriorityRule)
        prop.put("temperature", Integer.toString((config.instanceConfig.temperature ?: 0)))
        prop.put("reductionCoefficient", Double.toString((config.instanceConfig.reductionCoefficient ?: 0)))
        prop.put("stoppingCriterion", Double.toString((config.instanceConfig.stoppingCriterion ?: 0)))
        prop.put("totalNeighbor", Integer.toString((config.instanceConfig.totalNeighbor ?: 0)))
    }
	
	/*public boolean isLoaded() {
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
	}*/

}
