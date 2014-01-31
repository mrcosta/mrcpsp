package mrcpsp.process.mode;

import java.util.Comparator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import mrcpsp.model.enums.EnumModesComparator;
import mrcpsp.model.main.Mode;

public class ModeComparator implements Comparator<Mode> {
	
	private static final Logger log = Logger.getLogger(ModeComparator.class);
	
	private EnumModesComparator comparatorType;

	@Override
	public int compare(Mode mode, Mode modeToCompare) {
		Integer result = 0;
		
		switch (this.comparatorType) {  
        	case MC_AMOUNT_RENEWABLE:  
        		result = mode.getAmountRenewable().compareTo(modeToCompare.getAmountRenewable());  
        		break;  
        	case MC_AMOUNT_NON_RENEWABLE:  
        		result = mode.getAmountNonRenewable().compareTo(modeToCompare.getAmountNonRenewable());  
        		break;  
        	case MC_DURATION:  
        		result = mode.getDuration().compareTo(modeToCompare.getDuration());  
        		break;  
        	case MC_SUM_RESOURCES:  
        		result = mode.getSumResources().compareTo(modeToCompare.getSumResources());  
        		break;  
        	default:  
        		log.log(Level.ERROR, "Invalid Option to order the modes");
        		throw new RuntimeException("Invalid Option to order");  
		}
		
		return result;
	}	

	public void setComparatorType(EnumModesComparator comparatorType) {
		this.comparatorType = comparatorType;
	}

}
