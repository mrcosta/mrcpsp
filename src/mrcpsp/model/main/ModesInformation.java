package mrcpsp.model.main;

public class ModesInformation {
	
	private Integer longer;
	private Integer shorter;	
	
	private Integer lowerRenewableConsumption;
	private Integer greaterRenewableConsumption;
	
	private Integer lowerNonRenewableConsumption;
	private Integer greaterNonRenewableConsumption;
	
	private Integer higherSumComsuption;
	private Integer lowerSumComsuption;
	
	private Integer bestRatioNonRenewableComsumption;
	private Integer bestRatioRenewableComsumption;
	private Integer bestRatioComsumption;
	
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
	}
	
	public Integer getLonger() {
		return longer;
	}
	
	public void setLonger(Integer longer) {
		this.longer = longer;
	}
	
	public Integer getShorter() {
		return shorter;
	}
	
	public void setShorter(Integer shorter) {
		this.shorter = shorter;
	}

	public Integer getLowerRenewableConsumption() {
		return lowerRenewableConsumption;
	}

	public void setLowerRenewableConsumption(Integer lowerRenewableConsumption) {
		this.lowerRenewableConsumption = lowerRenewableConsumption;
	}

	public Integer getGreaterRenewableConsumption() {
		return greaterRenewableConsumption;
	}

	public void setGreaterRenewableConsumption(Integer greaterRenewableConsumption) {
		this.greaterRenewableConsumption = greaterRenewableConsumption;
	}

	public Integer getLowerNonRenewableConsumption() {
		return lowerNonRenewableConsumption;
	}

	public void setLowerNonRenewableConsumption(Integer lowerNonRenewableConsumption) {
		this.lowerNonRenewableConsumption = lowerNonRenewableConsumption;
	}

	public Integer getGreaterNonRenewableConsumption() {
		return greaterNonRenewableConsumption;
	}

	public void setGreaterNonRenewableConsumption(Integer greaterNonRenewableConsumption) {
		this.greaterNonRenewableConsumption = greaterNonRenewableConsumption;
	}

	public Integer getHigherSumComsuption() {
		return higherSumComsuption;
	}

	public void setHigherSumComsuption(Integer higherSumComsuption) {
		this.higherSumComsuption = higherSumComsuption;
	}

	public Integer getLowerSumComsuption() {
		return lowerSumComsuption;
	}

	public void setLowerSumComsuption(Integer lowerSumComsuption) {
		this.lowerSumComsuption = lowerSumComsuption;
	}
	
	public Integer getBestRatioNonRenewableComsumption() {
		return bestRatioNonRenewableComsumption;
	}

	public void setBestRatioNonRenewableComsumption(
			Integer bestRatioNonRenewableComsumption) {
		this.bestRatioNonRenewableComsumption = bestRatioNonRenewableComsumption;
	}

	public Integer getBestRatioRenewableComsumption() {
		return bestRatioRenewableComsumption;
	}

	public void setBestRatioRenewableComsumption(
			Integer bestRatioRenewableComsumption) {
		this.bestRatioRenewableComsumption = bestRatioRenewableComsumption;
	}

	public Integer getBestRatioComsumption() {
		return bestRatioComsumption;
	}

	public void setBestRatioComsumption(Integer bestRatioComsumption) {
		this.bestRatioComsumption = bestRatioComsumption;
	}

	@Override
	public String toString() {
		return "{ longer: " + longer + ", shorter: " + shorter
				+ ", lowerRenewableConsumption: " + lowerRenewableConsumption
				+ ", greaterRenewableConsumption: " + greaterRenewableConsumption
				+ ", lowerNonRenewableConsumption: " + lowerNonRenewableConsumption
				+ ", greaterNonRenewableConsumption: " + greaterNonRenewableConsumption 
				+ ", higherSumComsuption: " + higherSumComsuption 
				+ ", lowerSumComsuption: " + lowerSumComsuption + "}";
	}

}
