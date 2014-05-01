package mrcpsp.process.mode

import mrcpsp.model.enums.EnumOrderModesCriteria
import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.utils.FileUtils
import spock.lang.Specification

/**
 * Created by mateus on 5/1/14.
 */
class ModeRankingSpec extends Specification {

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

    def "should create a list with all modes"() {
        given:
        def realJobs = modeRanking.getOnlyRealJobs(project)

        when:
        def result = modeRanking.createListWithAllModes(realJobs)

        then:
        result.size() == 30
    }

    def "should rank the jobs and its modes per duration"() {
        given:
        def realJobs = modeRanking.getOnlyRealJobs(project)
        def modes = modeRanking.createListWithAllModes(realJobs)

        when:
        def orderedModes = modeRanking.rankPerDuration(modes)

        then:
        orderedModes.size() == 30
        orderedModes[0].jobId == 8 && orderedModes[0].id == 1
        orderedModes[1].jobId == 11 && orderedModes[0].id == 1
        orderedModes[29].jobId == 6 && orderedModes[29].id == 3
        orderedModes[28].jobId == 4 && orderedModes[28].id == 3
    }

    def "should rank jobs and their modes per amount of resources consumed"() {

    }

    def "should rank jobs and their modes of consumption per amount of non-renewable resources"() {

    }

    def "should rank jobs and their modes of consumption per amount of renewable resources"() {

    }

    def "should rank jobs and their modes of consumption per amount of the first non-renewable resources"() {

    }

    def "should rank jobs and their modes of consumption per amount of the second non-renewable resources"() {

    }

    def "should rank jobs and their modes of consumption per amount of the first renewable resources"() {

    }

    def "should rank jobs and their modes of consumption per amount of the second renewable resources"() {

    }

    def "should put the information about the specific criterion ranking in the modesRankingHistory map"() {
        given:
        def realJobs = modeRanking.getOnlyRealJobs(project)
        def criteria = [EnumOrderModesCriteria.PER_DURATION, EnumOrderModesCriteria.PER_AMOUNT]
        def modesRankingHistory = modeRanking.createMapForModesRankingHistory(realJobs, criteria)
        def modes = modeRanking.createListWithAllModes(realJobs)
        def orderedModes = modeRanking.rankPerDuration(modes)

        when:
        modesRankingHistory = modeRanking.saveModesRankingHistory(orderedModes, EnumOrderModesCriteria.PER_DURATION)

        then:
        modesRankingHistory != null
        modesRankingHistory."8"."D"."1" == 1
        modesRankingHistory."11"."D"."1" == 2
        modesRankingHistory."6"."D"."3" == 30
        modesRankingHistory."4"."D"."3" == 29
    }

    def "should create a map for the jobs with the criteria that will be used to ranking"() {
        given:
        def realJobs = modeRanking.getOnlyRealJobs(project)
        def criteria = [EnumOrderModesCriteria.PER_DURATION, EnumOrderModesCriteria.PER_AMOUNT]

        when:
        def modesRankingHistory = modeRanking.createMapForModesRankingHistory(realJobs, criteria)

        then:
        modesRankingHistory != null
        modesRankingHistory."2" != null
    }
}
