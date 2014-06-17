package mrcpsp.process.initialsolution

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.process.job.JobOperations
import mrcpsp.process.job.JobPriorityRulesOperations
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Logger

class GenerateInitialSolutionGRASP {
	
	static final Logger log = Logger.getLogger(GenerateInitialSolutionGRASP.class) 
	
	List<Integer> staggeredJobsId
	List<Integer> eligibleJobsId
	List<Integer> rclId
	InitialSolutionsOperations initialSolutionOperations
    JobOperations jobOperations

	List<Job> getInitialSolution(Project project) {
        project.jobs*.runningJobInformation*.eligible = false
        project.jobs*.runningJobInformation*.staggeredPredecessors*.clear()

        return executeGrasp(project)
	}
	
	GenerateInitialSolutionGRASP() {
        staggeredJobsId = []
        eligibleJobsId = []
		rclId = []
		
		initialSolutionOperations = new InitialSolutionsOperations()
        jobOperations = new JobOperations()
	}
	
	private List<Integer> executeGrasp(Project project) {
		List<Integer> remainingJobsId = new ArrayList<>(project.jobsId)
        Integer randomizedJobId

		while (!remainingJobsId.isEmpty()) {
			// getting the jobs available to be schedule
            eligibleJobsId = initialSolutionOperations.getEligibleJobsList(remainingJobsId, eligibleJobsId, project.jobs)

			// create/update the rclJobsList
			rclId = generateRclJobsList(rclId, eligibleJobsId, project.jobs)
			
			if (!rclId.isEmpty()) {
				// randomize a job to be scheduled
                randomizedJobId = initialSolutionOperations.getRandomJobIdFromRCL(rclId)
                staggeredJobsId.add(randomizedJobId)

				// update the staggered predecessors jobs list of the remaining jobs
				initialSolutionOperations.updateRunningJobInformation(remainingJobsId, randomizedJobId, project.jobs)
				
				// clear the rclJobsList
				rclId.clear()
                eligibleJobsId.remove( (Object) randomizedJobId )
                remainingJobsId.remove( (Object) randomizedJobId )
			}
			
		}

        log.info("Normal Jobs list: $project.jobsId")
        log.info("Staggered Jobs list: $staggeredJobsId")

        return staggeredJobsId
	}
	
	def List<Integer> generateRclJobsList(List<Integer> rclJobsListId, List<Integer> elegibleJobsListId, List<Job> jobs) {
        Double rclSize = UrlUtils.instance.RCLSize
        JobPriorityRulesOperations jprOperations = new JobPriorityRulesOperations()

        def elegibleJobsList = jobOperations.getJobsUsingIdList(jobs, elegibleJobsListId)

        jprOperations.orderEligibleJobsList(elegibleJobsList)

        Job max = elegibleJobsList[0]
        Job min = elegibleJobsList.last()

        ChronoWatch.instance.pauseSolutionTime()
        elegibleJobsList.each { job ->
            def resultCriterionRcl = min.totalSuccessors + (rclSize * (max.totalSuccessors -  min.totalSuccessors))

            if (job.totalSuccessors >= resultCriterionRcl) {
                rclJobsListId.add(job.id)
            }
        }
        ChronoWatch.instance.startSolutionTime()

        // back the jobs to the natural order - by the index
        jprOperations.backOrderEligibleJobsList(elegibleJobsList)

		return rclJobsListId
	}

}
