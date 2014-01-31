package mrcpsp.model.main
/**
 * @author mrcosta
 *
 */
class ResourceAvailabilities {
	
	List<Integer> renewableInitialAmount
	List<Integer> nonRenewableInitialAmount
	
	List<Integer> renewableConsumedAmount
	List<Integer> nonRenewableConsumedAmount
	
	List<Integer> remainingRenewableAmount
	List<Integer> remainingNonRenewableAmount
	
	List<Job> jobsUsingRenewableResources
	
	public ResourceAvailabilities() {
		renewableInitialAmount = []
		nonRenewableInitialAmount = []
		
		renewableConsumedAmount = []
		nonRenewableConsumedAmount = []
		
		remainingRenewableAmount = []
		remainingNonRenewableAmount = []
		
		jobsUsingRenewableResources = []
	}	

	@Override
	public String toString() {
		return "ResourceAvailabilities [renewableInitialAmount:" + renewableInitialAmount + ", nonRenewableInitialAmount: " + nonRenewableInitialAmount
								   + ", renewableConsumedAmount: " + renewableConsumedAmount  + ", nonRenewableConsumedAmount: " + nonRenewableConsumedAmount
	}
	
	public String toStringRenewable() {
		 return	"Total: " + renewableInitialAmount 
				+ " - Usage: " + renewableConsumedAmount
				+ " - Remaining: " + remainingRenewableAmount
	}

}
