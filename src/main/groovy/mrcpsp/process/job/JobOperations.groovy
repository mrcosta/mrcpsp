package mrcpsp.process.job

import mrcpsp.model.enums.EnumJobPriorityRules
import mrcpsp.model.main.Job
import mrcpsp.process.mode.ModeOperations
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils
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

    List<Integer> getJobsBetweenInterval(Job jobToSchedule, List<Job> jobs) {
        return jobs.findAll{ job ->
            (job.id != jobToSchedule.id) && (job.startTime < jobToSchedule.endTime) && (job.endTime > jobToSchedule.startTime) && (job.endTime > 0)
        }.id
    }

    List<Integer> getOnlyRealJobsId(Map<Integer, Integer> jobsId, Integer lastJobId) {
        return jobsId.findAll{ jobId -> ![1, lastJobId].contains(jobId.key) }*.key
    }

    /**
     * the second criteria is the job's order in the list
     */
    List<Job> getJobPredecessors(Job jobToSchedule, List<Job> jobs) {
        return jobs.findAll{ job ->
            jobToSchedule.predecessors.contains(job.id)
        }
    }

    List<Job> getJobsFromIdList(List<Job> jobs, List<Integer> jobsId) {
        return jobs.findAll { jobsId.contains(it.id) }
    }

    List<Job> getDifferentsJobsFromIdList(List<Job> jobs, List<Integer> jobsId) {
        return jobs.findAll { !jobsId.contains(it.id) }
    }

    def setSumRankingForTheRealJobsAndUpdateModesInformation(List<Job> realJobs, Map modesSumRankingPositions) {

        realJobs.each { job ->
            job.sumRanking = modesSumRankingPositions."$job.id".total

            job.availableModes.each { mode ->
                mode.sumRanking = modesSumRankingPositions."$job.id"."$mode.id"
            }

            job.modesInformation.modesOrderedByRanking = modeOperations.orderBySumRanking(job.availableModes)
            def differenceBetweenModes = modeOperations.getDifferenceBetweenShorterAndRemainingModesAccordingToSumRanking(job.availableModes, job.modesInformation)
            log.info("JOB $job.id -- Modes natural(id) order: -- $job.availableModes.id -- Modes order by sum ranking: -- $job.modesInformation.modesOrderedByRanking -- Difference from the shorter mode: $differenceBetweenModes")
        }

        return realJobs
    }

    List<Job> orderJobsByPositionsSums(List<Job> realJobs) {
        Integer modesRankingJobsReverseOrder = UrlUtils.instance.modesRankingJobsReverseOrder

        log.info("Ordering using the reverse order: " + ((modesRankingJobsReverseOrder == PropertyConstants.TRUE) ? "TRUE" : "FALSE"))
        jobComparator.comparatorType = EnumJobPriorityRules.BY_SUM_POSITIONS
        if (modesRankingJobsReverseOrder == PropertyConstants.TRUE) {
            Collections.sort(realJobs, Collections.reverseOrder(jobComparator))
        } else {
            Collections.sort(realJobs, jobComparator)
        }

        return realJobs
    }

    List<Integer> getJobTotalSuccessors(Job jobToGet, List<Job> jobs) {
        def successors = jobs.findAll { jobToGet.successors.contains(it.id) }
        List<Integer> allSuccessorsId = jobToGet.successors

        successors.each { job ->
            allSuccessorsId.addAll(getJobTotalSuccessors(job, jobs))
        }

        return allSuccessorsId.unique()
    }
}
