package mrcpsp.process

import mrcpsp.model.enums.EnumJobsMode
import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.mode.ShortestFeasibleMode
import mrcpsp.utils.UrlUtils
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

/**
 * @author mrcosta
 *
 */
public class JobsModeSelectProcessor {
	
	private static final Logger log = Logger.getLogger(JobsModeSelectProcessor.class);

    /**
     * each job get a mode that doesn't break the non renewable resources amount
     * @param jobs
     * @return
     */
	def setJobsMode(Project project) {
		def jobsMode = UrlUtils.getInstance().getJobsMode()

        log.info("MODE SELECTION: " + jobsMode)
        switch (jobsMode) {
            case EnumJobsMode.LESS_DURATION.description:
                project.staggeredJobs = setShorterDurationMode(project.jobs)
                break
            case EnumJobsMode.MINIMUM_NON_RENEWABLE_RESOURCES.description:
                project.staggeredJobs = setJobsLowerNonRenewableConsumption(project.jobs)
                break
            case EnumJobsMode.MINIMUM_RESOURCES.description:
                project.staggeredJobs = setJobsLowerSumComsuption(project.jobs)
                break
            case EnumJobsMode.SHORTER_NEAR_TO_LOWER_NON_RENEWABLE_RESOURCES.description:
                project.staggeredJobs = setJobsShorterNearToMinimumNonRenewableResources(project.jobs)
                break
            case EnumJobsMode.SHORTEST_FEASIBLE_MODE.description:
                project.staggeredJobs = setJobsModeShortestFeasbileMode(project)
                break
            default:
                log.log(Level.ERROR, "MODE SELECTION is not valid! Please check the argument 'mode.jobs' in mrcpsp.properties file");
                throw new IllegalArgumentException("MODE SELECTION is not valid! Please check the argument 'mode.jobs' in mrcpsp.properties file");
                break
        }
	}
	
	/**
	 * get a mode to each job, choosing the mode with the minimum duration to be execute
	 * @return
	 */
	def setShorterDurationMode(List<Job> jobs) {

		jobs.each { job ->
			def index = job.modesInformation.shorter
			job.mode = job.availableModes.find { it.id == index}

            log.info("JOB: " + job.id + " - Selected mode: " + job.mode.id + " - Duration: " + job.mode.duration + " - Values NR: " + job.mode.nonRenewable + " - Values R: " + job.mode.renewable)
		}
		
		return jobs
	}
	
	/**
	 * get a mode to each job, choosing the mode with the minimum non renewable required resources to be execute
	 * if the sum of resources are equals, then, get the mode that have the less duration.
	 * @return
	 */
	def List<Job> setJobsLowerNonRenewableConsumption(List<Job> jobs) {

        jobs.each { job ->
            def index = job.modesInformation.lowerNonRenewableConsumption
            job.mode = job.availableModes.find { it.id == index}

            log.info("JOB: " + job.id + " - Selected mode: " + job.mode.id + " - Duration: " + job.mode.duration + " - Values NR: " + job.mode.nonRenewable + " - Values R: " + job.mode.renewable)
        }

		return jobs;
	}
	
	/**
	 * get a mode to each job, choosing the mode with the minimum required resources to be execute
	 * if the sum of resources are equals, then, get the mode that have the less duration.
	 * @return
	 */
	def List<Job> setJobsLowerSumComsuption(List<Job> jobs) {

        jobs.each { job ->
            def index = job.modesInformation.lowerSumComsuption
            job.mode = job.availableModes.find { it.id == index}

            log.info("JOB: " + job.id + " - Selected mode: " + job.mode.id + " - Duration: " + job.mode.duration + " - Values NR: " + job.mode.nonRenewable + " - Values R: " + job.mode.renewable)
        }
		
		return jobs;
	}

    /**
     * get a mode to each job, choosing the mode that is next (use almost the amount that the lower nr consumption uses) to the lower non renewable consumption mode
     * @return
     */
    def List<Job> setJobsShorterNearToMinimumNonRenewableResources(List<Job> jobs) {

        jobs.each { job ->
            def index = job.modesInformation.shorterNearToLowerNonRenewableComsumption
            job.mode = job.availableModes.find { it.id == index}

            log.info("JOB: " + job.id + " - Selected mode: " + job.mode.id + " - Duration: " + job.mode.duration + " - Values NR: " + job.mode.nonRenewable + " - Values R: " + job.mode.renewable)
        }

        return jobs;
    }

    /**
     * get a mode to each job, choosing the shortest feasible mode
     * @return
     */
    def List<Job> setJobsModeShortestFeasbileMode(Project project) {
        ShortestFeasibleMode shortestFeasibleMode = new ShortestFeasibleMode()
        Map<String, String> jobsMode = shortestFeasibleMode.setJobsMode(project)

        jobsMode.each { jobMode ->
            Job job = project.jobs.find { job ->  job.id == jobMode.key}
            Mode mode = job.availableModes.find { it.id == jobMode.value}

            job.mode = mode

            log.info("JOB: " + job.id + " - Selected mode: " + job.mode.id + " - Duration: " + job.mode.duration + " - Values NR: " + job.mode.nonRenewable + " - Values R: " + job.mode.renewable)
        }

        return project.jobs;
    }
}
