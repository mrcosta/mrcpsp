package mrcpsp.process.concurrent;

import mrcpsp.process.ResultsProcessor;
import mrcpsp.utils.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.*;


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
       ResultsProcessor resultsProcessor = new ResultsProcessor();
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
