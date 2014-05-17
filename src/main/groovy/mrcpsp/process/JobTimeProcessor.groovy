package mrcpsp.process

import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project;
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

    List<Job> getJobTimes(Project project) {
        boolean checkResources = false
        ResourceAvailabilities ra = project.resourceAvailabilities
        List<Job> jobs = project.jobs
        Map<Integer, Integer> staggeredJobsModesId = project.staggeredJobsModesId

        ra.resetRenewableResources()
        jobs*.startTime = 0
        jobs*.endTime = 0

        staggeredJobsModesId.each { jobsModesId ->
            log.debug("Getting start and finish time - JOB: $jobsModesId.key")

            def job = jobs[jobsModesId.key - 1]
            def mode = job.availableModes.find { jobsModesId.value == it.id }
            if (job.predecessors.isEmpty()) {
                checkResources = setTimeJobWithoutPredecessors(ra, job, mode,  jobs, staggeredJobsModesId)
            } else {
                checkResources = setTimeJobWithPredecessors(ra, job, mode, jobs, staggeredJobsModesId);
            }
        }

        return jobs;
    }

    Job setTimeJobWithoutPredecessors(ResourceAvailabilities ra, Job jobToSchedule, Mode mode,  List<Job> jobs, Map<Integer, Integer> staggeredJobsModesId) {
        jobToSchedule.startTime = 0
        jobToSchedule.endTime = jobToSchedule.startTime + mode.duration

        def isSchedulled = false
        while (!isSchedulled) {
            //get all the jobs that was already scheduled in the proposed interval for the job that will be scheduled
            ra.scheduledJobsId = jobOperations.getJobsBetweenInterval(jobToSchedule, jobs)

            ra.scheduledJobsId?.each { jobId ->
                Mode scheduledJobMode = jobs[jobId - 1].availableModes.find { it.id ==  staggeredJobsModesId[jobId]}
                modeOperations.addingRenewableResources(ra, scheduledJobMode)
            }

            log.info("Scheduling job without predecessors - $jobToSchedule.id - Mode $mode.id - Duration: $mode.duration - RR: $mode.renewable - Jobs in interval: " + ra.scheduledJobsId + " - Resources usage: " + ra.toStringRenewable())

            // then we need to remove the jobs to schedule the one that we want and change its time (start and end)
            if (!modeOperations.checkRenewableResourcesAmount(ra, mode)) {
                jobToSchedule = setJobTimeUsingScheduledJobs(ra, jobToSchedule, jobs, staggeredJobsModesId, mode)
            } else {
                isSchedulled = true
            }

            ra.resetRenewableResources()
            ra.scheduledJobsId.clear()
        }

        log.info("JOB $jobToSchedule.id was scheduled: {start: $jobToSchedule.startTime, end: $jobToSchedule.endTime}\n")
        return jobToSchedule
    }

    Job setTimeJobWithPredecessors(ResourceAvailabilities ra, Job jobToSchedule, Mode mode, List<Job> jobs, Map<Integer, Integer> staggeredJobsModesId) {
        List<Job> jobPredecessors = jobOperations.getJobPredecessors(jobToSchedule, jobs)
        jobPredecessors = jobPriorityRulesOperations.getJobListOrderByEndTime(jobPredecessors)

        Job latestJob = jobPredecessors.last()
        jobToSchedule.startTime = latestJob.endTime
        jobToSchedule.endTime = jobToSchedule.startTime + mode.duration

        def isSchedulled = false
        while (!isSchedulled) {
            //get all the jobs that was already scheduled in the proposed interval for the job that will be scheduled
            ra.scheduledJobsId = jobOperations.getJobsBetweenInterval(jobToSchedule, jobs)

            ra.scheduledJobsId?.each { jobId ->
                Mode scheduledJobMode = jobs[jobId - 1].availableModes.find { it.id ==  staggeredJobsModesId[jobId]}
                modeOperations.addingRenewableResources(ra, scheduledJobMode)
            }

            log.info("Scheduling job with predecessors - $jobToSchedule.id - Mode $mode.id - Duration: $mode.duration - RR: $mode.renewable - Jobs in interval: " + ra.scheduledJobsId + " - Resources usage: " + ra.toStringRenewable())

            // we need to remove the jobs to schedule the one that we want and change its time (start and end)
            if (!modeOperations.checkRenewableResourcesAmount(ra, mode)) {
                jobToSchedule = setJobTimeUsingScheduledJobs(ra, jobToSchedule, jobs, staggeredJobsModesId, mode)
            } else {
                isSchedulled = true
            }

            ra.resetRenewableResources()
            ra.scheduledJobsId.clear()
        }

        log.info("JOB $jobToSchedule.id was scheduled: {start: $jobToSchedule.startTime, end: $jobToSchedule.endTime}\n")
        return jobToSchedule
    }

    Job setJobTimeUsingScheduledJobs(ResourceAvailabilities ra, Job jobToSchedule, List<Job> jobs, Map<Integer, Integer> staggeredJobsModesId, Mode modeToAdd) {
        def scheduledJobs = jobOperations.getJobsFromIdList(jobs, ra.scheduledJobsId)
        def scheduledJobsId
        // order jobs by endTime (from earliest end to the latest end)
        scheduledJobs = jobPriorityRulesOperations.getJobListOrderByEndTime(scheduledJobs)
        scheduledJobsId = scheduledJobs.id

        boolean checkJobScheduled = false
        while (!scheduledJobsId.isEmpty() && !checkJobScheduled) {
            Job jobToRemove = jobs[scheduledJobsId[0] - 1]
            Mode modeToRemove = jobToRemove.availableModes.find {it.id == staggeredJobsModesId[jobToRemove.id]}

            modeOperations.removingRenewableResources(ra, modeToRemove)

            if (modeOperations.checkRenewableResourcesAmount(ra, modeToAdd)) {
                jobToSchedule.startTime = jobToRemove.endTime
                jobToSchedule.endTime = jobToSchedule.startTime + modeToAdd.duration
                checkJobScheduled = true
            }

            scheduledJobsId.remove(0)
        }

        return jobToSchedule
    }
}
