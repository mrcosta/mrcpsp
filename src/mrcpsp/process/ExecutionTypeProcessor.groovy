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
		
		executeAll(fileName)
        checkGenerateDiagram()
        printTimeExecution()
	}

	public void executeOneFileTimes() {
		Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes())
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)
		
		for (int i = 0; i < timesToRun; i++) {
			executeAll(fileName)			
			resultsProcessor.checkLowerMakespan(mmProcessor.project)
		}

        checkGenerateDiagram()

        writeResult(resultsProcessor.lowerProjectMakespan)
        printTimeExecution()
	}

	public void executeAllFiles() {
						
		for (File file: FileUtils.getAllFilesInstances()) {
			LogUtils.setINSTANCE_STATUS("")			
			executeAll(file.getName())	
		}

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
		Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes())
				
		for (File file: FileUtils.getAllFilesInstances()) {	
			LogUtils.setINSTANCE_STATUS("")			
			
			for (int i = 0; i < timesToRun; i++) {
                executeAll(file.getName())
                resultsProcessor.checkLowerMakespan(mmProcessor.project)
			}

            writeResult(resultsProcessor.lowerProjectMakespan)
			resultsProcessor.lowerProjectMakespan = null
		}

        printTimeExecution()
	}
	
	private void executeAll(String fileName) {
		mmProcessor.initialSolutionWithGrasp(fileName)
        checkLocalSearchExecution();
        ChronoWatch.instance.totalTimeSolution = 0

        if (executionType == EnumExecutionTypes.ONE_FILE.name || executionType == EnumExecutionTypes.ALL.name) {
            writeResult(mmProcessor.project)
        }
	}
	
	private void checkLocalSearchExecution() {
		Integer executeLocalSearch = UrlUtils.getInstance().executeLocalSearch
		
		if (executeLocalSearch == PropertyConstants.TRUE) {
			mmProcessor.localSearchDescentUphillMethod()
		}
	}

    private void checkGenerateDiagram() {
        Integer generateDiagram = UrlUtils.instance.generateDiagram

        if (generateDiagram == PropertyConstants.TRUE) {
            if (executionType == EnumExecutionTypes.ONE_FILE.name) {
                mmProcessor.generateDiagram(mmProcessor.project)
            } else if (executionType == EnumExecutionTypes.ONE_FILE_TIMES.name) {
                mmProcessor.generateDiagram(resultsProcessor.getLowerMakespan())
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

    def writeResult(Project project) {
        resultJsonBuilder.buildInstanceResultJson(project)

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
}
