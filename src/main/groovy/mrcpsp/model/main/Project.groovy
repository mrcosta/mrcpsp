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
    Map<Integer, Integer> staggeredJobsModesId
    Map<Integer, Integer> startJobTimes
    Map<Integer, Integer> finishJobTimes
    List<Job> criticalPath

    Integer makespan
    Double averageMakespan
    Integer lowerBound
	
	String fileName
	String instanceResultStatus
    String totalTimeSolutionFormated

	public Project() {
		instanceResultStatus = ""

        averageMakespan = 0.0

        startJobTimes = [:]
        finishJobTimes = [:]
        staggeredJobsModesId = [:]
	}

}
