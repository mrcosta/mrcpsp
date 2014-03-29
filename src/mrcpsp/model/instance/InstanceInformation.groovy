package mrcpsp.model.instance;

/**
 * @author mrcosta
 *
 */
class InstanceInformation {
	
	String initialValueRandomGenerator;
	Integer jobsAmount;
	Integer horizon;
	Integer renewableAmount;
	Integer nonRenewableAmount;
	
	InstanceInformation() {
		this.initialValueRandomGenerator = "";
		this.jobsAmount = 0;
		this.horizon = 0;
		this.renewableAmount = 0;
		this.nonRenewableAmount = 0;
	}
}
