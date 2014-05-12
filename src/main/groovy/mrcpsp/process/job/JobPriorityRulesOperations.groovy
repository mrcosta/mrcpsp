package mrcpsp.process.job;

import mrcpsp.model.enums.EnumJobPriorityRules;
import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.process.JobTimeProcessor;
import mrcpsp.utils.PropertyConstants;
import mrcpsp.utils.UrlUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

class JobPriorityRulesOperations {

	private static final Logger log = Logger.getLogger(JobPriorityRulesOperations.class)

    JobOperations jobOperations

    JobPriorityRulesOperations() {
        jobOperations = new JobOperations()
    }

    /**
     * only the priority rules that doesn't no need update information at runtime
     * @param jobs
     * @return
     */
	List<Job> setJobsPriorityRuleInformation(Project project) {
		String priorityRule = UrlUtils.instance.jobPriorityRule
        def jobs

        switch (priorityRule) {
            case EnumJobPriorityRules.MAX_NIS.name:
                jobs = getJobsNIS(project.jobs)
                break
            case EnumJobPriorityRules.MIN_SLK.name:
                jobs = getJobsSLK(project.resourceAvailabilities, project.jobs)
                break
            case EnumJobPriorityRules.TOTAL_SUCCESSORS.name:
                jobs = getJobsTotalSuccessors(project.jobs)
                break
            default:
                log.log(Level.ERROR, "JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file")
                throw new IllegalArgumentException("JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file")
        }

		return jobs
	}

    /**
     * Check the priority rule passed in the config file and call the relativer method to order the
     * eligible jobs list
     * @param elegibleJobsList
     * @return
     */
	List<Job> orderEligibleJobsList(List<Job> elegibleJobsList) {
		String priorityRule = UrlUtils.instance.jobPriorityRule
		
		switch (priorityRule) {
            case EnumJobPriorityRules.MAX_NIS.name:
                getJobListOrderByMaxNis(elegibleJobsList)
                break
            case EnumJobPriorityRules.MIN_SLK.name:
                getJobListOrderBySlack(elegibleJobsList)
                break
            case EnumJobPriorityRules.TOTAL_SUCCESSORS.name:
                getJobListOrderTotalSuccessors(elegibleJobsList)
                break
            default:
                log.log(Level.ERROR, "JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file")
                throw new IllegalArgumentException("JOB PRIORITY RULE is not valid! Please check the argument 'job.priority.rule' in mrcpsp.properties file")
        }

		return elegibleJobsList
	}

    /**
     * order the elegible Jobs by the its MAX_NIS (Maximum Number of Immediate Successors)
     * the jobs that have only the 'x' task as its predecessor. Ex: 8 has only 5 as your predecessors, so we can say
     * that the 8 its a immediate successor of 5
     * @param elegibleJobs
     * @return
     */
	List<Job> getJobListOrderByMaxNis(List<Job> elegibleJobs) {
		JobComparator jobComparator = new JobComparator()
		
		jobComparator.comparatorType = EnumJobPriorityRules.MAX_NIS
		Collections.sort(elegibleJobs, Collections.reverseOrder(jobComparator))
		
		return elegibleJobs;
	}

    /**
     * Back the order of the elegible jobs list - by the job ID
     * @param elegibleJobsList
     * @return
     */
	public List<Job> backOrderEligibleJobsList(List<Job> elegibleJobsList) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.comparatorType = EnumJobPriorityRules.BY_ID
		Collections.sort(elegibleJobsList, jobComparator);		
		
		return elegibleJobsList;
	}

    /**
     * Update the job NIS - Maximum Number of Immediate Successors, run only one time.
     * There is no need to update this type of priority rule operation
     * @param jobs
     */
	public List<Job> getJobsNIS(List<Job> jobs) {
        jobs.each { job ->
            // so we update the nis amount of predecessor amount. the job has just one connection with predecessor
            if (job.predecessorsAmount == 1) {
                Integer predecessorIndex = job.predecessors[PropertyConstants.INDEX_START] - 1
                Job j = jobs[predecessorIndex]
                Integer countNIS = j.runningJobInformation.nisAmount

                countNIS++;

                j.runningJobInformation.nisAmount = countNIS
                log.info("JOB " + job.id + " has JOB " + job.id + " as your only predecessor. "
                        + "JOB: " + job.id + " PRI: " + j.runningJobInformation.toString())
            }
        }

		return jobs
	}

    public List<Job> getJobsSLK(ResourceAvailabilities ra, List<Job> jobs) {
        jobs = getJobTimes(ra, jobs)
        jobs.each { job ->
            job.runningJobInformation.slackAmount = getJobSlack(job, jobs)
        }
    }

    def List<Job> getJobsTotalSuccessors(List<Job> jobs) {
        jobs.each { job ->
            job.runningJobInformation.totalSuccessors = jobOperations.getJobTotalSuccessors(job, jobs).size()
        }

        log.info("${EnumJobPriorityRules.TOTAL_SUCCESSORS.name} was choosen. JOBS: $jobs.id -- $jobs.runningJobInformation.totalSuccessors")
        return jobs
    }

    def getJobTimes(ResourceAvailabilities ra, List<Job> jobs) {
        def jobTimeProcessor = new JobTimeProcessor()

        try {
            jobs = jobTimeProcessor.getJobTimes(ra, jobs)
            return jobs
        } catch (Exception e) {
            log.error("Exception during the executeGetJobTimes to get the slack for all jobs", e)
        }
    }

    def getJobSlack(Job job, List<Job> jobs) {
        def predecessors = jobs.findAll { job.predecessors.contains(it.id) }
        predecessors = getJobListOrderByEndTime(predecessors)

        if (!predecessors.isEmpty()) {
            Job latestJob = predecessors.last()
            def slackAmount = job.startTime - latestJob.endTime

            return slackAmount
        } else {
            return 0
        }
    }

    /**
     * order the predecessor Jobs by the its end time. Used to set the start and the finish time of each job
     * @param jobs
     * @return
     */
	List<Job> getJobListOrderByEndTime(List<Job> jobs) {
		JobComparator jobComparator = new JobComparator()
		
		jobComparator.comparatorType = EnumJobPriorityRules.BY_END_TIME

        // the second criteria ir the job's order in the list
        Collections.sort(jobs, jobComparator);
		//Collections.sort(jobs, Collections.reverseOrder(jobComparator));
		
		return jobs
	}

    List<Job> getJobListOrderBySlack(List<Job> jobs) {
        JobComparator jobComparator = new JobComparator()

        jobComparator.comparatorType = EnumJobPriorityRules.MIN_SLK

        // the second criteria ir the job's order in the list
        Collections.sort(jobs, jobComparator);
        //Collections.sort(jobs, Collections.reverseOrder(jobComparator));  //bigger to smaller

        return jobs
    }

    List<Job> getJobListOrderTotalSuccessors(List<Job> jobs) {
        JobComparator jobComparator = new JobComparator()

        jobComparator.comparatorType = EnumJobPriorityRules.TOTAL_SUCCESSORS

        // the second criteria ir the job's order in the list
        //Collections.sort(jobs, jobComparator);
        Collections.sort(jobs, Collections.reverseOrder(jobComparator));  //bigger to smaller

        return jobs
    }

}
