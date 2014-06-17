package mrcpsp.process.job

import mrcpsp.model.main.Job
import mrcpsp.process.mode.ModeOperations
import org.apache.log4j.Logger

class JobOperations {

	static final Logger log = Logger.getLogger(JobOperations.class)

    JobComparator jobComparator
    ModeOperations modeOperations

    JobOperations() {
        jobComparator = new JobComparator()
        modeOperations = new ModeOperations()
    }

    List<Job> getOnlyRealJobs(List<Job> jobs, Integer lastJobId) {
        return jobs.findAll{ job -> ![1, lastJobId].contains(job.id) }
    }

    List<Job> getJobsBetweenInterval(Job jobToSchedule, List<Job> jobs) {
        return jobs.findAll{ job ->
            (job.id != jobToSchedule.id) && (job.startTime < jobToSchedule.endTime) && (job.endTime > jobToSchedule.startTime) && (job.endTime > 0)
        }
    }

    /**
     * the second criteria is the job's order in the list
     */
    List<Job> getJobPredecessors(Job jobToSchedule, List<Job> jobs) {
        return jobs.findAll{ job ->
            jobToSchedule.predecessors.contains(job.id)
        }
    }

    List<Integer> getJobTotalSuccessors(Job jobToGet, List<Job> jobs) {
        def successors = jobs.findAll { jobToGet.successors.contains(it.id) }
        List<Integer> allSuccessorsId = jobToGet.successors

        successors.each { job ->
            allSuccessorsId.addAll(getJobTotalSuccessors(job, jobs))
        }

        return allSuccessorsId.unique()
    }

    List<Job> getJobsUsingIdList(List<Job> jobs, List<Integer> jobsId) {
        def result = []

        jobsId.each {
            result.add(jobs[it - 1])
        }

        return result
    }
}
