package mrcpsp.process.initialsolution 

import mrcpsp.model.enums.EnumLogUtils 
import mrcpsp.model.main.Job 
import mrcpsp.model.main.Project 
import mrcpsp.process.job.JobOperations 
import mrcpsp.process.job.JobPriorityRulesOperations 
import mrcpsp.utils.* 
import org.apache.log4j.Logger 


/**
 * @author mateus
 *
 */
class GenerateInitialSolutionGRASP {
	
	static final Logger log = Logger.getLogger(GenerateInitialSolutionGRASP.class) 
	
	List<Integer> staggeredJobsId
	List<Integer> eligibleJobsId
	List<Integer> rcl
    List<Job> eligibleJobs

    InitialSolutionsOperations initialSolutionOperations
	JobOperations jobOperations 
	
	List<Integer> getInitialSolution(Project project) {
		executeGrasp(project.jobs)
		
		return staggeredJobsId
	}	
	
	GenerateInitialSolutionGRASP() {
		staggeredJobsId = []
		eligibleJobs = []
        eligibleJobsId = []
		rcl = []
		
		initialSolutionOperations = new InitialSolutionsOperations() 
		jobOperations = new JobOperations() 
	}
	
	private List<Job> executeGrasp(List<Job> jobs) {
		log.info("Executing GRASP - Initial Solution...")

        List<Integer> jobsId = jobs.id
        List<Job> remaniningJobs
		while (!jobsId.isEmpty()) {
			// getting the jobs available to be schedule
            remaniningJobs = jobOperations.getDifferentsJobsFromIdList(jobs, staggeredJobsId)
            eligibleJobsId = initialSolutionOperations.getEligibleJobsList(remaniningJobs, eligibleJobsId)
            eligibleJobs = jobOperations.getJobsFromIdList(jobs, eligibleJobsId)

			// create/update the rclJobsList
			rcl = generateRclJobsList(rcl, eligibleJobs)
			if (!rcl.isEmpty()) {
				// randomize a job to be scheduled
                Integer randomizedJobId = initialSolutionOperations.getRandomJobIdFromRCL(rcl)
                ChronoWatch.instance.pauseSolutionTime()
                staggeredJobsId.add(randomizedJobId)
                ChronoWatch.instance.startSolutionTime()
				
				// update the staggered predecessors jobs list of the remaining jobs
				initialSolutionOperations.updateRunningJobInformation(remaniningJobs, randomizedJobId)
				
				// clear the rclJobsList
				rcl.clear()
                eligibleJobsId.remove( (Object)eligibleJobsId.find{ it == randomizedJobId } )
                jobsId.remove( (Object)jobsId.find{ it == randomizedJobId } )

				log.info("The job with id " + randomizedJobId + " was scheduled!")
				log.info("The REMAINING JOBS list has this index: $jobsId ")
                log.info("The ELIGIBLE JOBS list has this index: $eligibleJobsId ")
                log.info("The STAGGERED JOBS jobs list has this index: $staggeredJobsId \n")
			}
			
		}
		
		log.info("Executing GRASP - Initial Solution was created... \n") 
	}
	
	private List<Integer> generateRclJobsList(List<Integer> rclJobsListId, List<Job> elegibleJobsList) {
        Integer rclSize = UrlUtils.instance.RCLSize
				
		// add all the eligible jobs to the rclJobsList and doesn't need to order
		if (rclSize >= elegibleJobsList.size()) {

            ChronoWatch.instance.pauseSolutionTime()
            elegibleJobsList.each { job ->
                rclJobsListId.add(job.id)
            }
            ChronoWatch.instance.startSolutionTime()
			
			log.debug("Was not necessary to order - The RCL SIZE is bigger or equals than the ELIGIBLES JOBS SIZE LIST") 
		} else {
			JobPriorityRulesOperations jprOperations = new JobPriorityRulesOperations() 
			
			// ordering the elegible jobs list
			jprOperations.orderEligibleJobsList(elegibleJobsList)
			log.debug("The ELIGIBLE JOBS LIST was sorted - " + UrlUtils.instance.jobPriorityRule + " -- JOBS: " + elegibleJobsList.id)

			// order the jobs by the criteria (mrcpsp.properties) and add the first ones until the rclJobsList size is equals to rclSize
            ChronoWatch.instance.pauseSolutionTime()
            Integer count = PropertyConstants.INDEX_START 
			while(rclJobsListId.size() != rclSize) {
				Integer jobId = elegibleJobsList.get(count++).id
				
				rclJobsListId.add(jobId)
			}
            ChronoWatch.instance.startSolutionTime()
		}
		log.info("The RCL JOBS list has this index: $rclJobsListId")
		
		return rclJobsListId
	}

}
