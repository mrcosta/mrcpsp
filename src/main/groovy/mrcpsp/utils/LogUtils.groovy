package mrcpsp.utils;

import java.util.List;

import mrcpsp.model.enums.EnumLogUtils;
import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.ResourceAvailabilities;

/**
 * @author mateus
 *
 */
final class LogUtils {
	
	private static String INSTANCE_STATUS = "";
	
	static String generateJobsModeIDListLog(List<Integer> jobs, EnumLogUtils text) {
		String logString = ""
		
		if (!jobs.isEmpty()) {
			logString = "{ J" + jobs[PropertyConstants.INDEX_START].id + ": " + Integer.toString(jobs[PropertyConstants.INDEX_START].mode.id)

            jobs.findAll{ it.id != jobs[PropertyConstants.INDEX_START].id }.each {
                logString+= ", J" + it.id + ": " +  Integer.toString(it.mode.id);
            }
			logString+= " }";
			
			logString = text.listString + logString;
		} else {
			logString = text.emptyListString
		}
		
		return logString;
	}
	
	static String generateJobsIDListLog(List<Integer> jobs, EnumLogUtils text) {
		String logString = "";

        if (!jobs.isEmpty()) {
			logString = "{ " + Integer.toString(jobs[PropertyConstants.INDEX_START].id);

            jobs.findAll{ it.id != jobs[PropertyConstants.INDEX_START].id }.each {
				logString+= ", " + Integer.toString(it.id);
			}
			logString+= " }"
			
			logString = text.listString + logString
		} else {
			logString = text.emptyListString
		}
		
		return logString;
	}
	
	static String generateIDsModeListLog(List<Mode> modes) {
		String logString = "";

        if (!modes.isEmpty()) {
			logString = "{ " + Integer.toString(modes[PropertyConstants.INDEX_START].id);

            modes.findAll{ it.id != modes[PropertyConstants.INDEX_START].id }.each {
				logString+= ", " + Integer.toString(it.id);
			}
			logString+= " }";			
			
		} else {
			logString = "The MODES list is empty: {}";
		}
		
		return logString;
	}
	
	def generateErrorLog(def stackTraceElement) {
		return "\nFile name: " + stackTraceElement[1].fileName
				+ " - Method: " + stackTraceElement[1].methodName
				+ " - Line number: " + stackTraceElement[1].lineNumber
	}

}
