package com.cefetmg.mmc.mrcpsp.process.impl

import java.io.File

import org.apache.commons.lang.StringUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

import com.cefetmg.mmc.mrcpsp.model.enums.EnumExecutionTypes
import com.cefetmg.mmc.mrcpsp.process.ExecutionTypeProcessor
import com.cefetmg.mmc.mrcpsp.process.MmProcessor
import com.cefetmg.mmc.mrcpsp.process.ResultsProcessor
import com.cefetmg.mmc.mrcpsp.process.concurrent.MrcpspWorkerPool
import com.cefetmg.mmc.mrcpsp.utils.ChronoWatch
import com.cefetmg.mmc.mrcpsp.utils.FileUtils
import com.cefetmg.mmc.mrcpsp.utils.LogUtils
import com.cefetmg.mmc.mrcpsp.utils.PropertyConstants
import com.cefetmg.mmc.mrcpsp.utils.PropertyManager
import com.cefetmg.mmc.mrcpsp.utils.SystemUtils
import com.cefetmg.mmc.mrcpsp.utils.UrlUtils

class ExecutionTypeProcessorImpl implements ExecutionTypeProcessor {

	static final Logger log = Logger.getLogger(MmProcessorImpl.class)
	ResultsProcessor resultsProcessor
	MmProcessor mmProcessor
	ChronoWatch watch
	
	public ExecutionTypeProcessorImpl() {
		super()
		mmProcessor = new MmProcessorImpl()
		resultsProcessor = new ResultsProcessorImpl()		
	}
	
	@Override
	public void execute() {
		PropertyManager.getInstance()		
		def executionType = UrlUtils.getInstance().getExecutionType()
		def hasThread = UrlUtils.getInstance().getHasThread()
		
		removeOldResultFiles()
		SystemUtils.getSystemInformation()
		watch = ChronoWatch.getInstance("MRCPSP Execution").start()
		
		if (StringUtils.equals(executionType, EnumExecutionTypes.ONE_FILE.getName())) {		
			executeOneFile()
		} else if (StringUtils.equals(executionType, EnumExecutionTypes.ONE_FILE_TIMES.getName())) {
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

	@Override
	public void executeOneFile() {		
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)
		
		executeAll(fileName)
		
		printResultAndTimeToFile(watch, fileName)
	}

	@Override
	public void executeOneFileTimes() {
		Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes())
		String fileName = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCE_FILE)
		
		for (int i = 0; i < timesToRun; i++) {
			executeAll(fileName)			
			resultsProcessor.checkLowerMakespan(mmProcessor.project)
		}		
		
		executeLocalSearch()
		
		resultsProcessor.writeLowerMakespanToOneInstance(fileName)
		printResultAndTimeToFile(watch, fileName)
	}

	@Override
	public void executeAllFiles() {		
						
		for (File file: FileUtils.getAllFilesInstances()) {
			LogUtils.setINSTANCE_STATUS("")			
			executeAll(file.getName())	
		}
		
		printResultAndTimeToFileAllInstances(watch)
	}

	@Override
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
        checkGenerateDiagram()
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
        Integer generateDiagram = UrlUtils.getInstance().getGenerateDiagram()

        if (generateDiagram == PropertyConstants.TRUE) {
            mmProcessor.generateDiagram()
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

	@Override
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
