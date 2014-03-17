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
	
	public static UrlUtils getInstance() {
		if (instance == null) {
			createNewInstance()
		}
		return instance
	}
	
	public String getUrlForInstanceFile() {
		return PropertyConstants.INSTANCE_BASE_URL + 
			   PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCES_FOLDER)
	}
	
	public Integer getStartLineJobs() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.START_LINE_JOBS))
	}
	
	public Integer getStartLineModes() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.START_LINE_MODES))
	}
	
	public Integer getStartLineResourceAvailabilities() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.START_LINE_RESOURCES_AVAILABILITIES))
	}
	
	public Integer getRCLSize() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.RCL_SIZE))
	}
	
	public String getJobsMode() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.MODE_JOBS)
	}
	
	public String getJobPriorityRule() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.JOB_PRIORITY_RULE)
	}
	
	def String getExecutionType() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.EXECUTION_TYPE)
	}
	
	public String getExecutionTimes() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.EXECUTION_TIMES)
	}
	
	public Integer getHasThread() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.HAS_THREAD))
	}
	
	public String getLocalSearch() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.LOCAL_SEARCH)
	}
	
	public Integer getExecuteLocalSearch() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.EXECUTE_LOCAL_SEARCH))
	}
	
	public String getUrlForResultsFileToOneInstance(String fileName) {
		String pathFolder = PropertyConstants.RESULTS_PATH
		String nameFile = fileName + "_results.txt"
		
		return FileUtils.createDirectory(pathFolder) + "/" + nameFile
	}
	
	public String getUrlForInstance() {
		return PropertyConstants.INSTANCE_BASE_URL + 
			   PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCES_FOLDER)
	}
	
	public String getUrlForResultsFileToAllInstances() {
		String pathFolder = PropertyConstants.RESULTS_PATH
		String nameFile = "results.txt"
		
		return FileUtils.createDirectory(pathFolder) + "/" + nameFile
	}
	
	public Integer getExecuteLocalSearchEverySolution() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.LOCAL_SEARCH_EVERY_SOLUTION))
	}

    public Integer getGenerateDiagram() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.GENERATE_DIAGRAM))
    }

    public String getDiagramPath() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.DIAGRAM_PATH)
    }

    public Integer getShowPredecessors() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SHOW_PREDECESSORS))
    }

    public Integer getModeShorterNearToLowerNrPercentage() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.MODE_SHORTER_NEAR_TO_LOWER_NR_PERCENTAGE))
    }

    public Integer getModeShorterNearToLowerNrUnit() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.MODE_SHORTER_NEAR_TO_LOWER_NR_UNIT))
    }

    public Integer getWriteLowerBoundForAllInstances() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.WRITE_LOWERBOUND))
    }

    public Integer getShowCriticalPath() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SHOW_CRITICAL_PATH))
    }

    public Integer getShowLowerBound() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SHOW_LOWER_BOUND))
    }
}
