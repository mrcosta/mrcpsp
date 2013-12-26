package com.cefetmg.mmc.mrcpsp.process.concurrent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cefetmg.mmc.mrcpsp.model.enums.EnumExecutionTypes;
import com.cefetmg.mmc.mrcpsp.process.impl.MmProcessorImpl;
import com.cefetmg.mmc.mrcpsp.process.impl.ResultsProcessorImpl;
import com.cefetmg.mmc.mrcpsp.utils.LogUtils;
import com.cefetmg.mmc.mrcpsp.utils.UrlUtils;

public class MrcpspWorkerThread implements Runnable {
	
	private static final Logger log = Logger.getLogger(MrcpspWorkerThread.class);
	
	private String fileName;

	public MrcpspWorkerThread(String fileName){        
    	this.fileName = fileName;
    }

    @Override
    public void run() {
        log.info(Thread.currentThread().getName()+ "====== File: " + fileName + " started.");
        String executionType = UrlUtils.getInstance().getExecutionType();
        
        if (StringUtils.equals(executionType, EnumExecutionTypes.ALL_TIMES.getName())) {
        	processCommandAllMultipleTimes();
        } else {
        	processCommand();
        }
        
        log.info(Thread.currentThread().getName()+ "====== File: " + fileName + " finished.");
    }

    private void processCommand() {
        try {
        	MmProcessorImpl mmProcessor = new MmProcessorImpl();
        	mmProcessor.initialSolutionWithGrasp(fileName);
        } catch (Exception e) {
            log.error("Somethin wrong with the threads!");
        	e.printStackTrace();
        }
    }
    
    private void processCommandAllMultipleTimes() {
        try {
        	MmProcessorImpl mmProcessor = new MmProcessorImpl();
        	ResultsProcessorImpl resultsProcessor = new ResultsProcessorImpl();	
        	Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes());
        	
        	LogUtils.setINSTANCE_STATUS("");
        	
        	for (int i = 0; i < timesToRun; i++) {
				resultsProcessor.checkLowerMakespan(mmProcessor.initialSolutionWithGrasp(fileName));
			}	
			
			resultsProcessor.writeLowerMakespanToAllInstances(fileName);
			resultsProcessor.setLowerMakespan(null);
        } catch (Exception e) {
            log.error("Somethin wrong with the threads!");
        	e.printStackTrace();
        }
    }
    

    @Override
    public String toString(){
        return this.fileName;
    }

}
