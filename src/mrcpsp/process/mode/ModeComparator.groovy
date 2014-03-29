package mrcpsp.process.mode;

import java.util.Comparator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import mrcpsp.model.enums.EnumModesComparator;
import mrcpsp.model.main.Mode;

class ModeComparator implements Comparator<Mode> {
	
	private static final Logger log = Logger.getLogger(ModeComparator.class);
	
	EnumModesComparator comparatorType;

	@Override
	int compare(Mode mode, Mode modeToCompare) {
		Integer result = 0;
		
		switch (this.comparatorType.name) {
        	case EnumModesComparator.MC_AMOUNT_RENEWABLE.name:
        		result = mode.amountRenewable.compareTo(modeToCompare.amountRenewable)
        		break
        	case EnumModesComparator.MC_AMOUNT_NON_RENEWABLE.name:
        		result = mode.amountNonRenewable.compareTo(modeToCompare.amountNonRenewable)
        		break
        	case EnumModesComparator.MC_DURATION.name:
        		result = mode.duration.compareTo(modeToCompare.duration)
        		break
            case EnumModesComparator.MC_SUM_RESOURCES.name:
        		result = mode.sumResources.compareTo(modeToCompare.sumResources)
        		break
        	default:  
        		log.log(Level.ERROR, "Invalid Option to order the modes")
        		throw new RuntimeException("Invalid Option to order")
		}
		
		return result
	}
}
