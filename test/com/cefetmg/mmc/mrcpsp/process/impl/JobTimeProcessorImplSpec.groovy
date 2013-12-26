package com.cefetmg.mmc.mrcpsp.process.impl

import spock.lang.Specification

import com.cefetmg.mmc.mrcpsp.model.main.Job
import com.cefetmg.mmc.mrcpsp.model.main.Mode
import com.cefetmg.mmc.mrcpsp.model.main.ResourceAvailabilities
import com.cefetmg.mmc.mrcpsp.utils.PropertyConstants;

class JobTimeProcessorImplSpec extends Specification {
	
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
		def rp = Mock(RestrictionsProcessorImpl)
		def jtpi = new JobTimeProcessorImpl(restrictionsProcessor: rp)
		
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
		def rp = Mock(RestrictionsProcessorImpl)
		def jtpi = new JobTimeProcessorImpl(restrictionsProcessor: rp)
				
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
