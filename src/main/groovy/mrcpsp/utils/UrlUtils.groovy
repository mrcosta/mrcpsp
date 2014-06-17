package mrcpsp.utils



/**
 * @author mrcosta
 *
 */
public final class UrlUtils {
	
	private static UrlUtils instance
	
	private static UrlUtils createNewInstance() {
		instance = new UrlUtils()
		return instance
	}
	
	static UrlUtils getInstance() {
		if (instance == null) {
			createNewInstance()
		}
		return instance
	}
	
	String getUrlForInstanceFile() {
		return PropertyConstants.INSTANCE_BASE_URL + 
			   PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCES_FOLDER)
	}
	
	Integer getStartLineJobs() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.START_LINE_JOBS))
	}
	
	Integer getStartLineModes() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.START_LINE_MODES))
	}
	
	Integer getStartLineResourceAvailabilities() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.START_LINE_RESOURCES_AVAILABILITIES))
	}
	
	Double getRCLSize() {
		return Double.parseDouble(PropertyManager.getInstance().getProperty(PropertyConstants.RCL_SIZE))
	}
	
	String getJobsMode() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.MODE_JOBS)
	}
	
	String getJobPriorityRule() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.JOB_PRIORITY_RULE)
	}
	
	String getExecutionType() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.EXECUTION_TYPE)
	}
	
	Integer getExecutionTimes() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.EXECUTION_TIMES))
	}
	
	String getLocalSearch() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.LOCAL_SEARCH)
	}
	
	Integer getExecuteLocalSearch() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.EXECUTE_LOCAL_SEARCH))
	}
	
	String getUrlForResultsFileToOneInstance(String fileName) {
		String pathFolder = PropertyConstants.RESULTS_PATH
		String nameFile = fileName
		
		return FileUtils.createDirectory(pathFolder) + "/" + nameFile
	}
	
	String getUrlForInstance() {
		return PropertyConstants.INSTANCE_BASE_URL + 
			   PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCES_FOLDER)
	}

    public String getTestName() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.TEST_NAME)
    }

    public String getTestDescription() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.TEST_DESCRIPTION)
    }

    public String getModesRankingCriteria() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.MODES_RANKING_CRITERIA)
    }

    Integer getModesRankingJobsReverseOrder() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.MODES_RANKING_JOBS_REVERSE_ORDER))
    }

    Integer getSATemperature() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SA_TEMPERATURE))
    }

    Double getSAReductionCoefficient() {
        return Double.parseDouble(PropertyManager.getInstance().getProperty(PropertyConstants.SA_REDUCTION_COEFFICIENT))
    }

    Double getSAStoppingCriterion() {
        return Double.parseDouble(PropertyManager.getInstance().getProperty(PropertyConstants.SA_STOPPING_CRITERION))
    }

    Integer getSATotalNeighbor() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SA_TOTAL_NEIGHBOR))
    }
}
