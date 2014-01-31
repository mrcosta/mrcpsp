package mrcpsp.model.main;

import java.util.List;

/**
 * @author mrcosta
 *
 */
public class Mode {
	
	private Integer id;
	private Integer duration;	
	
	/** The amount of each renewable resource */	
	private List<Integer> renewable;
	
	/** The amount of each non renewable resource */
	private List<Integer> nonRenewable;
	
	/** The sum of all renewable resources */
	private Integer amountRenewable;
	
	/** The sum of all non renewable resources */	
	private Integer amountNonRenewable;	
	
	/** The sum of all resources */	
	private Integer sumResources;	
		
	public Mode() {	
	}	
	
	public Mode(Integer id, Integer duration, List<Integer> renewable,
			List<Integer> nonRenewable) {
		super();		
		this.id = id;
		this.duration = duration;
		this.renewable = renewable;		
		this.nonRenewable = nonRenewable;		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<Integer> getRenewable() {
		return renewable;
	}

	public void setRenewable(List<Integer> renewable) {
		this.renewable = renewable;
	}

	public List<Integer> getNonRenewable() {
		return nonRenewable;
	}

	public void setNonRenewable(List<Integer> nonRenewable) {
		this.nonRenewable = nonRenewable;
	}

	public Integer getAmountRenewable() {
		return amountRenewable;
	}

	public void setAmountRenewable(Integer amountRenewable) {
		this.amountRenewable = amountRenewable;
	}

	public Integer getAmountNonRenewable() {
		return amountNonRenewable;
	}

	public void setAmountNonRenewable(Integer amountNonRenewable) {
		this.amountNonRenewable = amountNonRenewable;
	}

	public Integer getSumResources() {
		return sumResources;
	}

	public void setSumResources(Integer sumResources) {
		this.sumResources = sumResources;
	}	
	
}
