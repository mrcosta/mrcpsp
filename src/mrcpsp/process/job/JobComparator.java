package mrcpsp.process.job;

import java.util.Comparator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import mrcpsp.model.enums.EnumJobPriorityRules;
import mrcpsp.model.main.Job;

public class JobComparator implements Comparator<Job> {

	private static final Logger log = Logger.getLogger(JobComparator.class);
	
	private EnumJobPriorityRules comparatorType;
	
	@Override
	public int compare(Job job, Job jobToCompare) {
		Integer result = 0;
		
		switch (this.comparatorType) {  
			case BY_ID :  
    			result = job.getId().compareTo(jobToCompare.getId());  
    			break;  
			case MAX_NIS :  
        		result = job.getRunningJobInformation().getNisAmount().
        				 compareTo(jobToCompare.getRunningJobInformation().getNisAmount());  
        		break;  
        	case MAX_CAN:  
        		result = job.getRunningJobInformation().getCanAmount().
       				 	 compareTo(jobToCompare.getRunningJobInformation().getCanAmount());    
        		break;  
        	case MAX_NISCAN:  
        		result = job.getRunningJobInformation().getNiscanAmount().
       				 	 compareTo(jobToCompare.getRunningJobInformation().getNiscanAmount());    
        		break;
        	case BY_END_TIME :  
    			result = job.getEndTime().compareTo(jobToCompare.getEndTime());  
    			break;  
        	default:  
        		log.log(Level.ERROR, "Invalid Option to order: " + JobComparator.class);
        		throw new RuntimeException("Invalid Option to order: " + JobComparator.class);  
		}
		
		return result;
		
	}

	public void setComparatorType(EnumJobPriorityRules comparatorType) {
		this.comparatorType = comparatorType;
	}	
}
