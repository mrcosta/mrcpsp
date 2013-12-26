package com.cefetmg.mmc.mrcpsp.process.concurrent;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cefetmg.mmc.mrcpsp.process.ResultsProcessor;
import com.cefetmg.mmc.mrcpsp.process.impl.ResultsProcessorImpl;
import com.cefetmg.mmc.mrcpsp.utils.ChronoWatch;
import com.cefetmg.mmc.mrcpsp.utils.FileUtils;
import com.cefetmg.mmc.mrcpsp.utils.PropertyConstants;
import com.cefetmg.mmc.mrcpsp.utils.PropertyManager;
import com.cefetmg.mmc.mrcpsp.utils.SystemUtils;


public class MrcpspWorkerPool {
	
	private static final Logger log = Logger.getLogger(MrcpspWorkerPool.class);
	
	private MrcpspMonitorThread monitor;
	private BlockingQueue<Runnable> worksQueue;
	private RejectedExecutionHandlerImpl rejectionHandler;
	private ThreadFactory threadFactory;
	private ThreadPoolExecutor tpe;
	private Thread monitorThread;
	
	private Integer poolSize;
	private Integer timeWait;
	private Integer totalFile;
	
	public void executeAllFilesConcurrent() throws InterruptedException {   
		ChronoWatch watch = ChronoWatch.getInstance("MRCPSP Execution").start();		
		SystemUtils.getSystemInformation();
		
		poolSize = Integer.parseInt(PropertyManager.getInstance().getProperty(PropertyConstants.CONCURRENT_POOLSIZE));
		timeWait = 10;
		totalFile = FileUtils.getAllFilesInstances().size();
		worksQueue = new ArrayBlockingQueue<Runnable>(totalFile);
		
        rejectionHandler = new RejectedExecutionHandlerImpl();        
        threadFactory = Executors.defaultThreadFactory();        
        tpe = new ThreadPoolExecutor(poolSize, poolSize, timeWait, TimeUnit.SECONDS, worksQueue, threadFactory, rejectionHandler);
        
        monitor = new MrcpspMonitorThread(tpe);
        monitorThread = new Thread(monitor);
        monitorThread.start();

        for (File file: FileUtils.getAllFilesInstances()) {
			tpe.execute(new MrcpspWorkerThread(file.getName()));	
		}
       
        // wait for the results
        waitForDone();

       // finish
       String timeString = watch.time();
       ResultsProcessor resultsProcessor = new ResultsProcessorImpl();
       resultsProcessor.writeRunningTimeToResultFileAllInstances(timeString);
       log.info(timeString);          
    }
	
	private void waitForDone() {        
		while (monitor != null && !monitor.isWorkDone()) {
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
            log.info("waiting for the processing . . .");
        }
		
        monitor.shutdown();
        tpe.shutdown();
    }
}
