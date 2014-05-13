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

    List<Integer> originalNonRenewableConsumedAmount
    List<Integer> originalNonRenewableRemainingAmount

	public ResourceAvailabilities() {
		renewableInitialAmount = []
		nonRenewableInitialAmount = []
		
		renewableConsumedAmount = []
		nonRenewableConsumedAmount = []
		
		remainingRenewableAmount = []
		remainingNonRenewableAmount = []

        originalNonRenewableConsumedAmount = []
        originalNonRenewableRemainingAmount = []

        scheduledJobs = []
	}

    def resetRenewableResources() {
        this.renewableConsumedAmount.each { it = 0 }

        this.remainingRenewableAmount.clear()
        this.remainingRenewableAmount.addAll(this.renewableInitialAmount)

        this.scheduledJobs = []
    }

    def setOriginalNonRenewableConsumedAndRemainingAmount() {
       this.originalNonRenewableConsumedAmount.clear()
       this.originalNonRenewableConsumedAmount.addAll(this.nonRenewableConsumedAmount)

        this.originalNonRenewableRemainingAmount.clear()
        this.originalNonRenewableRemainingAmount.addAll(this.remainingNonRenewableAmount)
    }

    def backNonRenewableConsumedAndRemainingAmountToOriginal() {
        this.nonRenewableConsumedAmount.clear()
        this.nonRenewableConsumedAmount.addAll(this.originalNonRenewableConsumedAmount)

        this.remainingNonRenewableAmount.clear()
        this.remainingNonRenewableAmount.addAll(this.originalNonRenewableRemainingAmount)
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
