package mrcpsp.process

import mrcpsp.model.enums.EnumExecutionTypes
import mrcpsp.model.main.Project
import mrcpsp.results.ResultJsonBuilder
import mrcpsp.utils.*
import org.apache.log4j.Logger

class ExecutionTypeProcessor {

	static final Logger log = Logger.getLogger(MmProcessor.class)
	ResultsProcessor resultsProcessor
    ResultJsonBuilder resultJsonBuilder
	MmProcessor mmProcessor
    def executionType;

	public ExecutionTypeProcessor() {
		super()
		mmProcessor = new MmProcessor()
		resultsProcessor = new ResultsProcessor()
        resultJsonBuilder = new ResultJsonBuilder()
	}
	
	public void execute() {
		executionType = UrlUtils.instance.executionType

		if (executionType == EnumExecutionTypes.ONE_FILE.name) {
			executeOneFile()
		} else if (executionType == EnumExecutionTypes.ONE_FILE_TIMES.name) {
			executeOneFileTimes()
		} else if (executionType == EnumExecutionTypes.ALL.name) {
            executeAllFiles()
		} else if (executionType == EnumExecutionTypes.ALL_TIMES.name) {
			executeAllFilesTimes()
		}
	}

    public void executeOneFile() {
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)
		
		mmProcessor.basicOperationsInstance(fileName)
        executeAll()
        addInstanceResultForJson(mmProcessor.project)
        writeResult()
        printTimeExecution()
	}

	public void executeOneFileTimes() {
		Integer timesToRun = UrlUtils.getInstance().getExecutionTimes()
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)

        mmProcessor.basicOperationsInstance(fileName)
		for (int i = 0; i < timesToRun; i++) {
            log.info("Another execution for the file $fileName")
            executeAll()
			resultsProcessor.checkLowerMakespan(mmProcessor.project)
		}

        addInstanceResultForJson(resultsProcessor.bestProject)
        writeResult()
        printTimeExecution()
	}

    public void executeAllFiles() {
        List<File> allFiles = FileUtils.getAllFilesInstances()
        def size = allFiles.size()
        def count = 1

        allFiles.each { file ->
            writeStatusToFile("Executing $file.name -- $count of $size")

            mmProcessor.basicOperationsInstance(file.name)
            executeAll()
            addInstanceResultForJson(mmProcessor.project)

            count++
        }

        writeResult()
        printTimeExecution()
    }

    public void executeAllFilesTimes() {
        List<File> allFiles = FileUtils.getAllFilesInstances()
        def size = allFiles.size()
        def count = 1
        Integer timesToRun = UrlUtils.instance.executionTimes

        allFiles.each { file ->
            writeStatusToFile("Executing $file.name -- $count of $size")

            mmProcessor.basicOperationsInstance(file.name)
            for (int i = 0; i < timesToRun; i++) {
                log.info("Another execution for the file $file.name")
                executeAll()
                resultsProcessor.checkLowerMakespan(mmProcessor.project)
            }
            log.info("Best  makespan: ${resultsProcessor.bestProject.makespan}")
            log.info("Staggered jobs: ${resultsProcessor.bestProject.staggeredJobsId}")
            log.info("Modes         : ${resultsProcessor.bestProject.modes}")

            addInstanceResultForJson(resultsProcessor.bestProject)
            resultsProcessor.bestProject = null
            resultsProcessor.averageMakespan = 0
            count++
        }

        writeResult()
        printTimeExecution()
    }
	
	private void executeAll() {
		mmProcessor.initialSolutionWithGrasp()
        checkLocalSearchExecution()
        ChronoWatch.instance.totalTimeSolution = 0
	}
	
	private void checkLocalSearchExecution() {
		Integer executeLocalSearch = UrlUtils.instance.executeLocalSearch
		
		if (executeLocalSearch == PropertyConstants.TRUE) {
			mmProcessor.localSearchDescentUphillMethod()
		}
	}

	def removeOldResultFiles() {
		FileUtils.removeAllFilesFromFolder(PropertyConstants.RESULTS_PATH)
	}

    def addInstanceResultForJson(Project project) {
        resultJsonBuilder.buildInstanceResultJson(project)
    }

    def writeResult() {
        String pathFile = UrlUtils.instance.getUrlForResultsFileToOneInstance(UrlUtils.instance.testName)
        String data = resultJsonBuilder.mergeConfigurationAndResults()

        FileUtils.writeToFile(new File(pathFile), data, false)
    }

    def printTimeExecution() {
        println "Total time execution: $ChronoWatch.instance.totalTimeExecutionFormated"
        println "Total time to find the solution: $ChronoWatch.instance.totalTimeSolutionFormated"
    }

    def writeStatusToFile(String data) {
        String pathFile = UrlUtils.instance.getUrlForResultsFileToOneInstance("status.txt")

        FileUtils.writeToFile(new File(pathFile), data, false)
    }
}
