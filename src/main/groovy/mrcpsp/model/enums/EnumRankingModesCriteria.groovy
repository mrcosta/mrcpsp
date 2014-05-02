package mrcpsp.model.enums;

enum EnumRankingModesCriteria {

    RK_ALL("RK_ALL", "all criteria", 1),
    RK_DRA("RK_DRA", "duration, renewable amount", 2)

	String name
    String description;
	Integer value;

	private EnumRankingModesCriteria(String name, String description, Integer value) {
		this.name = name
        this.description = description;
		this.value = value;
	}
}
