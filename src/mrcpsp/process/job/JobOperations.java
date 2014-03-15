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

}
