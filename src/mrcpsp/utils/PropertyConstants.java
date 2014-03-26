package mrcpsp.utils;


/**
 * @author mrcosta
 *
 */
public interface PropertyConstants {
	
	//############# GENERAL #################	
	public static final Integer INDEX_START = 0;
	public static final Integer LINE_MODES_SIZE_WITH_ID = 7;	
	public static final Integer PREFIX_AMOUNT_MODES_SIZE = 2;
	public static final Integer TRUE = 1;
	public static final Integer FALSE = 0;
	
	//############# MRCPSP #################
	public static final String INSTANCE_BASE_URL = System.getProperty("user.dir");	
	public static final String INSTANCES_FOLDER = "instanceFolder";
	public static final String INSTANCE_FILE = "instanceFile";
	
	public static final String START_LINE_JOBS = "startLineJobs";
	public static final String START_LINE_MODES = "startLineModes";
	public static final String START_LINE_RESOURCES_AVAILABILITIES = "startLineResourceAvailabilities";

	public static final String RCL_SIZE = "rclSize";
	
	public static final String JOB_PRIORITY_RULE = "jobPriorityRule";
	
	public static final Integer INSTANCE_MAKESPAN_ERROR = -999;

    //############# MODES  ###############
    public static final String MODE_JOBS = "jobsMode";
    public static final String MODE_SHORTER_NEAR_TO_LOWER_NR_PERCENTAGE = "modeShorterNearToLowerNrPercentage";
    public static final String MODE_SHORTER_NEAR_TO_LOWER_NR_UNIT = "modeShorterNearToLowerNrUnit";
	
	//############# MRCPSP - EXECUTION  #################
	public static final String EXECUTION_TYPE = "executionType";
	public static final String HAS_THREAD = "thread";
	public static final String EXECUTION_TIMES = "executionTimes";
	public static final String CONCURRENT_POOLSIZE = "concurrentPoolSize";
    public static final String GENERATE_DIAGRAM = "generateDiagram";
    public static final String WRITE_LOWERBOUND = "writeLowerboundForAllInstances";
    public static final String SHOW_CRITICAL_PATH = "showCriticalPath";
    public static final String SHOW_LOWER_BOUND = "showLowerBound";
    public static final String TEST_NAME = "testName";
    public static final String TEST_DESCRIPTION = "testDescription";
	
	//############# MRCPSP - TYPE - LOCALSEARCH  #################
	public static final String LOCAL_SEARCH = "localSearchType";
	public static final String EXECUTE_LOCAL_SEARCH = "executeLocalSearch";
	
	//############# OPERATIONS #################
	public static final Integer SUBTRACT = 0;
	public static final Integer ADD = 1;
		
	//############# RESULTS #################
	public static final String RESULTS_PATH = System.getProperty("user.home") + "/mrcpsp/results";

    //############# DIAGRAM #################
    public static final String DIAGRAM_PATH = "diagramPath";
    public static final String SHOW_PREDECESSORS = "showPredecessorsInDiagram";
}
