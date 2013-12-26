package com.cefetmg.mmc.mrcpsp.model.enums;

public enum EnumExecutionTypes {

	ONE_FILE("ONE_FILE", "execute just one file", 1),
	ONE_FILE_TIMES("ONE_FILE_TIMES", "execute one file x times", 2),
	ALL("ALL","execute all files of the instance one time", 3),
	ALL_TIMES("ALL_TIMES","execute all files x times", 4);
	
	private String name;
	private String description;
	private Integer value;
	
	private EnumExecutionTypes(String name, String description, Integer value) {
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
