package mrcpsp.process.job

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.RunningJobInformation;
import spock.lang.Specification;

class JobPriorityRulesOperationsSpec extends Specification {

    JobPriorityRulesOperations jobPriorityRulesOperations

    def setup() {
        jobPriorityRulesOperations = new JobPriorityRulesOperations()
    }

    def "should return a job list ordered by the endTime (earliest first)" () {
        when:
        def result = jobPriorityRulesOperations.getJobListOrderByEndTime(jobs)

        then:
        result.size() == 4
        result*.id == jobsOrdered

        where:
        jobs                                                                                                               | jobsOrdered
        [new Job(id: 2, endTime: 5), new Job(id: 4, endTime: 6), new Job(id: 5, endTime: 9), new Job(id: 9, endTime: 6)]   | [2, 4, 9, 5]
        [new Job(id: 9, endTime: 6), new Job(id: 4, endTime: 6), new Job(id: 5, endTime: 9), new Job(id: 2, endTime: 6)]   | [9, 4, 2, 5]
        [new Job(id: 1, endTime: 6), new Job(id: 2, endTime: 6), new Job(id: 3, endTime: 6), new Job(id: 4, endTime: 6)]   | [1, 2, 3, 4]
        [new Job(id: 1, endTime: 6), new Job(id: 2, endTime: 7), new Job(id: 3, endTime: 8), new Job(id: 4, endTime: 9)]   | [1, 2, 3, 4]
        [new Job(id: 1, endTime: 9), new Job(id: 2, endTime: 8), new Job(id: 3, endTime: 7), new Job(id: 4, endTime: 6)]   | [4, 3, 2, 1]
        [new Job(id: 1, endTime: 5), new Job(id: 2, endTime: 4), new Job(id: 3, endTime: 3), new Job(id: 4, endTime: 5)]   | [3, 2, 1, 4]
    }

    def "should get the job's slack"() {
        when:
        def job1 = new Job(id: 1, predecessors: [], startTime: 0, endTime: 0)
        def job2 = new Job(id: 2, predecessors: [1], startTime: 0, endTime: 5)
        def job3 = new Job(id: 3, predecessors: [1], startTime: 1, endTime: 5)
        def job4 = new Job(id: 4, predecessors: [1], startTime: 0, endTime: 1)
        def job5 = new Job(id: 5, predecessors: [3], startTime: 5, endTime: 12)
        def job6 = new Job(id: 6, predecessors: [5], startTime: 12, endTime: 16)
        def job7 = new Job(id: 7, predecessors: [4, 6], startTime: 16, endTime: 18)
        def job8 = new Job(id: 8, predecessors: [6], startTime: 18, endTime: 20)
        def job9 = new Job(id: 9, predecessors: [3], startTime: 5, endTime: 7)
        def job10 = new Job(id: 10, predecessors: [2, 4, 8], startTime: 20, endTime: 27)
        def job11 = new Job(id: 11, predecessors: [2, 7, 8], startTime: 20, endTime: 24)
        def job12 = new Job(id: 12, predecessors: [9, 10, 11], startTime: 27, endTime: 27)
        def jobs = [job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12]

        def result = jobPriorityRulesOperations.getJobSlack(job, jobs)

        then:
        result == slack

        where:
        job                                                                     | slack
        new Job(id: 1, predecessors: [], startTime: 0, endTime: 0)              | 0
        new Job(id: 2, predecessors: [1], startTime: 0, endTime: 5)             | 0
        new Job(id: 3, predecessors: [1], startTime: 1, endTime: 5)             | 1
        new Job(id: 4, predecessors: [1], startTime: 0, endTime: 1)             | 0
        new Job(id: 5, predecessors: [3], startTime: 5, endTime: 12)            | 0
        new Job(id: 6, predecessors: [5], startTime: 15, endTime: 16)           | 3
        new Job(id: 7, predecessors: [4, 6], startTime: 16, endTime: 18)        | 0
        new Job(id: 8, predecessors: [6], startTime: 18, endTime: 20)           | 2
        new Job(id: 9, predecessors: [3], startTime: 5, endTime: 7)             | 0
        new Job(id: 10, predecessors: [2, 4, 8], startTime: 20, endTime: 27)    | 0
        new Job(id: 11, predecessors: [2, 7, 8], startTime: 24, endTime: 24)    | 4
        new Job(id: 12, predecessors: [9, 10, 11], startTime: 27, endTime: 27)  | 0
    }

    def "should get the job list ordered by the slack (minimum slack)" () {
        when:
        def result = jobPriorityRulesOperations.getJobListOrderBySlack(jobs)

        then:
        result.size() == 4
        result*.id == jobsOrdered

        where:
        jobs                                                                                                                                                                                                                                                                                                                                       | jobsOrdered
        [new Job(id: 2, runningJobInformation: new RunningJobInformation(slackAmount: 1)), new Job(id: 4, runningJobInformation: new RunningJobInformation(slackAmount: 2)), new Job(id: 5, runningJobInformation: new RunningJobInformation(slackAmount: 4)), new Job(id: 9, runningJobInformation: new RunningJobInformation(slackAmount: 3))]   | [2, 4, 9, 5]
        [new Job(id: 9, runningJobInformation: new RunningJobInformation(slackAmount: 1)), new Job(id: 4, runningJobInformation: new RunningJobInformation(slackAmount: 2)), new Job(id: 5, runningJobInformation: new RunningJobInformation(slackAmount: 5)), new Job(id: 2, runningJobInformation: new RunningJobInformation(slackAmount: 3))]   | [9, 4, 2, 5]
        [new Job(id: 1, runningJobInformation: new RunningJobInformation(slackAmount: 1)), new Job(id: 2, runningJobInformation: new RunningJobInformation(slackAmount: 2)), new Job(id: 3, runningJobInformation: new RunningJobInformation(slackAmount: 3)), new Job(id: 4, runningJobInformation: new RunningJobInformation(slackAmount: 4))]   | [1, 2, 3, 4]
        [new Job(id: 1, runningJobInformation: new RunningJobInformation(slackAmount: 4)), new Job(id: 2, runningJobInformation: new RunningJobInformation(slackAmount: 3)), new Job(id: 3, runningJobInformation: new RunningJobInformation(slackAmount: 2)), new Job(id: 4, runningJobInformation: new RunningJobInformation(slackAmount: 1))]   | [4, 3, 2, 1]
        [new Job(id: 1, runningJobInformation: new RunningJobInformation(slackAmount: 3)), new Job(id: 2, runningJobInformation: new RunningJobInformation(slackAmount: 2)), new Job(id: 3, runningJobInformation: new RunningJobInformation(slackAmount: 1)), new Job(id: 4, runningJobInformation: new RunningJobInformation(slackAmount: 4))]   | [3, 2, 1, 4]
    }

}
