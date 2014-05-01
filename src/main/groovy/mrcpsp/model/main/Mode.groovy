package mrcpsp.model.main
/**
 * @author mrcosta
 *
 */
class Mode {
	
	Integer id
    Integer jobId

	Integer duration
	
	/** The amount of each renewable resource */
	List<Integer> renewable;
	
	/** The amount of each non renewable resource */
	List<Integer> nonRenewable;
	
	/** The sum of all renewable resources */
	Integer amountRenewable;
	
	/** The sum of all non renewable resources */	
	Integer amountNonRenewable;
	
	/** The sum of all resources */	
	Integer sumResources;
		
	public Mode() {	
	}	
	
	public Mode(Integer id, Integer duration, List<Integer> renewable, List<Integer> nonRenewable) {
		super();		
		this.id = id;
		this.duration = duration;
		this.renewable = renewable;		
		this.nonRenewable = nonRenewable;		
	}
	
}
