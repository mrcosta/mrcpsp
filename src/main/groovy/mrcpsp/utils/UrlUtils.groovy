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
	
	Integer getRCLSize() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.RCL_SIZE))
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
	
	String getExecutionTimes() {
		return PropertyManager.getInstance().getProperty(PropertyConstants.EXECUTION_TIMES)
	}
	
	Integer getHasThread() {
		return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.HAS_THREAD))
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

    Integer getGenerateDiagram() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.GENERATE_DIAGRAM))
    }

    String getDiagramPath() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.DIAGRAM_PATH)
    }

    Integer getShowPredecessors() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SHOW_PREDECESSORS))
    }

    Integer getModeShorterNearToLowerNrPercentage() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.MODE_SHORTER_NEAR_TO_LOWER_NR_PERCENTAGE))
    }

    Integer getModeShorterNearToLowerNrUnit() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.MODE_SHORTER_NEAR_TO_LOWER_NR_UNIT))
    }

    Integer getWriteLowerboundForAllInstances() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.WRITE_LOWERBOUND))
    }

    Integer getShowCriticalPath() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SHOW_CRITICAL_PATH))
    }

    Integer getShowLowerBound() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.SHOW_LOWER_BOUND))
    }

    public String getTestName() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.TEST_NAME)
    }

    public String getTestDescription() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.TEST_DESCRIPTION)
    }

    Integer getPerturbation() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.PERTURBATION))
    }

    public String getModesRankingCriteria() {
        return PropertyManager.getInstance().getProperty(PropertyConstants.MODES_RANKING_CRITERIA)
    }

    Integer getModesRankingJobsReverseOrder() {
        return Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.MODES_RANKING_JOBS_REVERSE_ORDER))
    }
}
