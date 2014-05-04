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

    def setSumRankingForTheRealJobsAndUpdateModesInformation(List<Job> realJobs, Map modesSumRankingPositions) {

        realJobs.each { job ->
            job.sumRanking = modesSumRankingPositions."$job.id".total

            job.availableModes.each { mode ->
                mode.sumRanking = modesSumRankingPositions."$job.id"."$mode.id"
            }

            job.modesInformation.modesOrderedByRanking = modeOperations.orderBySumRanking(job.availableModes)
            log.info("JOB $job.id -- Modes natural(id) order: -- $job.availableModes.id -- Modes order by sum ranking: -- $job.modesInformation.modesOrderedByRanking")
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
}
