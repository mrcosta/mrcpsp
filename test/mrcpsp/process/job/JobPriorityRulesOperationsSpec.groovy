package mrcpsp.process.job

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode;
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



}
