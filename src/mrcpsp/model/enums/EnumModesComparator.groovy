package mrcpsp.model.enums;

enum EnumModesComparator {
	
	MC_AMOUNT_RENEWABLE("by the amount renewable resources", 1),
	MC_AMOUNT_NON_RENEWABLE("by the amount non renewable resources", 2),
	MC_DURATION("by the mode duration", 3),
	MC_SUM_RESOURCES("by the sum of the all resources", 4);
	
	String description;
	Integer value;
	
	private EnumModesComparator(String description, Integer value) {
		this.description = description;
		this.value = value;
	}
}
