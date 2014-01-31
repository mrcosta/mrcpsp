package mrcpsp.process.localsearch

import mrcpsp.process.localsearch.LowerNonRenewableConsumption
import spock.lang.Specification

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities

class LowerNonRenewableConsumptionSpec extends Specification {
	
	Project project
	def ra	
	def jobs, job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12
	
	def setup() {
		job1 = new Job(id: 1, mode: new Mode(nonRenewable: [0, 0]), availableModes: [new Mode(nonRenewable: [0, 0])])
		job3 = new Job(id: 3, mode: new Mode(nonRenewable: [3, 0]), availableModes: [new Mode(nonRenewable: [0, 8])])
		job5 = new Job(id: 5, mode: new Mode(nonRenewable: [6, 0]), availableModes: [new Mode(nonRenewable: [0, 10])])
		job2 = new Job(id: 2, mode: new Mode(nonRenewable: [0, 4]), availableModes: [new Mode(nonRenewable: [9, 0])])
		job9 = new Job(id: 9, mode: new Mode(nonRenewable: [0, 1]), availableModes: [new Mode(nonRenewable: [0, 2])])
		job4 = new Job(id: 4, mode: new Mode(nonRenewable: [2, 0]), availableModes: [new Mode(nonRenewable: [0, 5])])
		job6 = new Job(id: 6, mode: new Mode(nonRenewable: [4, 0]), availableModes: [new Mode(nonRenewable: [4, 0])])
		job8 = new Job(id: 8, mode: new Mode(nonRenewable: [0, 6]), availableModes: [new Mode(nonRenewable: [7, 0])])
		job7 = new Job(id: 7, mode: new Mode(nonRenewable: [0, 5]), availableModes: [new Mode(nonRenewable: [0, 5])])
		job11 = new Job(id: 11, mode: new Mode(nonRenewable: [0, 7]), availableModes: [new Mode(nonRenewable: [8, 0])])
		job10 = new Job(id: 10, mode: new Mode(nonRenewable: [2, 0]), availableModes: [new Mode(nonRenewable: [0, 9])])
		job12 = new Job(id: 12, mode: new Mode(nonRenewable: [0, 0]), availableModes: [new Mode(nonRenewable: [0, 0])])
		
		jobs = [job1, job3, job5, job2, job9, job4, job6, job8, job7, job11, job10, job12]
		ra = new ResourceAvailabilities(remainingNonRenewableAmount : [12, 7], nonRenewableConsumedAmount: [17, 23], nonRenewableInitialAmount: [29, 30])
		project = new Project(staggeredJobs : jobs, resourceAvailabilities : ra)		
	}
	
	def "should check if is possible to change a mode associate to a job"() {
		given:
		def lnrr = new LowerNonRenewableConsumption()
		
		when:
		def result = lnrr.checkNonRenewableResourcesRestriction(project, jobPosition)
		
		then:
		result == check
		project.resourceAvailabilities.remainingNonRenewableAmount == remainingNonRenewableAmount
		project.resourceAvailabilities.nonRenewableConsumedAmount == nonRenewableConsumedAmount
		project.staggeredJobs[jobPosition].mode.nonRenewable == modeNonRenewable
		
		where:                                                                                
		jobPosition		| remainingNonRenewableAmount | nonRenewableConsumedAmount  | check  | modeNonRenewable
		2				| [12, 7]               	  |	[17, 23]					| false  | [6, 0]
		5				| [14, 2]             	      | [15, 28]					| true   | [0, 5]
		7				| [5, 13] 			  		  |	[24, 17]					| true   | [7, 0]
		9				| [4, 14] 			  		  | [25, 16]					| true   | [8, 0]
		10				| [12, 7]            		  |	[17, 23]					| false  | [2, 0]
	}

}
