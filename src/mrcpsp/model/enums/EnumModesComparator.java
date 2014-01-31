package mrcpsp.model.enums;

public enum EnumModesComparator {
	
	MC_AMOUNT_RENEWABLE("by the amount renewable resources", 1),
	MC_AMOUNT_NON_RENEWABLE("by the amount non renewable resources", 2),
	MC_DURATION("by the mode duration", 3),
	MC_SUM_RESOURCES("by the sum of the all resources", 4);
	
	private String description;
	private Integer value;
	
	private EnumModesComparator(String description, Integer value) {
		this.description = description;
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
