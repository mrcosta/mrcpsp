package mrcpsp.model.enums

enum EnumLocalSearch implements Serializable {

	LNRC("LNRC", "lower non renewable consumption", 1),
	BRNRC("BRNRC", "best ratio non renewable comsumption", 2),
	BRRC("BRRC", "best ratio renewable comsumption", 3),
	BRC("LNRC", "best ratio comsumption", 4),
    LNRCCP("LNRCCP", "lower non renewable consumption critical path", 5),
    BSFM("BSFM", "jobs block with shortest feasible mode", 5),
    BMS("BMS", "best mode to schedule", 6),
    SIMULATED_ANNEALING("SA", "simulated annealing", 7),
    CRITICAL_PATH_PRIORITY("CRITICAL_PATH_PRIORITY", "critical path priority", 8)
	
	String name;
	String description;
	Integer value;
	
	EnumLocalSearch(String name, String description, Integer value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}
}
