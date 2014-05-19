package mrcpsp.process

import mrcpsp.analytics.CompareResults
import mrcpsp.model.enums.EnumExecutionTypes
import mrcpsp.model.main.Project
import mrcpsp.results.J30PsplibProcessor
import mrcpsp.results.PsplibProcessor
import mrcpsp.results.ResultJsonBuilder
import mrcpsp.utils.*
import org.apache.log4j.Level
import org.apache.log4j.Logger

class ExecutionTypeProcessor {

	static final Logger log = Logger.getLogger(MmProcessor.class)
	ResultsProcessor resultsProcessor
    ResultJsonBuilder resultJsonBuilder
	MmProcessor mmProcessor
    def executionType;
    def hasThread;

	
	public ExecutionTypeProcessor() {
		super()
		mmProcessor = new MmProcessor()
		resultsProcessor = new ResultsProcessor()
        resultJsonBuilder = new ResultJsonBuilder()
	}
	
	public void execute() {
		executionType = UrlUtils.instance.executionType
		hasThread = UrlUtils.instance.hasThread
		
		if (executionType == EnumExecutionTypes.ONE_FILE.name) {
			executeOneFile()
		} else if (executionType == EnumExecutionTypes.ONE_FILE_TIMES.name) {
			executeOneFileTimes()
		} else if (executionType == EnumExecutionTypes.ALL.name) {
			
			/*if (hasThread == PropertyConstants.TRUE) {
				executeAllFilesConcurrent()
			} else {*/
				executeAllFiles()
			/*}*/
			
		} else if (executionType == EnumExecutionTypes.ALL_TIMES.name) {
			
			/*if (hasThread == PropertyConstants.TRUE) {
				executeAllFilesConcurrent()
			} else {*/
				executeAllFilesTimes()
			/*}*/
			
		} else if (executionType == EnumExecutionTypes.READ_PSPLIB_INSTANCE.name) {
            readResultsFromPsplibFile()
        } else if (executionType == EnumExecutionTypes.ANALYTICS.name) {
            generateResults()
        } else {
			log.log(Level.ERROR, "Argument not supported!" + LogUtils.generateErrorLog(Thread.currentThread().getStackTrace()))
			throw new IllegalArgumentException("Argument not supported!" + LogUtils.generateErrorLog(Thread.currentThread().getStackTrace()))
		}	
		
	}

    public void executeOneFile() {
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)
		
		mmProcessor.basicOperationsInstance(fileName)
        executeAll()
        addInstanceResultForJson(mmProcessor.project)
        writeResult()
        checkGenerateDiagram()
        printTimeExecution()
	}

	public void executeOneFileTimes() {
		Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes())
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)

        mmProcessor.basicOperationsInstance(fileName)
		for (int i = 0; i < timesToRun; i++) {
            log.info("Another execution for the file $fileName")
            executeAll()
			resultsProcessor.checkLowerMakespan(mmProcessor.project)
		}

        addInstanceResultForJson(resultsProcessor.lowerProjectMakespan)
        writeResult()
        checkGenerateDiagram()
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

    /*public void executeAllFilesConcurrent() {
        log.info("======== Executing in CONCURRENT MODE")
        MrcpspWorkerPool pool = new MrcpspWorkerPool()
        try {
            pool.executeAllFilesConcurrent()
        } catch (InterruptedException e) {
            e.printStackTrace()
        }
    }*/

    public void executeAllFilesTimes() {
        List<File> allFiles = FileUtils.getAllFilesInstances()
        def size = allFiles.size()
        def count = 1
        Integer timesToRun = Integer.parseInt(UrlUtils.instance.executionTimes)

        allFiles.each { file ->
            writeStatusToFile("Executing $file.name -- $count of $size")

            mmProcessor.basicOperationsInstance(file.name)
            for (int i = 0; i < timesToRun; i++) {
                log.info("Another execution for the file $file.name")
                executeAll()
                resultsProcessor.checkLowerMakespan(mmProcessor.project)
            }

            addInstanceResultForJson(resultsProcessor.lowerProjectMakespan)
            resultsProcessor.lowerProjectMakespan = null
            resultsProcessor.averageMakespan = 0
            count++
        }

        writeResult()
        printTimeExecution()
    }
	
	private void executeAll() {
		mmProcessor.initialSolutionWithGrasp()
        checkLocalSearchExecution()
        checkPerturbationExecution()
        ChronoWatch.instance.totalTimeSolution = 0
	}
	
	private void checkLocalSearchExecution() {
		Integer executeLocalSearch = UrlUtils.instance.executeLocalSearch
		
		if (executeLocalSearch == PropertyConstants.TRUE) {
			mmProcessor.localSearchDescentUphillMethod()
		}
	}

    private void checkPerturbationExecution() {
        Integer perturbation = UrlUtils.instance.perturbation

        if (perturbation == PropertyConstants.TRUE) {
            mmProcessor.perturbation()
        }
    }

    private void checkGenerateDiagram() {
        Integer generateDiagram = UrlUtils.instance.generateDiagram

        if (generateDiagram == PropertyConstants.TRUE) {
            if (executionType == EnumExecutionTypes.ONE_FILE.name) {
                mmProcessor.generateDiagram(mmProcessor.project)
            } else if (executionType == EnumExecutionTypes.ONE_FILE_TIMES.name) {
                mmProcessor.generateDiagram(resultsProcessor.lowerProjectMakespan)
            }
        }
	}

	def removeOldResultFiles() {
		FileUtils.removeAllFilesFromFolder(PropertyConstants.RESULTS_PATH)
	}

    def readResultsFromPsplibFile() {
        String fileName = PropertyManager.instance.getProperty(PropertyConstants.INSTANCE_FILE)

        if (fileName != "j30hrs.mm") {
            PsplibProcessor pp = new PsplibProcessor()
            pp.readResultsFromFile(fileName)
        } else {
            J30PsplibProcessor jpp = new J30PsplibProcessor()
            jpp.readResultsFromFile(fileName)
        }
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

    def generateResults() {
        CompareResults compareResults = new CompareResults()

        compareResults.compareInstances()
    }

    def writeStatusToFile(String data) {
        String pathFile = UrlUtils.instance.getUrlForResultsFileToOneInstance("status.txt")

        FileUtils.writeToFile(new File(pathFile), data, false)
    }
}
