package mrcpsp.process.mode

import mrcpsp.model.enums.EnumModesComparator
import mrcpsp.model.enums.EnumOrderModesCriteria
import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.job.JobOperations
import org.apache.log4j.Logger

/**
 * Created by mateus on 5/1/14.
 */
class ModeRanking {

    static final Logger log = Logger.getLogger(ModeRanking.class)

    ModeComparator modeComparator
    def modesRankingHistory

    ModeRanking() {
        modeComparator = new ModeComparator()
        modesRankingHistory = [:]
    }

    List<Job> getOnlyRealJobs(Project project) {
        JobOperations jobOperations = new JobOperations()
        return jobOperations.getOnlyRealJobs(project.jobs, project.instanceInformation.jobsAmount)
    }

    List<Mode> createListWithAllModes(List<Job> jobs) {
        List<Mode> modes = []

        jobs.each { job ->
            job.availableModes.each { mode ->
                modes.add(mode)
            }
        }

        return modes
    }

    List<Mode> rankPerDuration(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_DURATION
        Collections.sort(modes, modeComparator)

        return modes
    }

    def rankPerAmountConsumed(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_SUM_RESOURCES
        Collections.sort(modes, modeComparator)

        return modes
    }

    def rankPerAmountNonRenewable(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_NON_RENEWABLE
        Collections.sort(modes, modeComparator)

        return modes
    }

    def rankPerAmountRenewable(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_RENEWABLE
        Collections.sort(modes, modeComparator)

        return modes
    }

    def rankPerAmountFirstNonRenewable(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_FIRST_NR
        Collections.sort(modes, modeComparator)

        return modes
    }

    def rankPerAmountSecondNonRenewable(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_SECOND_NR
        Collections.sort(modes, modeComparator)

        return modes
    }

    def rankPerAmountFirstRenewable(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_FIRST_R
        Collections.sort(modes, modeComparator)

        return modes
    }

    def rankPerAmountSecondRenewable(List<Mode> modes) {
        modeComparator.comparatorType = EnumModesComparator.MC_AMOUNT_SECOND_R
        Collections.sort(modes, modeComparator)

        return modes
    }

    def saveModesRankingHistory(List<Mode> modes, EnumOrderModesCriteria criterion) {

        modes.each { mode ->
            modesRankingHistory."$mode.jobId"."$criterion.name"."$mode.id" = modes.indexOf(mode) + 1

            if (modesRankingHistory."$mode.jobId"."$criterion.name".total == null) {
                modesRankingHistory."$mode.jobId"."$criterion.name".total = modes.indexOf(mode) + 1
            } else {
                modesRankingHistory."$mode.jobId"."$criterion.name".total+= modes.indexOf(mode) + 1
            }
        }

        return modesRankingHistory
    }

    def createMapForModesRankingHistory(List<Job> jobs, List<EnumOrderModesCriteria> criteria) {

        jobs.each { job ->
            modesRankingHistory."$job.id" = [:]
            criteria.each {
                modesRankingHistory."$job.id"."$it.name" = [:]
            }
        }

        return modesRankingHistory
    }

    def getJobPositionsSum(List<Job> realJobs, List<EnumOrderModesCriteria> criteria) {
        def modesSumRankingPositions = [:]

        realJobs.each { job ->
            def jobHistory = modesRankingHistory."$job.id"
            modesSumRankingPositions."$job.id" = [:]

            criteria.each { criterion ->

                jobHistory."$criterion.name".each { modesRanking ->
                    if (modesSumRankingPositions."$job.id"."$modesRanking.key" == null) {
                        modesSumRankingPositions."$job.id"."$modesRanking.key" = jobHistory."$criterion.name"."$modesRanking.key"
                    } else {
                        modesSumRankingPositions."$job.id"."$modesRanking.key"+= jobHistory."$criterion.name"."$modesRanking.key"
                    }
                }
            }

            def jobModesSumRankingPosition = modesSumRankingPositions."$job.id"
            log.info("Job $job.id has the following values for its ranking: $jobModesSumRankingPosition")
        }

        return modesSumRankingPositions
    }
}
