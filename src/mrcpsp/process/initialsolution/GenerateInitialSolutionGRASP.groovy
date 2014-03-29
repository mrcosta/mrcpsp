package mrcpsp.process.initialsolution 

import mrcpsp.model.enums.EnumLogUtils 
import mrcpsp.model.main.Job 
import mrcpsp.model.main.Project 
import mrcpsp.process.job.JobOperations 
import mrcpsp.process.job.JobPriorityRulesOperations 
import mrcpsp.utils.* 
import org.apache.log4j.Logger 

import java.util.ArrayList 
import java.util.List 

/**
 * @author mateus
 *
 */
class GenerateInitialSolutionGRASP {
	
	static final Logger log = Logger.getLogger(GenerateInitialSolutionGRASP.class) 
	
	List<Job> staggeredJobs 
	List<Job> eligibleJobs 
	List<Job> rcl 
	List<Job> clonedJobs 
	InitialSolutionsOperations initialSolutionOperations 
	JobOperations jobOperations 
	
	List<Job> getInitialSolution(Project project) {
		clonedJobs = new ArrayList<Job>(project.jobs) 
		
		executeGrasp(clonedJobs) 
		
		return staggeredJobs
	}	
	
	GenerateInitialSolutionGRASP() {
		staggeredJobs = []
		eligibleJobs = []
		rcl = []
		
		initialSolutionOperations = new InitialSolutionsOperations() 
		jobOperations = new JobOperations() 
	}
	
	private List<Job> executeGrasp(List<Job> jobs) {
		Job randomizedJob 		
		
		log.info("Executing GRASP - Initial Solution...") 
		
		while (!jobs.isEmpty()) {
			// getting the jobs available to be schedule
            eligibleJobs = initialSolutionOperations.getEligibleJobsList(jobs, eligibleJobs)

			// create/update the rclJobsList 
			rcl = generateRclJobsList(rcl, eligibleJobs)
			
			if (!rcl.isEmpty()) {				
				// randomize a job to be scheduled
				randomizedJob = initialSolutionOperations.getRandomJobFromRCL(rcl) 
                ChronoWatch.instance.pauseSolutionTime()
                staggeredJobs.add(CloneUtils.cloneJob(randomizedJob)) 
                ChronoWatch.instance.startSolutionTime()
				
				// update the staggered predecessors jobs list of the remaining jobs
				initialSolutionOperations.updateRunningJobInformation(jobs, randomizedJob) 
				
				// clear the rclJobsList
				rcl.clear()
                eligibleJobs.remove( eligibleJobs.find{ it.id == randomizedJob.id } )
                jobs.remove( jobs.find{ it.id == randomizedJob.id } )

				log.info("The job with id " + randomizedJob.getId() + " was scheduled!") 
				log.info(LogUtils.generateJobsIDListLog(jobs, EnumLogUtils.REMAINING_JOBS)) 
				log.info(LogUtils.generateJobsIDListLog(eligibleJobs, EnumLogUtils.ELIGIBLE_JOBS)) 				
				log.info(LogUtils.generateJobsIDListLog(staggeredJobs, EnumLogUtils.STAGGERED_JOBS) + "\n") 
			}
			
		}
		
		log.info("Executing GRASP - Initial Solution was created... \n") 
	}
	
	private List<Job> generateRclJobsList(List<Job> rclJobsList, List<Job> elegibleJobsList) {
        Integer rclSize = UrlUtils.instance.RCLSize
				
		// add all the eligible jobs to the rclJobsList and doesn't need to order
		if (rclSize >= elegibleJobsList.size()) {

            ChronoWatch.instance.pauseSolutionTime()
            elegibleJobsList.each { job ->
                rclJobsList.add(CloneUtils.cloneJob(job))
            }
            ChronoWatch.instance.startSolutionTime()
			
			log.debug("Was not necessary to order - The RCL SIZE is bigger or equals than the ELIGIBLES JOBS SIZE LIST") 
		} else {
			JobPriorityRulesOperations jprOperations = new JobPriorityRulesOperations() 
			
			// ordering the elegible jobs list
			jprOperations.orderEligibleJobsList(elegibleJobsList) 
			log.debug("The ELIGIBLE JOBS LIST was sorted - " + UrlUtils.instance.jobPriorityRule)
			log.debug(LogUtils.generateJobsIDListLog(eligibleJobs, EnumLogUtils.ELIGIBLE_JOBS)) 
			
			// order the jobs by the criteria (mrcpsp.properties) and add the first ones until the rclJobsList size is equals to rclSize
            ChronoWatch.instance().pauseSolutionTime()
            Integer count = PropertyConstants.INDEX_START 
			while(rclJobsList.size() != rclSize) {
				Job job = elegibleJobsList.get(count++) 
				
				rclJobsList.add(CloneUtils.cloneJob(job)) 
			}
            ChronoWatch.instance.startSolutionTime()
			
			// back the jobs to the natural order - by the index
			jprOperations.backOrderEligibleJobsList(elegibleJobsList) 
			log.debug("The ELIGIBLE JOBS LIST IN NATURAL ORDER - BY_ID") 
			log.info(LogUtils.generateJobsIDListLog(eligibleJobs, EnumLogUtils.ELIGIBLE_JOBS)) 
		}
		log.info(LogUtils.generateJobsIDListLog(rclJobsList, EnumLogUtils.RCL_JOBS)) 
		
		return rclJobsList 
	}

}
