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
	public static final String INSTANCES_FOLDER = "instance.folder";
	public static final String INSTANCE_FILE = "instance.file";
	
	public static final String START_LINE_JOBS = "start.line.jobs";
	public static final String START_LINE_MODES = "start.line.modes";
	public static final String START_LINE_RESOURCES_AVAILABILITIES = "start.line.resourceAvailabilities";

	public static final String RCL_SIZE = "RCL_SIZE";
	
	public static final String JOB_PRIORITY_RULE = "job.priority.rule";
	
	public static final Integer INSTANCE_MAKESPAN_ERROR = -999;

    //############# MODES  ###############
    public static final String MODE_JOBS = "mode.jobs";
    public static final String MODE_SHORTER_NEAR_TO_LOWER_NR_PERCENTAGE = "mode.shorter_near_to_lower_nr.percentage";
    public static final String MODE_SHORTER_NEAR_TO_LOWER_NR_UNIT = "mode.shorter_near_to_lower_nr.unit";
	
	//############# MRCPSP - EXECUTION  #################
	public static final String EXECUTION_TYPE = "execution.type";
	public static final String HAS_THREAD = "thread";
	public static final String EXECUTION_TIMES = "execution.times";
	public static final String CONCURRENT_POOLSIZE = "concurrent.poolSize";
    public static final String GENERATE_DIAGRAM = "generate.diagram";
    public static final String WRITE_LOWERBOUND = "write.lowerbound.for.all.instances";
    public static final String SHOW_CRITICAL_PATH = "show.critical.path";
    public static final String SHOW_LOWER_BOUND = "show.lower.bound";
	
	//############# MRCPSP - TYPE - LOCALSEARCH  #################
	public static final String LOCAL_SEARCH = "type.localSearch";
	public static final String EXECUTE_LOCAL_SEARCH = "execute.localSearch";
	public static final String LOCAL_SEARCH_EVERY_SOLUTION = "execute.localSearch.everyInitialSolution";
	
	
	//############# OPERATIONS #################
	public static final Integer SUBTRACT = 0;
	public static final Integer ADD = 1;
		
	//############# RESULTS #################
	public static final String RESULTS_PATH = System.getProperty("user.home") + "/mrcpsp/results";

    //############# DIAGRAM #################
    public static final String DIAGRAM_PATH = "diagram.path";
    public static final String SHOW_PREDECESSORS = "show.predecessors";
}
