package mrcpsp.process

import mrcpsp.process.RestrictionsProcessor
import spock.lang.Specification

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ResourceAvailabilities

class RestrictionsProcessorSpec extends Specification {

	def ra = new ResourceAvailabilities()
	def jobToSchedule, job, job1, job2	
	def jobs
	
	def setup() {
		jobToSchedule = new Job(id: 8, mode: new Mode(renewable: [7, 0]))
		job = new Job(id: 9, mode: new Mode(renewable: [0, 2]))
		job1 = new Job(id: 2, mode: new Mode(renewable: [0, 3]))
		job2 = new Job(id: 7, mode: new Mode(renewable: [9, 0]))
				
		jobs = [job, job1, job2]
		
		ra.remainingRenewableAmount = [1, 8]
	}
	
	def "Should return a job if there is enough resources to it be scheduled"() {
		given:
		def rp = new RestrictionsProcessor()
		
		when:
		def job = rp.getBestJobToRemoveRR(ra, jobs, jobToSchedule)
		
		then:
		job != null
		job.id == 7
	}
	
	def "Should return null if there is not enough resources to it be scheduled"() {
		given:
		def rp = new RestrictionsProcessor()
		jobToSchedule = new Job(id: 8, mode: new Mode(renewable: [7, 0]))
		job = new Job(id: 9, mode: new Mode(renewable: [0, 2]))
		job1 = new Job(id: 2, mode: new Mode(renewable: [0, 3]))
		job2 = new Job(id: 7, mode: new Mode(renewable: [2, 0]))
		jobs = [job, job1, job2]
		ra.remainingRenewableAmount = [1, 8]
		
		when:
		def job = rp.getBestJobToRemoveRR(ra, jobs, jobToSchedule)
		
		then:
		job == null		
	}
	
}
