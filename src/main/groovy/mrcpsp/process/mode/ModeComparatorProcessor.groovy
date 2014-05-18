package mrcpsp.process.mode;

import com.google.common.collect.Iterables;
import mrcpsp.model.enums.EnumModesComparator;
import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.ModesInformation
import mrcpsp.model.main.ResourceAvailabilities;
import mrcpsp.utils.CloneUtils;
import mrcpsp.utils.LogUtils;
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;

class ModeComparatorProcessor {
	
	static final Logger log = Logger.getLogger(ModeComparatorProcessor.class)
	
	ModeComparator modeComparator
    ModeOperations modeOperations
	
	ModeComparatorProcessor() {
		modeComparator = new ModeComparator()
        modeOperations = new ModeOperations()
	}

    /**
     * order job modes list by renewable amount resources
     * @param job
     * @return
     */
	def ModesInformation orderModeByRenewableAmountResources(Job job) {
		def modesInformation = job.modesInformation
		// to not lose the natural modes order		
		def modeList = job.availableModes

        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_RENEWABLE
		Collections.sort(modeList, modeComparator);

        log.debug("JOB ID: " + job.id + " - MODES ORDER BY RENEWABLE AMOUNT RESOURCES: " + LogUtils.generateIDsModeListLog(modeList));
		
		def indexLowerRenewableConsumption = modeList[PropertyConstants.INDEX_START].id
		modesInformation.lowerRenewableConsumption = indexLowerRenewableConsumption

		def indexGreaterRenewableConsumption = Iterables.getLast(modeList).id
		modesInformation.greaterRenewableConsumption = indexGreaterRenewableConsumption

        // backing to the natural order (by id)
        job.availableModes = modeOperations.orderById(modeList)

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
		def modeList = job.availableModes
		
		modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_NON_RENEWABLE
		Collections.sort(modeList, modeComparator);
		log.debug("JOB ID: " + job.id + " - MODES ORDER BY NON RENEWABLE AMOUNT RESOURCES: " + LogUtils.generateIDsModeListLog(modeList));
		
		def indexLowerNonRenewableConsumption = modeList[PropertyConstants.INDEX_START].id
		modesInformation.lowerNonRenewableConsumption = indexLowerNonRenewableConsumption
		
		def indexGreaterNonRenewableConsumption = Iterables.getLast(modeList).id
		modesInformation.greaterNonRenewableConsumption = indexGreaterNonRenewableConsumption

        // backing to the natural order (by id)
        job.availableModes = modeOperations.orderById(modeList)
		
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
		def modeList = job.availableModes
		
		modeComparator.comparatorType = EnumModesComparator.MC_DURATION
		Collections.sort(modeList, modeComparator)
		log.debug("JOB ID: " + job.id + " - MODES ORDER BY DURATION: " + LogUtils.generateIDsModeListLog(modeList))
		
		def indexShorter = modeList[PropertyConstants.INDEX_START].id
		modesInformation.shorter = indexShorter
        modesInformation.modesByOrderDuration = modeList.id
		
		def indexLonger = Iterables.getLast(modeList).id
		modesInformation.longer = indexLonger

        // backing to the natural order (by id)
        job.availableModes = modeOperations.orderById(modeList)
		
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
		def modeList = job.availableModes
		
		modeComparator.comparatorType = EnumModesComparator.MC_SUM_RESOURCES
		Collections.sort(modeList, modeComparator);
		log.debug("JOB ID: " + job.id + " - MODES ORDER BY SUM OF THE RESOURCES: " + LogUtils.generateIDsModeListLog(modeList));
		
		def indexLowerSumComsuption = modeList[PropertyConstants.INDEX_START].id
		modesInformation.lowerSumComsuption = indexLowerSumComsuption
		
		def indexHigherSumComsuption = Iterables.getLast(modeList).id
		modesInformation.higherSumComsuption = indexHigherSumComsuption

        // backing to the natural order (by id)
        job.availableModes = modeOperations.orderById(modeList)
		
		return modesInformation;
	}

    /**
     * order job modes list by the sum of resources (renewable + non renewable)
     * @param job
     * @return
     */
    def ModesInformation checkModeNearLowerNRConsumption(Job job) {
        def indexLowerNonRenewableConsumption = job.modesInformation.lowerNonRenewableConsumption
        job.modesInformation.shorterNearToLowerNonRenewableComsumption = indexLowerNonRenewableConsumption

        def shorterModes = job.availableModes.findAll { it.duration < job.availableModes.find{ it.id == indexLowerNonRenewableConsumption }.duration}

        if (shorterModes) {
            shorterModes.each { mode ->
                def indexShorterNearToLowerNonRenewableConsumption = job.modesInformation.shorterNearToLowerNonRenewableComsumption
                def modeShorterNear = job.availableModes.find{ it.id == indexShorterNearToLowerNonRenewableConsumption}

                if (checkConditionsNearLowerNRConsumption(modeShorterNear, mode)) {
                    job.modesInformation.shorterNearToLowerNonRenewableComsumption = mode.id
                }
            }
        }

        return job.modesInformation
    }

    def boolean checkConditionsNearLowerNRConsumption(Mode modeShorterNear, Mode modeToCompare) {
        def percentage = UrlUtils.instance.modeShorterNearToLowerNrPercentage
        def units = UrlUtils.instance.modeShorterNearToLowerNrUnit

        if ( ((( (modeToCompare.amountNonRenewable * 100) / modeShorterNear.amountNonRenewable) - 100) <= percentage) || ((modeToCompare.amountNonRenewable - modeShorterNear.amountNonRenewable) <= units))  {
            return true
        } else {
            return false
        }
    }

    def List<Mode> excludeRenewableDumbModes(Job job, ResourceAvailabilities ra) {
        def modeList = job.availableModes
        def modesIdToRemove = []

        modeList.each { mode ->
            def countResource = 0
            mode.renewable.each {
                if (it > ra.renewableInitialAmount[countResource]) {
                    modesIdToRemove.add(mode.id)
                    log.info("JOB " + job.id + " - Excluded mode: " + mode.id + " - Duration: " + mode.duration + " - Values R: " + mode.renewable)
                }
                countResource++
            }
        }

        modeList.removeAll { modesIdToRemove.contains(it.id) }

        return modeList
    }

    def List<Mode> excludeNonRenewableDumbModes(Job job, ResourceAvailabilities ra) {
        def modesList = job.availableModes
        def modesIdToRemove = []

        modesList.each { mode ->
            def countResource = 0
            mode.nonRenewable.each {
                if (it > ra.nonRenewableInitialAmount[countResource]) {
                    modesIdToRemove.add(mode.id)
                    log.info("JOB " + job.id + " - Excluded mode: " + mode.id + " - Duration: " + mode.duration + " - Values NR: " + mode.nonRenewable)
                }
                countResource++
            }
        }

        modesList.removeAll { modesIdToRemove.contains(it.id) }

        return modesList
    }

    def getMinNonRenewableResourceConsumption(Job job) {
        job.modesInformation.minNonRenewableResourcesConsumption = []

        job.availableModes.each { mode ->
            int countNR = 0

            mode.nonRenewable.each {
                if (job.modesInformation.minNonRenewableResourcesConsumption[countNR] == null || (it < job.modesInformation.minNonRenewableResourcesConsumption[countNR]) ) {
                    job.modesInformation.minNonRenewableResourcesConsumption[countNR] = it
                }
                countNR++
            }
        }

        return job.modesInformation
    }

}
