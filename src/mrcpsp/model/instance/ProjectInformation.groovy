package mrcpsp.model.instance;

/**
 * @author mrcosta
 *
 */
class ProjectInformation {
	
	/**
	 * whithout supersource/sink
	 */
	Integer jobsAmount;
	Integer releaseDate;
	Integer dueDate;
	Integer tardinessCost;
		
	ProjectInformation() {
		this.jobsAmount = 0;
		this.releaseDate = 0;
		this.dueDate = 0;
		this.tardinessCost = 0;
	}
}
