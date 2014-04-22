package mrcpsp.model.main;

import java.util.List;

import mrcpsp.model.instance.InstanceInformation;
import mrcpsp.model.instance.ProjectInformation;

/**
 * @author mrcosta
 *
 */
class Project {
	
	InstanceInformation instanceInformation
	ProjectInformation projectInformation
	ResourceAvailabilities resourceAvailabilities
	List<Job> jobs
	List<Job> staggeredJobs
    List<Job> criticalPath
	Integer makespan
    Integer lowerBound
	
	String fileName
	String instanceResultStatus
    String totalTimeSolutionFormated

	public Project() {
		instanceResultStatus = ""
	}

}
