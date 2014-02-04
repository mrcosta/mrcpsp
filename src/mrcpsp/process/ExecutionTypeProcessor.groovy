package mrcpsp.process

import mrcpsp.model.enums.EnumExecutionTypes
import mrcpsp.process.concurrent.MrcpspWorkerPool
import mrcpsp.utils.*
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

class ExecutionTypeProcessor {

	static final Logger log = Logger.getLogger(MmProcessor.class)
	ResultsProcessor resultsProcessor
	MmProcessor mmProcessor
	ChronoWatch watch
    def executionType;
    def hasThread;

	
	public ExecutionTypeProcessor() {
		super()
		mmProcessor = new MmProcessor()
		resultsProcessor = new ResultsProcessor()
	}
	
	public void execute() {
		PropertyManager.getInstance()		
		executionType = UrlUtils.instance.executionType
		hasThread = UrlUtils.instance.hasThread
		
		removeOldResultFiles()
		SystemUtils.getSystemInformation()
		watch = ChronoWatch.getInstance("MRCPSP Execution").start()
		
		if (checkOneFileExecution()) {
			executeOneFile()
		} else if (checkOneFileTimesExecution()) {
			executeOneFileTimes()
		} else if (StringUtils.equals(executionType, EnumExecutionTypes.ALL.getName())) {
			
			if (hasThread == PropertyConstants.TRUE) {
				executeAllFilesConcurrent()
			} else {
				executeAllFiles()
			}
			
		} else if (StringUtils.equals(executionType, EnumExecutionTypes.ALL_TIMES.getName())) {
			
			if (hasThread == PropertyConstants.TRUE) {
				executeAllFilesConcurrent()
			} else {
				executeAllFilesTimes()
			}
			
		} else {
			log.log(Level.ERROR, "Argument not supported!" + LogUtils.generateErrorLog(Thread.currentThread().getStackTrace()))
			throw new IllegalArgumentException("Argument not supported!" + LogUtils.generateErrorLog(Thread.currentThread().getStackTrace()))
		}	
		
	}

    private boolean checkOneFileTimesExecution() {
        StringUtils.equals(executionType, EnumExecutionTypes.ONE_FILE_TIMES.getName())
    }

    private boolean checkOneFileExecution() {
        StringUtils.equals(executionType, EnumExecutionTypes.ONE_FILE.getName())
    }

    public void executeOneFile() {
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)
		
		executeAll(fileName)
        checkGenerateDiagram()
		
		printResultAndTimeToFile(watch, fileName)
	}

	public void executeOneFileTimes() {
		Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes())
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)
		
		for (int i = 0; i < timesToRun; i++) {
			executeAll(fileName)			
			resultsProcessor.checkLowerMakespan(mmProcessor.project)
		}

        checkGenerateDiagram()
		executeLocalSearch()
		
		resultsProcessor.writeLowerMakespanToOneInstance(fileName)
		printResultAndTimeToFile(watch, fileName)
	}

	public void executeAllFiles() {
						
		for (File file: FileUtils.getAllFilesInstances()) {
			LogUtils.setINSTANCE_STATUS("")			
			executeAll(file.getName())	
		}
		
		printResultAndTimeToFileAllInstances(watch)
	}

	public void executeAllFilesTimes() {
		Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes())
				
		for (File file: FileUtils.getAllFilesInstances()) {	
			LogUtils.setINSTANCE_STATUS("")			
			
			for (int i = 0; i < timesToRun; i++) {
				resultsProcessor.checkLowerMakespan(mmProcessor.initialSolutionWithGrasp(file.getName()))
			}	
			
			resultsProcessor.writeLowerMakespanToAllInstances(file.getName())
			resultsProcessor.setLowerMakespan(null)
		}
		
		printResultAndTimeToFileAllInstances(watch)
	}
	
	private void executeAll(String fileName) {
		mmProcessor.initialSolutionWithGrasp(fileName)	
		checkLocalSearchExecutionEverySolution();
		mmProcessor.executeWriteResults()
	}
	
	private void executeLocalSearch() {
		checkLocalSearchExecution()
		mmProcessor.executeWriteResults()		
	}
	
	private void checkLocalSearchExecution() {
		Integer executeLocalSearch = UrlUtils.getInstance().getExecuteLocalSearch()
		
		if (executeLocalSearch == PropertyConstants.TRUE) {
			mmProcessor.localSearchDescentUphillMethod(resultsProcessor.getLowerMakespan())
		}
	}
	
	private void checkLocalSearchExecutionEverySolution() {
		Integer executeLocalSearchEverySolution = UrlUtils.getInstance().getExecuteLocalSearchEverySolution()
		
		if (executeLocalSearchEverySolution == PropertyConstants.TRUE) {
			mmProcessor.localSearchDescentUphillMethod()
		}
	}

    private void checkGenerateDiagram() {
        Integer generateDiagram = UrlUtils.instance.generateDiagram

        if (generateDiagram == PropertyConstants.TRUE) {
            if (checkOneFileExecution()) {
                mmProcessor.generateDiagram(mmProcessor.project)
            } else if (checkOneFileTimesExecution()) {
                mmProcessor.generateDiagram(resultsProcessor.getLowerMakespan())
            }
        }
    }
	
	private void printResultAndTimeToFile(ChronoWatch watch, String fileName) {
		// finish
		String timeString = watch.time()
		resultsProcessor.writeRunningTimeToResultFile(timeString, fileName)
		log.info(timeString)
	}

	private void printResultAndTimeToFileAllInstances(ChronoWatch watch) {
		// finish
		String timeString = watch.time()
		resultsProcessor.writeRunningTimeToResultFileAllInstances(timeString)
		log.info(timeString)
	}
	
	private void removeOldResultFiles() {
		FileUtils.removeAllFilesFromFolder(PropertyConstants.RESULTS_PATH)
	}

	public void executeAllFilesConcurrent() {
		log.info("======== Executing in CONCURRENT MODE")
		MrcpspWorkerPool pool = new MrcpspWorkerPool()
		try {
			pool.executeAllFilesConcurrent()
		} catch (InterruptedException e) {					
			e.printStackTrace()
		}
	}	

}
