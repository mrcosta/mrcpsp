package mrcpsp.process

import org.apache.commons.lang.StringUtils

import mrcpsp.model.enums.EnumExecutionTypes
import mrcpsp.model.main.Project

import mrcpsp.utils.FileUtils
import mrcpsp.utils.LogUtils
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.PropertyManager
import mrcpsp.utils.UrlUtils

public class ResultsProcessor {
	
	private Project lowerProjectMakespan	
		
	public void writeResultsToOneInstance(Project project) {
		String pathFile = UrlUtils.instance.getUrlForResultsFileToOneInstance(project.fileName)
		boolean append = true
		String data = project.fileName +  " MAKESPAN: " + Integer.toString(project.makespan) + LogUtils.getINSTANCE_STATUS() + "\n"
		
		FileUtils.writeToFile(new File(pathFile), data, append)
	}

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

	public void checkLowerMakespan(Project project) {
		if (lowerProjectMakespan == null || lowerProjectMakespan.makespan == PropertyConstants.INSTANCE_MAKESPAN_ERROR) {
			lowerProjectMakespan = project
		} else {			
			if (project.makespan < lowerProjectMakespan.makespan) {
				lowerProjectMakespan = project
			}
		}
	}

	public void writeLowerMakespanToOneInstance(String fileName) {
		String pathFile = UrlUtils.getInstance().getUrlForResultsFileToOneInstance(fileName)
		boolean append = true
		String data = PropertyManager.instance.getProperty(PropertyConstants.INSTANCE_FILE) + " LOWER MAKESPAN: " + Integer.toString(lowerProjectMakespan.makespan) + LogUtils.getINSTANCE_STATUS() + "\n"
		
		FileUtils.writeToFile(new File(pathFile), data, append)		
	}

	public void writeRunningTimeToResultFile(String data, String fileName) {
		String pathFile = UrlUtils.instance.getUrlForResultsFileToOneInstance(fileName)
		boolean append = true
		
		FileUtils.writeToFile(new File(pathFile), data, append)	
	}

	public void checkExecutionTypeToGenerateResults(Project project) {
		String executionType = UrlUtils.instance.executionType
				
		if (StringUtils.equals(executionType, EnumExecutionTypes.ONE_FILE.getName())) {		
			writeResultsToOneInstance(project)
		} else if (StringUtils.equals(executionType, EnumExecutionTypes.ALL.getName())) {
			writeResultsAllFile(project)
		} 
		
	}

	public void writeResultsAllFile(Project project) {
		String pathFile = UrlUtils.instance.urlForResultsFileToAllInstances
		boolean append = true
		String data = project.fileName +  " MAKESPAN: " + Integer.toString(project.makespan) + project.instanceResultStatus + "\n"
		
		FileUtils.writeToFile(new File(pathFile), data, append)		
	}

	public void writeRunningTimeToResultFileAllInstances(String data) {
		String pathFile = UrlUtils.instance.urlForResultsFileToAllInstances
		boolean append = true
		
		FileUtils.writeToFile(new File(pathFile), data, append)		
	}

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
	
	public void setLowerMakespan(Project lowerProjectMakespan) {
		this.lowerProjectMakespan = lowerProjectMakespan
	}
	
	public Project getLowerMakespan() {
		return this.lowerProjectMakespan
	}
}
