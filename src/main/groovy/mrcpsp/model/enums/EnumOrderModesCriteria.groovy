package mrcpsp.model.enums;

enum EnumOrderModesCriteria {

	PER_DURATION("D", "by the mode duration", 1),
	PER_AMOUNT("A", "by the total amount consumed", 2),
    PER_NR_AMOUNT("NRA", "by the non-renewable total amount consumed", 3),
    PER_R_AMOUNT("RA", "by the renewable total amount consumed", 4),
    PER_FIRST_NR_AMOUNT("FNRA", "by the first non-renewable resource total amount consumed", 5),
    PER_SECOND_NR_AMOUNT("SNRA", "by the second non-renewable resource total amount consumed", 6),
    PER_FIRST_R_AMOUNT("FRA", "by the first renewable resource total amount consumed", 7),
    PER_SECOND_R_AMOUNT("SRA", "by the second renewable resource total amount consumed", 8)

	String name
    String description;
	Integer value;

	private EnumOrderModesCriteria(String name, String description, Integer value) {
		this.name = name
        this.description = description;
		this.value = value;
	}
}
