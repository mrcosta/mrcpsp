package mrcpsp.process.initialsolution

import mrcpsp.model.main.Job
import mrcpsp.model.main.RunningJobInformation
import org.apache.commons.lang.math.RandomUtils
import org.apache.log4j.Logger

class InitialSolutionsOperations {
	
	static final Logger log = Logger.getLogger(InitialSolutionsOperations.class);

    /**
     * If the predecessors list equals to staggeredPredecessors, so the job has all its predecessor schedulled.
     * Then the job is ready to be scheduled and added to the rclJobsList */
	List<Integer> getEligibleJobsList(List<Integer> remainingJobsId, List<Job> eligibleJobsListId, List<Job> jobs) {
		log.debug("Getting the list of remaining jobs to scheduling...");

        remainingJobsId.each { jobId ->
            Job job = jobs[jobId - 1]

			if (job.predecessors.size() == job.runningJobInformation.staggeredPredecessors.size() && !job.runningJobInformation.isEligible()) {

                eligibleJobsListId.add(job.id)
				job.runningJobInformation.eligible = true

				log.debug("The job with id " + job.id + " was included in the Eligible Jobs List.")
			}
		}

		return eligibleJobsListId
	}

	Integer getRandomJobIdFromRCL(List<Integer> rclJobsListId) {
		Integer randomIndex

		// random between 0 and rclJobsList.size
	    randomIndex = RandomUtils.nextInt(rclJobsListId.size())

		return rclJobsListId[randomIndex]
	}

    /**
     * Update the RunningJobInformation - nis, can and niscan amount
     * Update the predecessors list of the successors jobs of the task that was picked to be scheduled */
    def updateRunningJobInformation(List<Integer> remainingJobsId, Integer randomizedJobId, List<Job> jobs) {

        remainingJobsId.each {
            Job job = jobs[it - 1]

			RunningJobInformation runningJobInformation = job.runningJobInformation

			if (job.predecessors.contains(randomizedJobId)) {
				runningJobInformation.staggeredPredecessors.add(randomizedJobId)
			}
		}
	}
	
}
