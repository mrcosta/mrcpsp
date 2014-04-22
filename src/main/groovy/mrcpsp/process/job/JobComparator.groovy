package mrcpsp.process.job;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import mrcpsp.model.enums.EnumJobPriorityRules;
import mrcpsp.model.main.Job;

class JobComparator implements Comparator<Job> {

	private static final Logger log = Logger.getLogger(JobComparator.class);
	
	EnumJobPriorityRules comparatorType;
	
	@Override
	public int compare(Job job, Job jobToCompare) {
		Integer result = 0
		
		switch (this.comparatorType.name) {
			case EnumJobPriorityRules.BY_ID.name:
    			result = job.id.compareTo(jobToCompare.id)
    			break
			case EnumJobPriorityRules.MAX_NIS.name:
        		result = job.runningJobInformation.nisAmount.compareTo(jobToCompare.runningJobInformation.nisAmount)
        		break
        	case EnumJobPriorityRules.MAX_CAN.name:
        		result = job.runningJobInformation.canAmount.compareTo(jobToCompare.runningJobInformation.canAmount)
        		break
        	case EnumJobPriorityRules.MAX_NISCAN.name:
        		result = job.runningJobInformation.niscanAmount.compareTo(jobToCompare.runningJobInformation.niscanAmount)
        		break
        	case EnumJobPriorityRules.BY_END_TIME.name:
    			result = job.endTime.compareTo(jobToCompare.endTime)
    			break
        	default:  
        		log.log(Level.ERROR, "Invalid Option to order: " + JobComparator.class)
        		throw new RuntimeException("Invalid Option to order: " + JobComparator.class)
		}
		
		return result
	}
}
