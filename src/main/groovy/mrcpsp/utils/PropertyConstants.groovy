package mrcpsp.utils;


/**
 * @author mrcosta
 *
 */
interface PropertyConstants {
	
	//############# GENERAL #################	
	static final Integer INDEX_START = 0
	static final Integer LINE_MODES_SIZE_WITH_ID = 7
	static final Integer PREFIX_AMOUNT_MODES_SIZE = 2
	static final Integer TRUE = 1
	static final Integer FALSE = 0
	
	//############# MRCPSP #################
	static final String INSTANCE_BASE_URL = System.getProperty("user.dir")
	static final String INSTANCES_FOLDER = "instanceFolder"
	static final String INSTANCE_FILE = "instanceFile"
	
	static final String START_LINE_JOBS = "startLineJobs"
	static final String START_LINE_MODES = "startLineModes"
	static final String START_LINE_RESOURCES_AVAILABILITIES = "startLineResourceAvailabilities"

	static final String RCL_SIZE = "rclSize"
	
	static final String JOB_PRIORITY_RULE = "jobPriorityRule"
	
	static final Integer INSTANCE_MAKESPAN_ERROR = -999

    //############# MODES  ###############
    static final String MODE_JOBS = "jobsMode"
    static final String MODE_SHORTER_NEAR_TO_LOWER_NR_PERCENTAGE = "modeShorterNearToLowerNrPercentage"
    static final String MODE_SHORTER_NEAR_TO_LOWER_NR_UNIT = "modeShorterNearToLowerNrUnit"
    static final String MODES_RANKING_CRITERIA = "modesRankingCriteria"
    static final String MODES_RANKING_JOBS_REVERSE_ORDER = "rankingJobsReverseOrder"
	
	//############# MRCPSP - EXECUTION  #################
	static final String EXECUTION_TYPE = "executionType"
	static final String HAS_THREAD = "thread"
	static final String EXECUTION_TIMES = "executionTimes"
	static final String CONCURRENT_POOLSIZE = "concurrentPoolSize"
    static final String GENERATE_DIAGRAM = "generateDiagram"
    static final String WRITE_LOWERBOUND = "writeLowerboundForAllInstances"
    static final String SHOW_CRITICAL_PATH = "showCriticalPath"
    static final String SHOW_LOWER_BOUND = "showLowerBound"
    static final String TEST_NAME = "testName"
    static final String TEST_DESCRIPTION = "testDescription"
    static final String WRITE_INSTANCE_INFORMATION_TO_JSON = "writeInstanceInformationToJson"
	
	//############# MRCPSP - TYPE - LOCALSEARCH  #################
	static final String LOCAL_SEARCH = "localSearchType"
	static final String EXECUTE_LOCAL_SEARCH = "executeLocalSearch"

    //############# MRCPSP - PERTURBATION  #################
    static final String PERTURBATION = "perturbation"
    static final String JOBS_PERTURBATION = "jobsPerturbation"
	
	//############# OPERATIONS #################
	static final Integer SUBTRACT = 0
	static final Integer ADD = 1
		
	//############# RESULTS #################
	static final String RESULTS_PATH = System.getProperty("user.home") + "/mrcpsp_results/results"

    //############# DIAGRAM #################
    static final String DIAGRAM_PATH = "diagramPath"
    static final String SHOW_PREDECESSORS = "showPredecessorsInDiagram"

    //############# SA CONFIGURATION #################
    static final String SA_TEMPERATURE = "temperature"
    static final String SA_REDUCTION_COEFFICIENT = "reductionCoefficient"
    static final String SA_STOPPING_CRITERION = "stoppingCriterion"
    static final String SA_TOTAL_NEIGHBOR = "totalNeighbor"
}
