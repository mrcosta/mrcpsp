package com.cefetmg.mmc.mrcpsp.process.impl

import java.io.File

import org.apache.commons.lang.StringUtils

import com.cefetmg.mmc.mrcpsp.model.enums.EnumExecutionTypes
import com.cefetmg.mmc.mrcpsp.model.main.Project
import com.cefetmg.mmc.mrcpsp.process.ResultsProcessor
import com.cefetmg.mmc.mrcpsp.utils.FileUtils
import com.cefetmg.mmc.mrcpsp.utils.LogUtils
import com.cefetmg.mmc.mrcpsp.utils.PropertyConstants
import com.cefetmg.mmc.mrcpsp.utils.PropertyManager
import com.cefetmg.mmc.mrcpsp.utils.UrlUtils

public class ResultsProcessorImpl implements ResultsProcessor {
	
	private Project lowerProjectMakespan	
		
	@Override
	public void writeResultsToOneInstance(Project project) {		
		String pathFile = UrlUtils.instance.getUrlForResultsFileToOneInstance(project.fileName)
		boolean append = true
		String data = project.fileName +  " MAKESPAN: " + Integer.toString(project.makespan) + LogUtils.getINSTANCE_STATUS() + "\n"
		
		FileUtils.writeToFile(new File(pathFile), data, append)
	}

	@Override
	def boolean getMakespanFromScheduledJobs(Project project, boolean success) {		
		if (success) {
			Integer lastIndex = project.staggeredJobs.size() - 1
			Integer makespan = project.staggeredJobs.get(lastIndex).getEndTime()
			
			project.makespan = makespan
			success
		} else {			
			LogUtils.setINSTANCE_STATUS(" - Check results.")
			project.instanceResultStatus = " - Check results."
			project.makespan = PropertyConstants.INSTANCE_MAKESPAN_ERROR
			false
		}
	}

	@Override
	public void checkLowerMakespan(Project project) {
		if (lowerProjectMakespan == null || lowerProjectMakespan.makespan == PropertyConstants.INSTANCE_MAKESPAN_ERROR) {
			lowerProjectMakespan = project
		} else {			
			if (project.makespan < lowerProjectMakespan.makespan) {
				lowerProjectMakespan = project
			}
		}
	}

	@Override
	public void writeLowerMakespanToOneInstance(String fileName) {
		String pathFile = UrlUtils.getInstance().getUrlForResultsFileToOneInstance(fileName)
		boolean append = true
		String data = PropertyManager.instance.getProperty(PropertyConstants.INSTANCE_FILE) + " LOWER MAKESPAN: " + Integer.toString(lowerProjectMakespan.makespan) + LogUtils.getINSTANCE_STATUS() + "\n"
		
		FileUtils.writeToFile(new File(pathFile), data, append)		
	}

	@Override
	public void writeRunningTimeToResultFile(String data, String fileName) {
		String pathFile = UrlUtils.instance.getUrlForResultsFileToOneInstance(fileName)
		boolean append = true
		
		FileUtils.writeToFile(new File(pathFile), data, append)	
	}

	@Override
	public void checkExecutionTypeToGenerateResults(Project project) {
		String executionType = UrlUtils.instance.executionType
				
		if (StringUtils.equals(executionType, EnumExecutionTypes.ONE_FILE.getName())) {		
			writeResultsToOneInstance(project)
		} else if (StringUtils.equals(executionType, EnumExecutionTypes.ALL.getName())) {
			writeResultsAllFile(project)
		} 
		
	}

	@Override
	public void writeResultsAllFile(Project project) {
		String pathFile = UrlUtils.instance.urlForResultsFileToAllInstances
		boolean append = true
		String data = project.fileName +  " MAKESPAN: " + Integer.toString(project.makespan) + project.instanceResultStatus + "\n"
		
		FileUtils.writeToFile(new File(pathFile), data, append)		
	}

	@Override
	public void writeRunningTimeToResultFileAllInstances(String data) {
		String pathFile = UrlUtils.instance.urlForResultsFileToAllInstances
		boolean append = true
		
		FileUtils.writeToFile(new File(pathFile), data, append)		
	}

	@Override
	public void writeLowerMakespanToAllInstances(String fileName) {
		String pathFile = UrlUtils.instance.urlForResultsFileToAllInstances
		boolean append = true
		String instanceResultStatus = ""
		
		if (lowerProjectMakespan.makespan == PropertyConstants.INSTANCE_MAKESPAN_ERROR) {
			instanceResultStatus = " - Check results."
		}
		
		String data = fileName + " LOWER MAKESPAN: " + Integer.toString(lowerProjectMakespan.makespan) + instanceResultStatus + "\n"
		
		FileUtils.writeToFile(new File(pathFile), data, append)			
	}
	
	@Override
	public void setLowerMakespan(Project lowerProjectMakespan) {
		this.lowerProjectMakespan = lowerProjectMakespan
	}
	
	@Override
	public Project getLowerMakespan() {
		return this.lowerProjectMakespan
	}
}
