package com.cefetmg.mmc.mrcpsp.utils;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.enums.EnumLogUtils;
import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.model.main.Mode;
import com.cefetmg.mmc.mrcpsp.model.main.ResourceAvailabilities;

/**
 * @author mateus
 *
 */
public final class LogUtils {
	
	private static String INSTANCE_STATUS = "";
	
	public static String generateJobsModeIDListLog(List<Job> jobs, EnumLogUtils text) {		
		String logString = "";
		
		if (jobs.size() > 0) {
			logString = "{ " + Integer.toString(jobs.get(PropertyConstants.INDEX_START).getMode().getId());			
			
			for(int i = 1; i < jobs.size(); i++) {
				logString+= ", " + Integer.toString(jobs.get(i).getMode().getId());
			}
			logString+= " }";
			
			logString = text.getListString() + logString;
		} else {
			logString = text.getEmptyListString();
		}
		
		return logString;
	}
	
	public static String generateJobsIDListLog(List<Job> jobs, EnumLogUtils text) {		
		String logString = "";
		
		if (jobs.size() > 0) {
			logString = "{ " + Integer.toString(jobs.get(PropertyConstants.INDEX_START).getId());			
			
			for(int i = 1; i < jobs.size(); i++) {
				logString+= ", " + Integer.toString(jobs.get(i).getId());
			}
			logString+= " }";
			
			logString = text.getListString() + logString;
		} else {
			logString = text.getEmptyListString();
		}
		
		return logString;
	}
	
	public static String generateIDsModeListLog(List<Mode> modes) {
		String logString = "";
		
		if (!modes.isEmpty()) {
			logString = "{ " + Integer.toString(modes.get(PropertyConstants.INDEX_START).getId());			
			
			for(int i = 1; i < modes.size(); i++) {
				logString+= ", " + Integer.toString(modes.get(i).getId());
			}
			logString+= " }";			
			
		} else {
			logString = "The MODES list is empty: {}";
		}
		
		return logString;
	}
	
	public static String generateJobRenewableResourcesAndTimeLog(ResourceAvailabilities ra, Job job) {
		return "JOB " + job.getId() 
				+ " - Duration: " + job.getMode().getDuration()
				+ " - Renew: " + job.getMode().getRenewable()
				+ " - Total: " + ra.getRenewableInitialAmount() 
				+ " - Usage: " + ra.getRenewableConsumedAmount()
				+ " - Remaining: " + ra.getRemainingRenewableAmount()
				+ " - {Start, End} = {" + job.getStartTime() + ", " + job.getEndTime() + "}";
	}
	
	public static String generateErrorLog(StackTraceElement stackTraceElement[]) {
		return "\nFile name: " + stackTraceElement[1].getFileName() 
				+ " - Method: " + stackTraceElement[1].getMethodName() 
				+ " - Line number: " + stackTraceElement[1].getLineNumber();				
	}

	public static String getINSTANCE_STATUS() {
		return INSTANCE_STATUS;
	}

	public static void setINSTANCE_STATUS(String iNSTANCE_STATUS) {
		INSTANCE_STATUS = iNSTANCE_STATUS;
	}

}
