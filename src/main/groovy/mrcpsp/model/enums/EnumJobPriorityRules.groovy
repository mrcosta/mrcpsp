package mrcpsp.model.enums;

enum EnumJobPriorityRules {
	
	BY_ID("BY_ID", "by the job ID", 1),
	MAX_NIS("MAX_NIS", "by max nis - Maximum Number of Immediate Successors", 2),
	MAX_CAN("MAX_CAN","by max can - Maximum Number of Subsequent Candidates", 3),
	MAX_NISCAN("MAX_NISCAN","by MAX_NIS and MAX_CAN", 4),
	BY_END_TIME("BY_END_TIME","by end time, used to get the start and finish time of the jobs", 5);
	
	String name;
	String description;
	Integer value;
	
	private EnumJobPriorityRules(String name, String description, Integer value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}
}
