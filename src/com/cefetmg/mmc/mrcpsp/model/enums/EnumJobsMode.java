package com.cefetmg.mmc.mrcpsp.model.enums;

/**
 * @author mrcosta
 *
 */
public enum EnumJobsMode {
	
	LESS_DURATION("less_duration"),
	MINIMUM_RESOURCES("minimum_resources"),
	MINIMUM_NON_RENEWABLE_RESOURCES("minimum_non_renewable_resources");
	
	private String description;
	
	private EnumJobsMode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
