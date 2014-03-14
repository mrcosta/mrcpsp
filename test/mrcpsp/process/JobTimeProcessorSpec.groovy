package mrcpsp.process

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ResourceAvailabilities
import spock.lang.Specification

/**
 * Created by mateus on 3/13/14.
 */
class JobTimeProcessorSpec extends Specification {

    def ra = new ResourceAvailabilities()
    def job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12
    def jobs

    def setup() {
        job1 = new Job(id: 1, mode: new Mode(renewable: [0, 0]))
        job2 = new Job(id: 2, mode: new Mode(renewable: [0, 3]))
        job3 = new Job(id: 3, mode: new Mode(renewable: [0, 8]))
        job4 = new Job(id: 4, mode: new Mode(renewable: [5, 0]))
        job5 = new Job(id: 5, mode: new Mode(renewable: [0, 7]))
        job6 = new Job(id: 6, mode: new Mode(renewable: [0, 6]))
        job7 = new Job(id: 7, mode: new Mode(renewable: [9, 0]))
        job8 = new Job(id: 8, mode: new Mode(renewable: [10, 0]))
        job9 = new Job(id: 9, mode: new Mode(renewable: [0, 2]))
        job10 = new Job(id: 10, mode: new Mode(renewable: [7, 0]))
        job11 = new Job(id: 11, mode: new Mode(renewable: [0, 10]))
        job12 = new Job(id: 12, mode: new Mode(renewable: [0, 0]))
        // scheduling order: { 1, 3, 4, 2, 9, 5, 6, 8, 7, 11, 10, 12 } --

        jobs = [job1, job3, job4, job2, job9, job5, job6, job8, job7, job11, job10, job12]

        ra.remainingRenewableAmount = [1, 8]
    }
}
