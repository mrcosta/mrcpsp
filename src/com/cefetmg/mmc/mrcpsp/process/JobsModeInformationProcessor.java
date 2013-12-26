package com.cefetmg.mmc.mrcpsp.process;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.main.Job;

public interface JobsModeInformationProcessor {
	
	public List<Job> getJobModesInformation(List<Job> jobs);

}
