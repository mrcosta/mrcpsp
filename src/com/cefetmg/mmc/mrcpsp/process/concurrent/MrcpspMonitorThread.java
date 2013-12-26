package com.cefetmg.mmc.mrcpsp.process.concurrent;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

public class MrcpspMonitorThread implements Runnable {
	
	private static final Logger log = Logger.getLogger(MrcpspMonitorThread.class);	
	
	private ThreadPoolExecutor executor;

	private static final int MONITOR_PERIOD = 3000;

    private boolean run=true;

    public MrcpspMonitorThread(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public void shutdown(){
        this.run=false;
    }

    @Override
    public void run() {
        while(run){
                System.out.println(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                        this.executor.getPoolSize(),
                        this.executor.getCorePoolSize(),
                        this.executor.getActiveCount(),
                        this.executor.getCompletedTaskCount(),
                        this.executor.getTaskCount(),
                        this.executor.isShutdown(),
                        this.executor.isTerminated()));
                try {
                    Thread.sleep(MONITOR_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
    
    public boolean isWorkDone() {
    	log.info("EXECUTOR: " + this.executor.getActiveCount() + " - COMPLETED: " + this.executor.getCompletedTaskCount() + " - TOTAL: " + this.executor.getTaskCount());
        return this.executor.getActiveCount() == 0;
    }

}
