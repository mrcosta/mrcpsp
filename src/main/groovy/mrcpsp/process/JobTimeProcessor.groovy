package mrcpsp.process

import mrcpsp.model.main.Job;
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.process.job.JobOperations
import mrcpsp.process.job.JobPriorityRulesOperations
import mrcpsp.process.mode.ModeOperations;
import mrcpsp.utils.CloneUtils;
import mrcpsp.utils.LogUtils;
import mrcpsp.utils.PropertyConstants;
import org.apache.log4j.Logger

class JobTimeProcessor {

    static final Logger log = Logger.getLogger(JobTimeProcessor.class);

    ModeOperations modeOperations
    JobPriorityRulesOperations jobPriorityRulesOperations
    JobOperations jobOperations

    JobTimeProcessor() {
        modeOperations = new ModeOperations()
        jobPriorityRulesOperations = new JobPriorityRulesOperations()
        jobOperations = new JobOperations()
    }

    List<Job> getJobTimes(ResourceAvailabilities ra, List<Job> jobs) {
        boolean checkResources = false

        ra.resetRenewableResources()
        jobs*.startTime = 0
        jobs*.endTime = 0

        jobs.each { job ->
            log.debug("Getting start and finish time - JOB: $job.id")

            if (job.predecessors.isEmpty()) {
                checkResources = setTimeJobWithoutPredecessors(ra, job, jobs)
            } else {
                checkResources = setTimeJobWithPredecessors(ra, job, jobs);
            }
        }

        return jobs;
    }

    Job setTimeJobWithoutPredecessors(ResourceAvailabilities ra, Job jobToSchedule, List<Job> jobs) {
        jobToSchedule.startTime = 0
        jobToSchedule.endTime = jobToSchedule.startTime + jobToSchedule.mode.duration

        def isSchedulled = false
        while (!isSchedulled) {
            //get all the jobs that was already scheduled in the proposed interval for the job that will be scheduled
            ra.scheduledJobs = jobOperations.getJobsBetweenInterval(jobToSchedule, jobs)

            ra.scheduledJobs?.each {
                modeOperations.addingRenewableResources(ra, it.mode)
            }

            log.info("Scheduling job without predecessors - $jobToSchedule.id - Mode $jobToSchedule.mode.id RR: $jobToSchedule.mode.renewable - Jobs in interval: " + ra.scheduledJobs?.id + " - Resources usage: " + ra.toStringRenewable())

            // then we need to remove the jobs to schedule the one that we want and change its time (start and end)
            if (!modeOperations.checkRenewableResourcesAmount(ra, jobToSchedule.mode)) {
                jobToSchedule = setJobTimeUsingScheduledJobs(ra, jobToSchedule)
            } else {
                isSchedulled = true
            }

            resetResources(ra)
            ra.scheduledJobs.clear()
        }

        log.info("JOB $jobToSchedule.id was scheduled: {start: $jobToSchedule.startTime, end: $jobToSchedule.endTime}\n")
        return jobToSchedule
    }

    Job setTimeJobWithPredecessors(ResourceAvailabilities ra, Job jobToSchedule, List<Job> jobs) {
        List<Job> jobPredecessors = jobOperations.getJobPredecessors(jobToSchedule, jobs)
        jobPredecessors = jobPriorityRulesOperations.getJobListOrderByEndTime(jobPredecessors)

        Job latestJob = jobPredecessors.last()
        jobToSchedule.startTime = latestJob.endTime
        jobToSchedule.endTime = jobToSchedule.startTime + jobToSchedule.mode.duration

        def isSchedulled = false
        while (!isSchedulled) {
            //get all the jobs that was already scheduled in the proposed interval for the job that will be scheduled
            ra.scheduledJobs = jobOperations.getJobsBetweenInterval(jobToSchedule, jobs)

            ra.scheduledJobs?.each {
                modeOperations.addingRenewableResources(ra, it.mode)
            }

            log.info("Scheduling job with predecessors - $jobToSchedule.id - Mode $jobToSchedule.mode.id RR: $jobToSchedule.mode.renewable - Jobs in interval: " + ra.scheduledJobs?.id + " - Resources usage: " + ra.toStringRenewable())

            // we need to remove the jobs to schedule the one that we want and change its time (start and end)
            if (!modeOperations.checkRenewableResourcesAmount(ra, jobToSchedule.mode)) {
                jobToSchedule = setJobTimeUsingScheduledJobs(ra, jobToSchedule)
            } else {
                isSchedulled = true
            }

            resetResources(ra)
            ra.scheduledJobs.clear()
        }

        log.info("JOB $jobToSchedule.id was scheduled: {start: $jobToSchedule.startTime, end: $jobToSchedule.endTime}\n")
        return jobToSchedule
    }

    Job setJobTimeUsingScheduledJobs(ResourceAvailabilities ra, Job jobToSchedule) {
        // order jobs by endTime (from earliest end to the latest end)
        ra.scheduledJobs = jobPriorityRulesOperations.getJobListOrderByEndTime(ra.scheduledJobs)

        boolean checkJobScheduled = false
        while (!ra.scheduledJobs.isEmpty() && !checkJobScheduled) {
            Job jobToRemove = ra.scheduledJobs[0]

            modeOperations.removingRenewableResources(ra, jobToRemove.mode)

            if (modeOperations.checkRenewableResourcesAmount(ra, jobToSchedule.mode)) {
                jobToSchedule.startTime = jobToRemove.endTime
                jobToSchedule.endTime = jobToSchedule.startTime + jobToSchedule.mode.duration
                checkJobScheduled = true
            }

            ra.scheduledJobs.remove(0)
        }

        return jobToSchedule
    }

    List<Job> resetResources(ResourceAvailabilities ra) {
        if (!ra.scheduledJobs.isEmpty()) {
            ra.scheduledJobs.each { scheduledJob ->
                modeOperations.removingRenewableResources(ra, scheduledJob.mode)
            }
        }
    }
}
