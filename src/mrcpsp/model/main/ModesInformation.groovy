package mrcpsp.model.main;

class ModesInformation {
	
	Integer longer;
	Integer shorter;
	
	Integer lowerRenewableConsumption;
	Integer greaterRenewableConsumption;
	
	Integer lowerNonRenewableConsumption;
	Integer greaterNonRenewableConsumption;
	
	Integer higherSumComsuption;
	Integer lowerSumComsuption;
	
	Integer bestRatioNonRenewableComsumption;
	Integer bestRatioRenewableComsumption;
	Integer bestRatioComsumption;

    Integer shorterNearToLowerNonRenewableComsumption

    List<Integer> modesByOrderDuration

    List<Integer> minNonRenewableResourcesConsumption
	
	public ModesInformation() {
		this.longer = 0;
		this.shorter = 0;
		
		this.lowerRenewableConsumption = 0;
		this.greaterRenewableConsumption = 0;
		
		this.lowerNonRenewableConsumption = 0;
		this.greaterNonRenewableConsumption = 0;
		
		this.lowerSumComsuption = 0;
		this.higherSumComsuption = 0;
		
		this.bestRatioNonRenewableComsumption = 0;
		this.bestRatioRenewableComsumption = 0;
		this.bestRatioComsumption = 0;

        this.shorterNearToLowerNonRenewableComsumption = 0
	}

	@Override
	public String toString() {
		return """{longer: $longer,
                   shorter: $shorter,
                   lowerRenewableConsumption: $lowerRenewableConsumption,
                   greaterRenewableConsumption: $greaterRenewableConsumption,
                   lowerNonRenewableConsumption: $lowerNonRenewableConsumption,
				   greaterNonRenewableConsumption: $greaterNonRenewableConsumption,
                   higherSumComsuption: $higherSumComsuption,
                   lowerSumComsuption: $lowerSumComsuption,
                   shorterNearToLowerNonRenewableComsumption: $shorterNearToLowerNonRenewableComsumption,
                   modesByOrderDuration: $modesByOrderDuration,
                   minNonRenewableResourcesConsumption: $minNonRenewableResourcesConsumption}""";
	}
}
