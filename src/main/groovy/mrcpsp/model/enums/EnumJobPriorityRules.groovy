package mrcpsp.model.enums;

enum EnumJobPriorityRules {
	
	BY_ID("BY_ID", "by the job ID", 1),
	MAX_NIS("MAX_NIS", "by max nis - Maximum Number of Immediate Successors", 2),
    MIN_SLK("MIN_SLK", "by min slk - Minimum Slack", 3),
    BY_END_TIME("BY_END_TIME","by end time, used to get the start and finish time of the jobs", 4),
    BY_SUM_POSITIONS("BY_SUM_POSITIONS","by the sum of positions for all criteria", 5);
	
	String name;
	String description;
	Integer value;
	
	private EnumJobPriorityRules(String name, String description, Integer value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}
}
