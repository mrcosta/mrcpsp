package mrcpsp.process

import mrcpsp.process.JobTimeProcessor
import mrcpsp.process.RestrictionsProcessor
import spock.lang.Specification

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.utils.PropertyConstants;

class JobTimeProcessorSpec extends Specification {
	
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
	
	def "Should remove just one job"() {
		given:
		def rp = Mock(RestrictionsProcessor)
		def jtpi = new JobTimeProcessor(restrictionsProcessor: rp)
		
		when:
		def jobs = jtpi.subtractRRACheckingAmount(ra, jobs, jobToSchedule)
		
		then:
		rp.getBestJobToRemoveRR(ra, jobs, jobToSchedule) >> new Job(id: 7)
		rp.setRenewableResourcesConsumedAmount(ra, jobToSchedule.mode, PropertyConstants.SUBTRACT) >> true
		rp.checkResourcesBeforeSetValues >> true		
		
		then:
		jobs.size == 1
		jobs.get(0).id == 7
	}
	
	def "Should remove three jobs"() {
		given:
		def rp = Mock(RestrictionsProcessor)
		def jtpi = new JobTimeProcessor(restrictionsProcessor: rp)
				
		when:
		def jobs = jtpi.subtractRRACheckingAmount(ra, jobs, jobToSchedule)
		
		then:
		rp.getBestJobToRemoveRR(ra, jobs, jobToSchedule) >> null
		rp.setRenewableResourcesConsumedAmount(ra, jobToSchedule.mode, PropertyConstants.SUBTRACT) >> true
		rp.checkResourcesBeforeSetValues >> true
		
		then:
		jobs.size == 3		
	}	

}
