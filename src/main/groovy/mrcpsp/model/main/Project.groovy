package mrcpsp.model.main;

class Project {

    Integer jobsAmount
	
	ResourceAvailabilities resourceAvailabilities

	List<Integer> jobsId
    List<Integer> staggeredJobsId

    List<Job> jobs

    Map originalModes
    Map modes
    Map times

    Integer makespan
    Double averageMakespan
	
	String fileName
    String totalTimeSolutionFormated

	public Project() {
        averageMakespan = 0.0
	}

    Project setProject(Project baseProject) {
        this.makespan = baseProject.makespan
        this.fileName = baseProject.fileName

        this.staggeredJobsId.clear()
        this.staggeredJobsId.addAll(baseProject.staggeredJobsId)

        this.modes.clear()
        this.modes.putAll(baseProject.modes)

        this.times.clear()
        this.times.putAll(baseProject.times)
    }

}
