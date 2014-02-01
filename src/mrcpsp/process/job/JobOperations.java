package mrcpsp.process.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import mrcpsp.model.main.Job;
import mrcpsp.utils.CloneUtils;

public class JobOperations {

	private static final Logger log = Logger.getLogger(JobOperations.class);

    /**
     * remove the job, using as base the index of the clonedJob
     * @param jobs
     * @param index
     * @return
     */
	public Job removeJobFromListByIndex(List<Job> jobs, Integer index) {
		boolean foundJob = false;
		Integer count = 0;
		Job job = null;
		
		while (!foundJob && (count != jobs.size())) {
			job = jobs.get(count);
			
			if (job.getId() == index) {
				jobs.remove(job);
				foundJob = true;
			}
			
			count++;
		}
		
		if (foundJob) {
			return job;
		} else {
			return null;
		}
	}

    /**
     * remove the jobs, using as base the each index in the list passed by parameter
     * @param jobs
     * @param indexList
     * @return
     */
	public List<Job> removeJobsFromListByIndexList(List<Job> jobs, List<Integer> indexList) {
		Job job;
		List<Job> jobsRemoved = new ArrayList<Job>();
		
		for (Integer index: indexList) {
			
			job = removeJobFromListByIndex(jobs, index);
			
			if (job != null) {
				jobsRemoved.add(job);
				log.debug("JOB " + index + " was removed from the list.");
			} else {
				log.debug("JOB " + index + " was already removed from the list.");
			}
		}
		
		return jobsRemoved;
	}

    /**
     * get a cloned list with only the jobs passed in the index list
     * @param jobs
     * @param indexList
     * @return
     */
	public List<Job> getJobsCopyListByIndexList(List<Job> jobs, List<Integer> indexList) {
		List<Job> clonedJobs = new ArrayList<Job>();
		boolean foundJob = false;
		Integer count = 0;
		Job job = null;
		
		for (Integer index: indexList) {
			foundJob = false;
			count = 0;
			
			while (!foundJob && (count != jobs.size())) {
				job = jobs.get(count);
				
				if (job.getId() == index) {
					clonedJobs.add(CloneUtils.cloneJob(job));
					foundJob = true;
				}
				
				count++;
			}
		}

		return clonedJobs;
	}

}
