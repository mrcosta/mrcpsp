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

    List<Integer> scheduledJobsId

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

        scheduledJobsId = []
	}

    def resetRenewableResources() {
        for (int i = 0; i < renewableConsumedAmount.size(); i++) {
            renewableConsumedAmount[i] = 0
        }

        remainingRenewableAmount.clear()
        remainingRenewableAmount.addAll(this.renewableInitialAmount)

        scheduledJobsId = []
    }

    def setOriginalNonRenewableConsumedAndRemainingAmount() {
        originalNonRenewableConsumedAmount.clear()
        originalNonRenewableConsumedAmount.addAll(this.nonRenewableConsumedAmount)

        originalNonRenewableRemainingAmount.clear()
        originalNonRenewableRemainingAmount.addAll(this.remainingNonRenewableAmount)
    }

    def backNonRenewableConsumedAndRemainingAmountToOriginal() {
        nonRenewableConsumedAmount.clear()
        nonRenewableConsumedAmount.addAll(this.originalNonRenewableConsumedAmount)

        remainingNonRenewableAmount.clear()
        remainingNonRenewableAmount.addAll(this.originalNonRenewableRemainingAmount)
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
