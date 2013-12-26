package com.cefetmg.mmc.mrcpsp.process

import java.util.List

import com.cefetmg.mmc.mrcpsp.model.main.Job
import com.cefetmg.mmc.mrcpsp.model.main.Mode
import com.cefetmg.mmc.mrcpsp.model.main.Project
import com.cefetmg.mmc.mrcpsp.model.main.ResourceAvailabilities

interface RestrictionsProcessor {
	
	/**
	 * Checks if there is some NON RENEWABLE resources to be used.
	 * If not, then the scheduled is broken
	 * @return
	 */
	abstract boolean checkNonRenewableResourcesAmount(Project project)
	
	/**
	 * Setting the RENEWABLE resources amount usage
	 * @param ra
	 * @param mode
	 * @return
	 */	
	abstract boolean setRenewableResourcesConsumedAmount(ResourceAvailabilities ra, Mode mode, Integer Operation)
	
	/**
	 * Should find a unique job that freeing its resources is enough to add the new job in the scheduling.
	 * @param ra
	 * @param jobs
	 * @param jobToScheduled
	 * @return
	 */
	abstract Job getBestJobToRemoveRR(ResourceAvailabilities ra, List<Job> jobs, Job jobToScheduled)
}
