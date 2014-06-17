package mrcpsp.model.main;

class Project {

    Integer jobsAmount
	
	ResourceAvailabilities resourceAvailabilities

	List<Integer> jobsId
    List<Integer> staggeredJobsId

    List<Job> jobs

    Map modes
    Map staggeredModes

    Map times

    List<Integer> start
    List<Integer> end

    Integer makespan
    Double averageMakespan
	
	String fileName
	String instanceResultStatus
    String totalTimeSolutionFormated

	public Project() {
		instanceResultStatus = ""

        averageMakespan = 0.0
	}

    Project setProjectUsingAnother(Project project, Project baseProject) {
        //project.staggeredJobs =
        //project.staggeredModes =
    }

}
