package mrcpsp.model.enums;

import java.io.Serializable;

enum EnumLocalSearch implements Serializable {

	LNRC("LNRC", "lower non renewable consumption", 1),
	BRNRC("BRNRC", "best ratio non renewable comsumption", 2),
	BRRC("BRRC", "best ratio renewable comsumption", 3),
	BRC("LNRC", "best ratio comsumption", 4),
    LNRCCP("LNRCCP", "lower non renewable consumption critical path", 5);
	
	private String name;
	private String description;
	private Integer value;
	
	EnumLocalSearch(String name, String description, Integer value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getValue() {
		return value;
	}
	
}
