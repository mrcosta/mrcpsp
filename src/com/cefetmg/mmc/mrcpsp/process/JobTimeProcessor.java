package com.cefetmg.mmc.mrcpsp.process;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.model.main.ResourceAvailabilities;

public interface JobTimeProcessor {
	
	public abstract boolean getJobTimes(ResourceAvailabilities ra, List<Job> jobs);

}
