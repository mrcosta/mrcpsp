package mrcpsp.process.job

import mrcpsp.model.enums.EnumJobPriorityRules
import mrcpsp.model.main.Job
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Logger

class JobOperations {

	static final Logger log = Logger.getLogger(JobOperations.class);

    JobComparator jobComparator

    JobOperations() {
        jobComparator = new JobComparator()
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

    def setSumRankingForTheRealJobs(List<Job> realJobs, Map modesSumRankingPositions) {

        realJobs.each { job ->
            job.sumRanking = modesSumRankingPositions."$job.id".total
        }

        return realJobs
    }

    List<Job> orderJobsByPositionsSums(List<Job> realJobs) {
        Integer modesRankingJobsReverseOrder = UrlUtils.instance.modesRankingJobsReverseOrder

        jobComparator.comparatorType = EnumJobPriorityRules.BY_SUM_POSITIONS
        if (modesRankingJobsReverseOrder == PropertyConstants.TRUE) {
            Collections.sort(realJobs, Collections.reverseOrder(jobComparator))
        } else {
            Collections.sort(realJobs, jobComparator)
        }

        return realJobs
    }
}
