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

    List<Job> scheduledJobs

	public ResourceAvailabilities() {
		renewableInitialAmount = []
		nonRenewableInitialAmount = []
		
		renewableConsumedAmount = []
		nonRenewableConsumedAmount = []
		
		remainingRenewableAmount = []
		remainingNonRenewableAmount = []

        scheduledJobs = []
	}

    def resetRenewableResources() {
        this.renewableConsumedAmount.each {
            it = 0
        }

        def count = 0
        this.remainingRenewableAmount.each {
            it = this.renewableInitialAmount[count]
            count++
        }

        this.scheduledJobs = []
    }

	@Override
	public String toString() {
		return """ResourceAvailabilities [renewableInitialAmount: $renewableInitialAmount, nonRenewableInitialAmount: $nonRenewableInitialAmount, renewableConsumedAmount: $renewableConsumedAmount, nonRenewableConsumedAmount: $nonRenewableConsumedAmount] """
	}
	
	public String toStringRenewable() {
		 return	"""Total: $renewableInitialAmount - Usage: $renewableConsumedAmount - Remaining: $remainingRenewableAmount"""
	}

    public String toStringNonRenewable() {
        return	"""Total: $nonRenewableInitialAmount - Usage: $nonRenewableConsumedAmount - Remaining: $remainingNonRenewableAmount"""
    }

}
