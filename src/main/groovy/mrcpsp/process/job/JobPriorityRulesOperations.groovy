package mrcpsp.process.job

import mrcpsp.model.enums.EnumJobPriorityRules
import mrcpsp.model.main.Job

class JobPriorityRulesOperations {

	List<Job> orderEligibleJobsList(List<Job> elegibleJobsList) {

        getJobListOrderTotalSuccessors(elegibleJobsList)

		return elegibleJobsList
	}

	public List<Job> backOrderEligibleJobsList(List<Job> elegibleJobsList) {
		JobComparator jobComparator = new JobComparator();
		
		jobComparator.comparatorType = EnumJobPriorityRules.BY_ID
		Collections.sort(elegibleJobsList, jobComparator);		
		
		return elegibleJobsList;
	}

	List<Job> getJobListOrderByEndTime(List<Job> jobs) {
		JobComparator jobComparator = new JobComparator()
		
		jobComparator.comparatorType = EnumJobPriorityRules.BY_END_TIME

        // the second criteria ir the job's order in the list
        Collections.sort(jobs, jobComparator);
		//Collections.sort(jobs, Collections.reverseOrder(jobComparator));
		
		return jobs
	}

    List<Job> getJobListOrderTotalSuccessors(List<Job> jobs) {
        JobComparator jobComparator = new JobComparator()

        jobComparator.comparatorType = EnumJobPriorityRules.TOTAL_SUCCESSORS

        Collections.sort(jobs, Collections.reverseOrder(jobComparator));  //bigger to smaller

        return jobs
    }

}
