package mrcpsp.process.mode

import mrcpsp.model.enums.EnumModesComparator
import mrcpsp.model.main.Mode
import org.apache.log4j.Level
import org.apache.log4j.Logger

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
            case EnumModesComparator.MC_AMOUNT_FIRST_NR.name:
                result = mode.nonRenewable[0].compareTo(modeToCompare.nonRenewable[0])
        		break
            case EnumModesComparator.MC_AMOUNT_SECOND_NR.name:
                result = mode.nonRenewable[1].compareTo(modeToCompare.nonRenewable[1])
                break
            case EnumModesComparator.MC_AMOUNT_FIRST_R.name:
                result = mode.renewable[0].compareTo(modeToCompare.renewable[0])
                break
            case EnumModesComparator.MC_AMOUNT_SECOND_R.name:
                result = mode.renewable[1].compareTo(modeToCompare.renewable[1])
                break
            case EnumModesComparator.MC_SUM_RANKING_POSITIONS.name:
                result = mode.sumRanking.compareTo(modeToCompare.sumRanking)
                break
            case EnumModesComparator.MC_ID.name:
                result = mode.id.compareTo(modeToCompare.id)
                break
        	default:  
        		log.log(Level.ERROR, "Invalid Option to order the modes")
        		throw new RuntimeException("Invalid Option to order")
		}
		
		return result
	}
}
