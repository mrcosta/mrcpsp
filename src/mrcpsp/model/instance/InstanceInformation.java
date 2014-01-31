package mrcpsp.model.instance;

/**
 * @author mrcosta
 *
 */
public class InstanceInformation {
	
	private String initialValueRandomGenerator;
	private Integer jobsAmount;
	private Integer horizon;
	private Integer renewableAmount;
	private Integer nonRenewableAmount;
	
	public InstanceInformation() {
		this.initialValueRandomGenerator = "";
		this.jobsAmount = 0;
		this.horizon = 0;
		this.renewableAmount = 0;
		this.nonRenewableAmount = 0;
	}

	public String getInitialValueRandomGenerator() {
		return initialValueRandomGenerator;
	}

	public void setInitialValueRandomGenerator(String initialValueRandomGenerator) {
		this.initialValueRandomGenerator = initialValueRandomGenerator;
	}

	public Integer getJobsAmount() {
		return jobsAmount;
	}

	public void setJobsAmount(Integer jobsAmount) {
		this.jobsAmount = jobsAmount;
	}

	public Integer getHorizon() {
		return horizon;
	}

	public void setHorizon(Integer horizon) {
		this.horizon = horizon;
	}

	public Integer getRenewableAmount() {
		return renewableAmount;
	}

	public void setRenewableAmount(Integer renewableAmount) {
		this.renewableAmount = renewableAmount;
	}

	public Integer getNonRenewableAmount() {
		return nonRenewableAmount;
	}

	public void setNonRenewableAmount(Integer nonRenewableAmount) {
		this.nonRenewableAmount = nonRenewableAmount;
	}

}
