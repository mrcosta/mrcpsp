package mrcpsp.process.job

import mrcpsp.model.enums.EnumJobPriorityRules
import mrcpsp.model.main.Job
import org.apache.log4j.Level
import org.apache.log4j.Logger

class JobComparator implements Comparator<Job> {

	private static final Logger log = Logger.getLogger(JobComparator.class)
	
	EnumJobPriorityRules comparatorType;
	
	@Override
	public int compare(Job job, Job jobToCompare) {
		Integer result = 0
		
		switch (this.comparatorType.name) {
			case EnumJobPriorityRules.BY_ID.name:
    			result = job.id.compareTo(jobToCompare.id)
    			break
        	case EnumJobPriorityRules.BY_END_TIME.name:
    			result = job.endTime.compareTo(jobToCompare.endTime)
    			break
            case EnumJobPriorityRules.TOTAL_SUCCESSORS.name:
                result = job.totalSuccessors.compareTo(jobToCompare.totalSuccessors)
                break
        	default:  
        		log.log(Level.ERROR, "Invalid Option to order: " + JobComparator.class)
        		throw new RuntimeException("Invalid Option to order: " + JobComparator.class)
		}
		
		return result
	}
}
