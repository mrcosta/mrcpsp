package mrcpsp.process.mode

import mrcpsp.model.enums.EnumOrderModesCriteria
import mrcpsp.model.enums.EnumRankingModesCriteria
import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.process.job.JobComparator
import mrcpsp.utils.FileUtils
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger
import spock.lang.Specification

/**
 * Created by mateus on 5/2/14.
 */
class ModeRankingOrchestratorSpec extends Specification {

    /**
     * Information from the j1029_3.mm that show one of the worsts results - psplib: 24 --- this work: 27 -- difference: 12.500
     */
    def project
    def jobs
    def job, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12

    def ModeRanking modeRanking

    def setup() {
        modeRanking = new ModeRanking()

        job = new Job(id: 1)
        job2 = new Job(id: 2)
        job3 = new Job(id: 3)
        job4 = new Job(id: 4)
        job5 = new Job(id: 5)
        job6 = new Job(id: 6)
        job7 = new Job(id: 7)
        job8 = new Job(id: 8)
        job9 = new Job(id: 9)
        job10 = new Job(id: 10)
        job11 = new Job(id: 11)
        job12 = new Job(id: 12)
        jobs = [job, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12]

        project = new Project()

        File file = new File(System.getProperty("user.dir") + "/data/j10.mm/" + "j1029_3.mm")
        FileUtils fileUtils = new FileUtils()
        project.instanceInformation = fileUtils.readInstanceInformation(file)
        project.jobs = fileUtils.readJobsInformation(file, project.instanceInformation)
        project.jobs = fileUtils.readModesInformation(project.jobs, file, project.instanceInformation)
    }

    def "should order the all jobs by the sum of positions"() {
        given:
        modeRanking = Spy(ModeRanking, {
            modeComparator: new ModeComparator()
            jobComparator: new JobComparator()
        }
        )

        when:
        def realJobs = modeRanking.rankJobsAndModes(project)

        then:
        1 * modeRanking.getOnlyRealJobs(project)
        1 * modeRanking.createListWithAllModes(_)
        1 * modeRanking.createMapForModesRankingHistory(_, _)
        1 * modeRanking.rankPerDuration(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_DURATION)
        1 * modeRanking.rankPerAmountConsumed(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_AMOUNT)
        1 * modeRanking.rankPerAmountNonRenewable(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_NR_AMOUNT)
        1 * modeRanking.rankPerAmountRenewable(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_R_AMOUNT)
        1 * modeRanking.rankPerAmountFirstNonRenewable(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_FIRST_NR_AMOUNT)
        1 * modeRanking.rankPerAmountSecondNonRenewable(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_SECOND_NR_AMOUNT)
        1 * modeRanking.rankPerAmountFirstRenewable(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_FIRST_R_AMOUNT)
        1 * modeRanking.rankPerAmountSecondRenewable(_)
        1 * modeRanking.saveModesRankingHistory(_, EnumOrderModesCriteria.PER_SECOND_R_AMOUNT)
        1 * modeRanking.getJobPositionsSum(_, _)
    }
}
