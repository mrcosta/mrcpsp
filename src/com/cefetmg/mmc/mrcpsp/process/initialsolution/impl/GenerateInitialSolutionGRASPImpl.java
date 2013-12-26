package com.cefetmg.mmc.mrcpsp.process.initialsolution.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cefetmg.mmc.mrcpsp.model.enums.EnumLogUtils;
import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.model.main.Project;
import com.cefetmg.mmc.mrcpsp.process.initialsolution.GenerateInitialSolutionGRASP;
import com.cefetmg.mmc.mrcpsp.process.initialsolution.InitialSolutionOperations;
import com.cefetmg.mmc.mrcpsp.process.job.JobOperations;
import com.cefetmg.mmc.mrcpsp.process.job.JobPriorityRulesOperations;
import com.cefetmg.mmc.mrcpsp.process.job.impl.JobOperationsImpl;
import com.cefetmg.mmc.mrcpsp.process.job.impl.JobPriorityRulesOperationsImpl;
import com.cefetmg.mmc.mrcpsp.utils.CloneUtils;
import com.cefetmg.mmc.mrcpsp.utils.LogUtils;
import com.cefetmg.mmc.mrcpsp.utils.PropertyConstants;
import com.cefetmg.mmc.mrcpsp.utils.UrlUtils;

/**
 * @author mateus
 *
 */
public class GenerateInitialSolutionGRASPImpl implements GenerateInitialSolutionGRASP {
	
	private static final Logger log = Logger.getLogger(GenerateInitialSolutionGRASPImpl.class);
	
	private List<Job> staggeredJobs;
	private List<Job> eligibleJobs;
	private List<Job> rcl;
	private List<Job> clonedJobs;
	private InitialSolutionOperations initialSolutionOperations;
	private JobOperations jobOperations;
	
	@Override
	public List<Job> getInitialSolution(Project project) {
		clonedJobs = new ArrayList<Job>(project.getJobs());
		
		executeGrasp(clonedJobs);
		
		return staggeredJobs;
	}	
	
	public GenerateInitialSolutionGRASPImpl() {
		staggeredJobs = new ArrayList<Job>();
		eligibleJobs = new ArrayList<Job>();
		rcl = new ArrayList<Job>();		
		
		initialSolutionOperations = new InitialSolutionsOperationsImpl();
		jobOperations = new JobOperationsImpl();
	}
	
	private List<Job> executeGrasp(List<Job> jobs) {
		Job randomizedJob;		
		
		log.info("Executing GRASP - Initial Solution...");
		
		while (!jobs.isEmpty()) {
			// getting the jobs available to be schedule
			initialSolutionOperations.getEligibleJobsList(jobs, eligibleJobs);			

			// create/update the rclJobsList;
			generateRclJobsList(rcl, eligibleJobs, UrlUtils.getInstance().getRCLSize());
			
			if (!rcl.isEmpty()) {				
				// randomize a job to be scheduled
				randomizedJob = initialSolutionOperations.getRandomJobFromRCL(rcl);
				staggeredJobs.add(CloneUtils.cloneJob(randomizedJob));
				
				// update the staggered predecessors jobs list of the remaining jobs
				initialSolutionOperations.updateRunningJobInformation(jobs, randomizedJob);
				
				// clear the rclJobsList
				rcl.clear();
				jobOperations.removeJobFromListByIndex(eligibleJobs, randomizedJob.getId());
				jobOperations.removeJobFromListByIndex(jobs, randomizedJob.getId());
				
				log.info("The job with id " + randomizedJob.getId() + " was scheduled!");
				log.info(LogUtils.generateJobsIDListLog(jobs, EnumLogUtils.REMAINING_JOBS));
				log.info(LogUtils.generateJobsIDListLog(eligibleJobs, EnumLogUtils.ELIGIBLE_JOBS));				
				log.info(LogUtils.generateJobsIDListLog(staggeredJobs, EnumLogUtils.STAGGERED_JOBS) + "\n");
			}
			
		}
		
		log.info("Executing GRASP - Initial Solution was created... \n");
		return null;
	}
	
	private List<Job> generateRclJobsList(List<Job> rclJobsList, List<Job> elegibleJobsList, Integer rclSize) {		
		Integer sumRCLEligibleJobsList = rclJobsList.size() + elegibleJobsList.size();
				
		// add all the eligible jobs to the rclJobsList and doesn't need to order
		if (rclSize >= sumRCLEligibleJobsList) {
			
			for(Job job : elegibleJobsList) {
				rclJobsList.add(CloneUtils.cloneJob(job));
			}
			
			log.debug("Was not necessary to order - The RCL SIZE is bigger or equals than the ELIGIBLES JOBS SIZE LIST");
		} else {
			JobPriorityRulesOperations jprOperations = new JobPriorityRulesOperationsImpl();			
			
			// ordering the elegible jobs list
			jprOperations.orderEligibleJobsList(elegibleJobsList);
			log.debug("The ELIGIBLE JOBS LIST was sorted - " + UrlUtils.getInstance().getJobPriorityRule());
			log.debug(LogUtils.generateJobsIDListLog(eligibleJobs, EnumLogUtils.ELIGIBLE_JOBS));
			
			// order the jobs by the criteria (mrcpsp.properties) and add the first ones until the rclJobsList size is equals to rclSize
			Integer count = PropertyConstants.INDEX_START;
			while(rclJobsList.size() != rclSize) {
				Job job = elegibleJobsList.get(count++);
				
				rclJobsList.add(CloneUtils.cloneJob(job));
			}
			
			// back the jobs to the natural order - by the index
			jprOperations.backOrderEligibleJobsList(elegibleJobsList);
			log.debug("The ELIGIBLE JOBS LIST IN NATURAL ORDER - BY_ID");
			log.info(LogUtils.generateJobsIDListLog(eligibleJobs, EnumLogUtils.ELIGIBLE_JOBS));
		}
		log.info(LogUtils.generateJobsIDListLog(rclJobsList, EnumLogUtils.RCL_JOBS));
		
		return rclJobsList;
	}

}
