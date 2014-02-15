package mrcpsp.process.mode;

import com.google.common.collect.Iterables;
import mrcpsp.model.enums.EnumModesComparator;
import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.ModesInformation;
import mrcpsp.utils.CloneUtils;
import mrcpsp.utils.LogUtils;
import mrcpsp.utils.PropertyConstants;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;

class ModeComparatorProcessor {
	
	static final Logger log = Logger.getLogger(ModeComparatorProcessor.class);
	
	ModeComparator modeComparator;
	
	public ModeComparatorProcessor() {
		modeComparator = new ModeComparator();
	}

    /**
     * order job modes list by renewable amount resources
     * @param job
     * @return
     */
	def ModesInformation orderModeByRenewableAmountResources(Job job) {
		def modesInformation = job.modesInformation
		// to not lose the natural modes order		
		def cloneModeList = CloneUtils.cloneModeList(job.availableModes);

        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_RENEWABLE
		Collections.sort(cloneModeList, modeComparator);

        log.debug("JOB ID: " + job.id + " - MODES ORDER BY RENEWABLE AMOUNT RESOURCES: " + LogUtils.generateIDsModeListLog(cloneModeList));
		
		def indexLowerRenewableConsumption = cloneModeList[PropertyConstants.INDEX_START].id
		modesInformation.lowerRenewableConsumption = indexLowerRenewableConsumption

		def indexGreaterRenewableConsumption = Iterables.getLast(cloneModeList).id
		modesInformation.greaterRenewableConsumption = indexGreaterRenewableConsumption

		return modesInformation;
	}

    /**
     * order job modes list by non renewable amount resources
     * @param job
     * @return
     */
	def ModesInformation orderModeByNonRenewableAmountResources(Job job) {
		def modesInformation = job.modesInformation
		// to not lose the natural modes order		
		def cloneModeList = CloneUtils.cloneModeList(job.availableModes);
		
		modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_NON_RENEWABLE
		Collections.sort(cloneModeList, modeComparator);
		log.debug("JOB ID: " + job.id + " - MODES ORDER BY NON RENEWABLE AMOUNT RESOURCES: " + LogUtils.generateIDsModeListLog(cloneModeList));
		
		def indexLowerNonRenewableConsumption = cloneModeList[PropertyConstants.INDEX_START].id
		modesInformation.lowerNonRenewableConsumption = indexLowerNonRenewableConsumption
		
		def indexGreaterNonRenewableConsumption = Iterables.getLast(cloneModeList).id
		modesInformation.greaterNonRenewableConsumption = indexGreaterNonRenewableConsumption
		
		return modesInformation;
	}

    /**
     * order job modes list by duration of the mode
     * @param job
     * @return
     */
	def ModesInformation orderModeByDuration(Job job) {
		def modesInformation = job.modesInformation
		// to not lose the natural modes order		
		def cloneModeList = CloneUtils.cloneModeList(job.availableModes)
		
		modeComparator.comparatorType = EnumModesComparator.MC_DURATION
		Collections.sort(cloneModeList, modeComparator)
		log.debug("JOB ID: " + job.id + " - MODES ORDER BY DURATION: " + LogUtils.generateIDsModeListLog(cloneModeList))
		
		def indexShorter = cloneModeList[PropertyConstants.INDEX_START].id
		modesInformation.shorter = indexShorter
		
		def indexLonger = Iterables.getLast(cloneModeList).id
		modesInformation.longer = indexLonger
		
		return modesInformation
	}

    /**
     * order job modes list by the sum of resources (renewable + non renewable)
     * @param job
     * @return
     */
	def ModesInformation orderModeBySumResources(Job job) {
		def modesInformation = job.modesInformation
		// to not lose the natural modes order		
		def cloneModeList = CloneUtils.cloneModeList(job.availableModes)
		
		modeComparator.comparatorType = EnumModesComparator.MC_SUM_RESOURCES
		Collections.sort(cloneModeList, modeComparator);
		log.debug("JOB ID: " + job.id + " - MODES ORDER BY SUM OF THE RESOURCES: " + LogUtils.generateIDsModeListLog(cloneModeList));
		
		def indexLowerSumComsuption = cloneModeList[PropertyConstants.INDEX_START].id
		modesInformation.lowerSumComsuption = indexLowerSumComsuption
		
		def indexHigherSumComsuption = Iterables.getLast(cloneModeList).id
		modesInformation.higherSumComsuption = indexHigherSumComsuption
		
		return modesInformation;
	}

    /**
     * order job modes list by the sum of resources (renewable + non renewable)
     * @param job
     * @return
     */
    def ModesInformation checkModeNearLowerNRConsumption(Job job) {

    }

}
