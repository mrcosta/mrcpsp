package mrcpsp.process.mode

import mrcpsp.model.enums.EnumJobPriorityRules
import mrcpsp.model.enums.EnumOrderModesCriteria
import mrcpsp.model.enums.EnumRankingModesCriteria
import mrcpsp.model.main.Project
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

/**
 * Created by mateus on 5/2/14.
 */
class ModeRankingOrchestrator {

    private static final Logger log = Logger.getLogger(ModeRankingOrchestrator.class)

    ModeRanking modeRanking
    Map modesSumRankingPositions

    ModeRankingOrchestrator() {
        modeRanking = new ModeRanking()
    }

    def rankJobsAndModesWithCriteria(Project project) {
        def modeRankingCriteria = UrlUtils.instance.modesRankingCriteria

        switch (modeRankingCriteria) {
            case EnumRankingModesCriteria.RK_ALL.name:
                modesSumRankingPositions = rankJobsAndModesAllCriteria(project)
                break
            case EnumRankingModesCriteria.RK_DRA.name:
                modesSumRankingPositions = rankJobsAndModesPerDurationAndRenewableAmount(project)
                break
            default:
                log.log(Level.ERROR, "Invalid Option to use to ranking the modes: " + ModeRankingOrchestrator.class)
                throw new RuntimeException("Invalid Option to ranking the modes: " + ModeRankingOrchestrator.class)
        }

        return modesSumRankingPositions
    }

    private def rankJobsAndModesAllCriteria(Project project) {
        def modesSumRankingPositions
        def realJobs
        def modes
        def criteria = [EnumOrderModesCriteria.PER_DURATION, EnumOrderModesCriteria.PER_AMOUNT, EnumOrderModesCriteria.PER_NR_AMOUNT, EnumOrderModesCriteria.PER_R_AMOUNT,
                        EnumOrderModesCriteria.PER_FIRST_NR_AMOUNT, EnumOrderModesCriteria.PER_SECOND_NR_AMOUNT, EnumOrderModesCriteria.PER_FIRST_R_AMOUNT, EnumOrderModesCriteria.PER_SECOND_R_AMOUNT]

        realJobs = modeRanking.getOnlyRealJobs(project)
        modes = modeRanking.createListWithAllModes(realJobs)
        modeRanking.createMapForModesRankingHistory(realJobs, criteria)

        modeRanking.rankPerDuration(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_DURATION)

        modeRanking.rankPerAmountConsumed(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_AMOUNT)

        modeRanking.rankPerAmountNonRenewable(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_NR_AMOUNT)

        modeRanking.rankPerAmountRenewable(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_R_AMOUNT)

        modeRanking.rankPerAmountFirstNonRenewable(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_FIRST_NR_AMOUNT)

        modeRanking.rankPerAmountSecondNonRenewable(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_SECOND_NR_AMOUNT)

        modeRanking.rankPerAmountFirstRenewable(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_FIRST_R_AMOUNT)

        modeRanking.rankPerAmountSecondRenewable(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_SECOND_R_AMOUNT)

        modesSumRankingPositions = modeRanking.getJobPositionsSum(realJobs, criteria)

        return modesSumRankingPositions
    }

    private def rankJobsAndModesPerDurationAndRenewableAmount(Project project) {
        def modesSumRankingPositions
        def realJobs
        def modes
        def criteria = [EnumOrderModesCriteria.PER_DURATION, EnumOrderModesCriteria.PER_R_AMOUNT]

        realJobs = modeRanking.getOnlyRealJobs(project)
        modes = modeRanking.createListWithAllModes(realJobs)
        modeRanking.createMapForModesRankingHistory(realJobs, criteria)

        modeRanking.rankPerDuration(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_DURATION)

        modeRanking.rankPerAmountRenewable(modes)
        modeRanking.saveModesRankingHistory(modes, EnumOrderModesCriteria.PER_R_AMOUNT)

        modesSumRankingPositions = modeRanking.getJobPositionsSum(realJobs, criteria)

        return modesSumRankingPositions
    }
}
