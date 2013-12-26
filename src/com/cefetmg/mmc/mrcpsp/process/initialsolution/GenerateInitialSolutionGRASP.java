package com.cefetmg.mmc.mrcpsp.process.initialsolution;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.model.main.Project;

/**
 * @author mateus
 *
 */
public interface GenerateInitialSolutionGRASP {
	
	/**
	 * Generate a initial solution using GRASP
	 */	
	public abstract List<Job> getInitialSolution(Project project);
	

}
