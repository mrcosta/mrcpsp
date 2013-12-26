package com.cefetmg.mmc.mrcpsp.process.job.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.process.job.JobOperations;
import com.cefetmg.mmc.mrcpsp.utils.CloneUtils;

public class JobOperationsImpl implements JobOperations {

	private static final Logger log = Logger.getLogger(JobOperationsImpl.class);
	
	@Override
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

	@Override
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

	@Override
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
