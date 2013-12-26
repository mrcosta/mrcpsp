package com.cefetmg.mmc.mrcpsp.process

import com.cefetmg.mmc.mrcpsp.model.main.Project

interface ResultsProcessor {
	
	abstract void checkExecutionTypeToGenerateResults(Project project)
	
	abstract void writeResultsToOneInstance(Project project)
	
	abstract boolean getMakespanFromScheduledJobs(Project project, boolean success)
	
	abstract void checkLowerMakespan(Project project)
	
	abstract void writeLowerMakespanToOneInstance(String fileName)
	
	abstract void writeLowerMakespanToAllInstances(String fileName)
	
	abstract void writeRunningTimeToResultFile(String data, String fileName)
	
	abstract void writeRunningTimeToResultFileAllInstances(String data)
	
	abstract void writeResultsAllFile(Project project)
	
	abstract void setLowerMakespan(Project lowerMakespan)
	
	abstract Project getLowerMakespan()
}
