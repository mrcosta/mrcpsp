package mrcpsp.model.enums;

public enum EnumLogUtils {
	
	REMAINING_JOBS("REMAINING_JOBS", "The REMAINING JOBS list has this index: ", "The REMAINING JOBS list is empty: {}", 1),
	RCL_JOBS("RCL_JOBS", "The RCL JOBS list has this index: ", "The RCL JOBS list is empty: {}", 2),	
	JOBS_ADD_RCL_LIST("JOBS_ADD_RCL_LIST", "The FOLLOW JOBS were added in the RCL JOBS list: ", "The RCL JOBS list is empty: {}", 3),
	ELIGIBLE_JOBS("ELIGIBLE_JOBS", "The ELIGIBLE JOBS list has this index: ", "The ELIGIBLE JOBS list is empty: {}", 4),
	STAGGERED_JOBS("STAGGERED_JOBS", "The STAGGERED JOBS jobs list has this index: ", "The STAGGERED JOBS jobs list is empty: {}", 5),
	LIST_JOBS("LIST_JOBS", "The JOBS list has this index: ", "The JOBS list is empty: {}", 6),
	REMAINING_JOBS_RENEWABLE("REMAINING_JOBS_RENEWABLE", "The remaining jobs list using renewable resources has this index: ", "The Remaining jobs using renewable resources list is empty: {}", 7),
	PREDECESSORS_JOBS("PREDECESSORS_JOBS", "The PREDECESSORS JOBS list has this index: ", "The PREDECESSORS JOBS list is empty: {}", 8),
	JOBS_MODE_LIST("JOBS_MODE_LIST", "The JOBS MODES list has this index: ", "The JOBS MODES list is empty: {}", 9),
    CRITICAL_PATH_JOBS("CRITICAL_PATH_JOBS", "The CRITICAL PATH have the given jobs: ", "The CRITICAL PATH list is empty: {}", 10);
	
	private String name;
	private String listString;
	private String emptyListString;
	private Integer value;

	private EnumLogUtils(String name, String listString, String emptyListString, Integer value) {
		this.name = name;
		this.listString = listString;
		this.emptyListString = emptyListString;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public String getListString() {
		return listString;
	}

	public void setListString(String listString) {
		this.listString = listString;
	}

	public String getEmptyListString() {
		return emptyListString;
	}

	public void setEmptyListString(String emptyListString) {
		this.emptyListString = emptyListString;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
