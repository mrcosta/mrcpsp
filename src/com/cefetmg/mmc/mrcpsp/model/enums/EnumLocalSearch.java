package com.cefetmg.mmc.mrcpsp.model.enums;

import java.io.Serializable;

public enum EnumLocalSearch implements Serializable {

	LNRC("LNRC", "lower non renewable consumption", 1),
	BRNRC("BRNRC", "best ratio non renewable comsumption", 2),
	BRRC("BRRC", "best ratio renewable comsumption", 3),
	BRC("LNRC", "best ratio comsumption", 4);
	
	private String name;
	private String description;
	private Integer value;
	
	private EnumLocalSearch(String name, String description, Integer value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
