package mrcpsp.process.initialsolution

import mrcpsp.model.main.Job
import mrcpsp.model.main.RunningJobInformation
import mrcpsp.utils.UrlUtils
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by mateus on 6/6/14.
 */
class GenerateInitialSolutionGRASPSpec extends Specification {

    GenerateInitialSolutionGRASP gisgrasp
    @Shared eligibleJobs

    def setup() {
        gisgrasp = new GenerateInitialSolutionGRASP()

        eligibleJobs = [new Job(id: 1, runningJobInformation: new RunningJobInformation(totalSuccessors: 10)),
                            new Job(id: 2, runningJobInformation: new RunningJobInformation(totalSuccessors: 8)),
                            new Job(id: 3, runningJobInformation: new RunningJobInformation(totalSuccessors: 6)),
                            new Job(id: 4, runningJobInformation: new RunningJobInformation(totalSuccessors: 4)),
                            new Job(id: 5, runningJobInformation: new RunningJobInformation(totalSuccessors: 2)),
        ]
    }

    // TODO: Ignoring this one, because depends on the configuration file. Need to check how to exactly mock static modes and classes
    @Ignore
    def "should create a rcl for GRASP according with the alpha parameter and the jobs totalSuccessors amount"() {
        given:
        GroovySpy(UrlUtils, global: true)

        when:
        def rcl = gisgrasp.generateRclJobsList(rclList, eligibleJobs)

        then:
        1* UrlUtils.instance.RCLSize >> 0
        1* UrlUtils.instance.RCLSize >> 0.8
        1* UrlUtils.instance.RCLSize >> 0.6
        1* UrlUtils.instance.RCLSize >> 0.4

        and:
        rcl.size() == expectedSize

        where:
        rclList | expectedSize
        []      |  1
        []      |  1
        []      |  2
        []      |  3
    }


}
