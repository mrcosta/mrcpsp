package mrcpsp.model.enums;

enum EnumModesComparator {
	
	MC_AMOUNT_RENEWABLE("MC_AMOUNT_RENEWABLE", "by the amount renewable resources", 1),
	MC_AMOUNT_NON_RENEWABLE("MC_AMOUNT_NON_RENEWABLE", "by the amount non renewable resources", 2),
	MC_DURATION("MC_DURATION", "by the mode duration", 3),
	MC_SUM_RESOURCES("MC_SUM_RESOURCES", "by the sum of the all resources", 4),
    MC_AMOUNT_FIRST_NR("MC_AMOUNT_FIRST_NR", "by the first non-renewable resource amount", 5),
    MC_AMOUNT_SECOND_NR("MC_AMOUNT_SECOND_NR", "by the second non-renewable resource amount", 6),
    MC_AMOUNT_FIRST_R("MC_AMOUNT_FIRST_R", "by the first renewable resource amount", 7),
    MC_AMOUNT_SECOND_R("MC_AMOUNT_SECOND_R", "by the second renewable resource amount", 8),
    MC_SUM_RANKING_POSITIONS("MC_SUM_RANKING_POSITIONS", "by the sum of the ranking positions", 9),
    MC_ID("MC_ID", "by mode id", 9)
	
	String name
    String description;
	Integer value;
	
	private EnumModesComparator(String name, String description, Integer value) {
		this.name = name
        this.description = description;
		this.value = value;
	}
}
