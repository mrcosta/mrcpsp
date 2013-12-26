package com.cefetmg.mmc.mrcpsp.model.main;

import java.util.List;

import com.cefetmg.mmc.mrcpsp.model.instance.InstanceInformation;
import com.cefetmg.mmc.mrcpsp.model.instance.ProjectInformation;

/**
 * @author mrcosta
 *
 */
public class Project {
	
	private InstanceInformation instanceInformation;
	private ProjectInformation projectInformation;
	private ResourceAvailabilities resourceAvailabilities;
	private List<Job> jobs;
	private List<Job> staggeredJobs;
	private Integer makespan;
	
	private String fileName;
	private String instanceResultStatus;

	public Project() {
		instanceResultStatus = "";
	}
	
	public InstanceInformation getInstanceInformation() {
		return instanceInformation;
	}

	public void setInstanceInformation(InstanceInformation instanceInformation) {
		this.instanceInformation = instanceInformation;
	}

	public ProjectInformation getProjectInformation() {
		return projectInformation;
	}

	public void setProjectInformation(ProjectInformation projectInformation) {
		this.projectInformation = projectInformation;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public ResourceAvailabilities getResourceAvailabilities() {
		return resourceAvailabilities;
	}

	public void setResourceAvailabilities(ResourceAvailabilities resourceAvailabilities) {
		this.resourceAvailabilities = resourceAvailabilities;
	}

	public List<Job> getStaggeredJobs() {
		return staggeredJobs;
	}

	public void setStaggeredJobs(List<Job> staggeredJobs) {
		this.staggeredJobs = staggeredJobs;
	}

	public Integer getMakespan() {
		return makespan;
	}

	public void setMakespan(Integer makespan) {
		this.makespan = makespan;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getInstanceResultStatus() {
		return instanceResultStatus;
	}

	public void setInstanceResultStatus(String instanceResultStatus) {
		this.instanceResultStatus = instanceResultStatus;
	}

}
