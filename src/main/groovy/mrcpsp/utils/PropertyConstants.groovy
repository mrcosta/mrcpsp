package mrcpsp.utils;


/**
 * @author mrcosta
 *
 */
interface PropertyConstants {
	
	//############# GENERAL #################	
	static final Integer TRUE = 1

	//############# MRCPSP #################
	static final String INSTANCE_BASE_URL = System.getProperty("user.dir")
	static final String INSTANCES_FOLDER = "instanceFolder"
	static final String INSTANCE_FILE = "instanceFile"
	
	static final String START_LINE_JOBS = "startLineJobs"
	static final String START_LINE_MODES = "startLineModes"
	static final String START_LINE_RESOURCES_AVAILABILITIES = "startLineResourceAvailabilities"

	static final String RCL_SIZE = "rclSize"
	
	static final String JOB_PRIORITY_RULE = "jobPriorityRule"

    //############# MODES  ###############
    static final String MODE_JOBS = "jobsMode"
    static final String MODES_RANKING_CRITERIA = "modesRankingCriteria"
    static final String MODES_RANKING_JOBS_REVERSE_ORDER = "rankingJobsReverseOrder"
	
	//############# MRCPSP - EXECUTION  #################
	static final String EXECUTION_TYPE = "executionType"
	static final String EXECUTION_TIMES = "executionTimes"
    static final String TEST_NAME = "testName"
    static final String TEST_DESCRIPTION = "testDescription"
	
	//############# MRCPSP - TYPE - LOCALSEARCH  #################
	static final String LOCAL_SEARCH = "localSearchType"
	static final String EXECUTE_LOCAL_SEARCH = "executeLocalSearch"

	//############# RESULTS #################
	static final String RESULTS_PATH = System.getProperty("user.home") + "/mrcpsp_results/results"

    //############# SA CONFIGURATION #################
    static final String SA_TEMPERATURE = "temperature"
    static final String SA_REDUCTION_COEFFICIENT = "reductionCoefficient"
    static final String SA_STOPPING_CRITERION = "stoppingCriterion"
    static final String SA_TOTAL_NEIGHBOR = "totalNeighbor"
}
