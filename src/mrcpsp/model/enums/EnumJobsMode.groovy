package mrcpsp.model.enums;

/**
 * @author mrcosta
 *
 */
enum EnumJobsMode {
	
	LESS_DURATION("less_duration"),
	MINIMUM_RESOURCES("minimum_resources"),
	MINIMUM_NON_RENEWABLE_RESOURCES("minimum_non_renewable_resources"),
    SHORTER_NEAR_TO_LOWER_NON_RENEWABLE_RESOURCES("shorter_near_to_lower_nr")
	
	private final String description;
	
	EnumJobsMode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
