package mrcpsp.utils

import groovy.json.JsonSlurper
import org.apache.log4j.Level
import org.apache.log4j.LogManager
import org.apache.log4j.Logger

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

    String getProperty(String key) {
        if (prop == null) {
        	loadProperties();
        }
        return prop.getProperty(key).trim();
    }
	
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
        prop.put("testName", config.testName)
        prop.put("testDescription", config.testDescription)
        prop.put("totalExecutionTime", config.totalExecutionTime ?: "")

        prop.put("executionTimes", Integer.toString(config.generalConfig.executionTimes))
        prop.put("instanceFile", config.generalConfig.instanceFile ?: "")
        prop.put("executeLocalSearch", Integer.toString(config.generalConfig.executeLocalSearch))

        prop.put("modesRankingCriteria", (config.instanceConfig.modesRankingCriteria ?: "empty"))
        prop.put("rankingJobsReverseOrder", Integer.toString((config.instanceConfig.rankingJobsReverseOrder ?: 0)))
        prop.put("startLineJobs", Integer.toString(config.instanceConfig.startLineJobs))
        prop.put("startLineModes", Integer.toString(config.instanceConfig.startLineModes))
        prop.put("startLineResourceAvailabilities", Integer.toString(config.instanceConfig.startLineResourceAvailabilities))
        prop.put("rclSize", Double.toString(config.instanceConfig.rclSize))
        prop.put("jobsMode", config.instanceConfig.jobsMode)
        prop.put("jobPriorityRule", config.instanceConfig.jobPriorityRule)
        prop.put("temperature", Integer.toString((config.instanceConfig.temperature ?: 0)))
        prop.put("reductionCoefficient", Double.toString((config.instanceConfig.reductionCoefficient ?: 0)))
        prop.put("stoppingCriterion", Double.toString((config.instanceConfig.stoppingCriterion ?: 0)))
        prop.put("totalNeighbor", Integer.toString((config.instanceConfig.totalNeighbor ?: 0)))
    }
}
