package com.cefetmg.mmc.mrcpsp.model.instance;

/**
 * @author mrcosta
 *
 */
public class ProjectInformation {
	
	/**
	 * whithout supersource/sink
	 */
	private Integer jobsAmount;
	private Integer releaseDate;
	private Integer dueDate;
	private Integer tardinessCost;
		
	public ProjectInformation() {
		this.jobsAmount = 0;
		this.releaseDate = 0;
		this.dueDate = 0;
		this.tardinessCost = 0;
	}

	public Integer getJobsAmount() {
		return jobsAmount;
	}

	public void setJobsAmount(Integer jobsAmount) {
		this.jobsAmount = jobsAmount;
	}

	public Integer getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Integer releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Integer getDueDate() {
		return dueDate;
	}

	public void setDueDate(Integer dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getTardinessCost() {
		return tardinessCost;
	}

	public void setTardinessCost(Integer tardinessCost) {
		this.tardinessCost = tardinessCost;
	}

}
