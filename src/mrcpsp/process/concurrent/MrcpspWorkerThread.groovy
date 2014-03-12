package mrcpsp.process.concurrent;

import mrcpsp.process.MmProcessor;
import mrcpsp.process.ResultsProcessor;
import mrcpsp.utils.PropertyConstants;
import mrcpsp.utils.PropertyManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import mrcpsp.model.enums.EnumExecutionTypes;
import mrcpsp.utils.LogUtils;
import mrcpsp.utils.UrlUtils;

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
            MmProcessor mmProcessor = new MmProcessor();
        	mmProcessor.initialSolutionWithGrasp(fileName);

            Integer executeLocalSearchEverySolution = UrlUtils.getInstance().getExecuteLocalSearchEverySolution()
            if (executeLocalSearchEverySolution == PropertyConstants.TRUE) {
                mmProcessor.localSearchDescentUphillMethod()
            }

            mmProcessor.executeWriteResults();
        } catch (Exception e) {
            log.error("Somethin wrong with the threads!");
        	e.printStackTrace();
        }
    }
    
    private void processCommandAllMultipleTimes() {
        try {
        	MmProcessor mmProcessor = new MmProcessor();
        	ResultsProcessor resultsProcessor = new ResultsProcessor();
        	Integer timesToRun = Integer.parseInt(UrlUtils.getInstance().getExecutionTimes());
        	
        	LogUtils.setINSTANCE_STATUS("");
        	
        	for (int i = 0; i < timesToRun; i++) {
                mmProcessor.initialSolutionWithGrasp(fileName);

                Integer executeLocalSearchEverySolution = UrlUtils.getInstance().getExecuteLocalSearchEverySolution()
                if (executeLocalSearchEverySolution == PropertyConstants.TRUE) {
                    mmProcessor.localSearchDescentUphillMethod()
                }

                resultsProcessor.checkLowerMakespan(mmProcessor.project)
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
