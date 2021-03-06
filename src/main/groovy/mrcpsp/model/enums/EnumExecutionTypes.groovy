package mrcpsp.model.enums;

public enum EnumExecutionTypes {

	ONE_FILE("ONE_FILE", "execute just one file", 1),
	ONE_FILE_TIMES("ONE_FILE_TIMES", "execute one file x times", 2),
	ALL("ALL","execute all files of the instance one time", 3),
	ALL_TIMES("ALL_TIMES","execute all files x times", 4),
    READ_PSPLIB_INSTANCE("READ_PSPLIB_INSTANCE", "read the results from the psplib file", 5),
    ANALYTICS("ANALYTICS", "compare two results file", 6);
	
	String name
	String description
	Integer value
	
	private EnumExecutionTypes(String name, String description, Integer value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}
}
