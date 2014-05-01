package mrcpsp.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import mrcpsp.model.instance.InstanceInformation;
import mrcpsp.model.instance.ProjectInformation;
import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.Project;
import mrcpsp.model.main.ResourceAvailabilities;

/**
 * @author mrcosta
 *
 */
final class FileUtils {
	
	static final Logger log = Logger.getLogger(FileUtils.class);

	Project loadInstanceInformation(String fileName) {
		Project project = new Project()
		
		// load the file and pass to the other methods
		File file = loadFile(fileName)
		
		System.out.println("\n")
		log.info("-----Instance Information-----")
		log.info("File name: " + file.name)
		
		// file name
		project.fileName = file.name
		
		// instance information
		project.instanceInformation = readInstanceInformation(file)
		
		// project information
		project.projectInformation = readProjectInformation(file)
		
		// jobs information
		project.jobs = readJobsInformation(file, project.instanceInformation)
		
		// jobs information + modes
		project.jobs = readModesInformation(project.jobs, file, project.instanceInformation)
		
		// jobs information + predecessors
		project.jobs = readPredecessorsInformation(project.jobs)
		
		// resource availabilities
		project.resourceAvailabilities = readResourceAvailabilities(file, project.instanceInformation)
		
		return project;
	}
	
	/**
	 * Get the instance information from the test file.
	 * @param file
	 * @return
	 */
	InstanceInformation readInstanceInformation(File file) {
		InstanceInformation instanceInformation = new InstanceInformation()
		
		// initial value random generator
		String initialValueRandomGenerator = getLine(file, 2).split(": ")[1]
		instanceInformation.initialValueRandomGenerator = initialValueRandomGenerator
		
		// jobs (incl. supersource/sink )
		Integer jobsAmount = Integer.parseInt(getLine(file, 5).split(":  ")[1])
		instanceInformation.jobsAmount = jobsAmount
		log.info("1- Jobs Amount: " + jobsAmount)
		
		// horizon
		Integer horizon = Integer.parseInt(getLine(file, 6).split(":  ")[1]);
		instanceInformation.horizon = horizon
		
		// renewable
		def values = getLine(file, 8).replaceAll("\\s+", " ").trim().split(" ");
		Integer renewableAmount = Integer.parseInt(values[3]);
		instanceInformation.renewableAmount = renewableAmount
		log.info("2- Jobs Renewable Amount: " + renewableAmount);
		
		// nonrenewable
		values = getLine(file, 9).replaceAll("\\s+", " ").trim().split(" ");
		Integer nonRenewableAmount = Integer.parseInt(values[3]);
		instanceInformation.nonRenewableAmount = nonRenewableAmount
		log.info("3- Jobs Non Renewable Amount: " + nonRenewableAmount);		
		
		return instanceInformation;
	}
	
	/**
	 * Get the project information from the test file
	 * @param file
	 * @return
	 */
	ProjectInformation readProjectInformation(File file) {
		ProjectInformation projectInformation = new ProjectInformation();
		def values = getLine(file, 14).replaceAll("\\s+", " ").trim().split(" ");
		
		Integer jobsAmount = Integer.parseInt(values[1])
		projectInformation.jobsAmount = jobsAmount
		
		Integer releaseDate = Integer.parseInt(values[2]);
		projectInformation.releaseDate = releaseDate
				
		Integer dueDate = Integer.parseInt(values[3]);
		projectInformation.dueDate = dueDate
		log.info("4- Due Date: " + dueDate)
		System.out.println("\n")
		
		Integer tardinessCost = Integer.parseInt(values[4]);
		projectInformation.tardinessCost = tardinessCost
		
		return projectInformation;
	}
	
	/**
	 * get the jobs information
	 * @param file
	 * @param instanceInformation
	 * @return
	 */
	List<Job> readJobsInformation(File file, InstanceInformation instanceInformation) {
		List<Job> jobs = new ArrayList<Job>();
		Job job;
		Integer count = UrlUtils.instance.startLineJobs
		def values
		
		for (int i = 0; i < instanceInformation.jobsAmount; i++) {
			job = new Job()
			values = getLine(file, count).replaceAll("\\s+", " ").trim().split(" ")
			
			// job ID
			Integer id = Integer.parseInt(values[0]);
			job.id = id

			// modes amount
			Integer modesAmount = Integer.parseInt(values[1]);
			job.modesAmount = modesAmount
			
			// successors amount
			Integer successorsAmount = Integer.parseInt(values[2]);
			job.successorsAmount = successorsAmount
			
			job.successors = getJobSuccessors(values)
			
			jobs.add(job)

            count++
		}
		
		return jobs;
	}
	
	List<Integer> getJobSuccessors(def values) {
		List<Integer> successors = new ArrayList<Integer>();		
		
		// means that this specific job has successors
		if (values.length > 3) {			
			for (int i = 3; i < values.length; i++) {
				Integer successor = Integer.parseInt(values[i])
				successors.add(successor)
			}			
		}
		
		return successors;
	}
	
	List<Job> readModesInformation(List<Job> jobs, File file, InstanceInformation instanceInformation) {
		Integer startLineModes = UrlUtils.instance.startLineModes
		
		jobs.each { job ->
			job.availableModes = getJobModes(file, job, startLineModes, instanceInformation)
			
			startLineModes+= job.modesAmount
		}
		
		return jobs;
	}
	
	List<Mode> getJobModes(File file, Job job, Integer starLineModes, InstanceInformation instanceInformation) {
		List<Mode> modes = new ArrayList<Mode>();		
		def values;
		
		for (int i = 0; i < job.modesAmount; i++) {
			values = getLine(file, starLineModes).replaceAll("\\s+", " ").trim().split(" ")
			
			modes.add(createModeFromLineValues(instanceInformation, values, job))
            starLineModes++
		}
		
		return modes
	}
	
	Mode createModeFromLineValues(InstanceInformation instanceInformation, def values, Job job) {
		Mode mode = new Mode();
        mode.jobId = job.id
		def modeValues = new int[PropertyConstants.PREFIX_AMOUNT_MODES_SIZE + instanceInformation.renewableAmount + instanceInformation.nonRenewableAmount];
		Integer countValuesPosition = 0;
		
		if (values.length == PropertyConstants.LINE_MODES_SIZE_WITH_ID) {
			modeValues = getModeValues(1, modeValues.length, values);
		} else {
			modeValues = getModeValues(0, modeValues.length, values);
		}
		
		// mode id
		Integer id = modeValues[0];
		mode.id = id
		countValuesPosition++;

		// mode's duration
		Integer duration = modeValues[1];
		mode.duration = duration
		countValuesPosition++;
		
		// amount and sum renewable
		List<Integer> renewable = new ArrayList<Integer>();
		Integer amountRenewable = 0;
		for (int i = 0; i < instanceInformation.renewableAmount; i++) {
			renewable.add(modeValues[countValuesPosition]);
			amountRenewable+= modeValues[countValuesPosition];
            countValuesPosition++
		}
		mode.renewable = renewable
		mode.amountRenewable = amountRenewable
		
		// amount and sum nonRenewable
		List<Integer> nonRenewable = new ArrayList<Integer>();
		Integer amountNonRenewable = 0;
		for (int i = 0; i < instanceInformation.nonRenewableAmount; i++) {
			nonRenewable.add(modeValues[countValuesPosition]);
			amountNonRenewable+= modeValues[countValuesPosition];
            countValuesPosition++
		}
		mode.nonRenewable = nonRenewable
		mode.amountNonRenewable = amountNonRenewable
		
		// sum renewable + nonRenewable
		mode.sumResources = amountRenewable + amountNonRenewable
		
		return mode;
	}
	
	int[] getModeValues(Integer valuesStartPosition, Integer modeValuesLenght, def values) {
		def modeValues = new int[modeValuesLenght];
		
		for (int i = 0; i < modeValues.length; i++) {
			modeValues[i] = Integer.parseInt(values[valuesStartPosition]);
            valuesStartPosition++
		}
		
		return modeValues;
	}
	
	ResourceAvailabilities readResourceAvailabilities(File file, InstanceInformation instanceInformation) {
		ResourceAvailabilities resourceAvailabilities = new ResourceAvailabilities();
		Integer numberLineRA = UrlUtils.instance.startLineResourceAvailabilities
		def values = getLine(file, numberLineRA).replaceAll("\\s+", " ").trim().split(" ");
		Integer count = 0;
				
		log.info("-----Resources Avaialabilities-----");
		
		for (int i = 0; i < instanceInformation.renewableAmount; i++) {
			Integer renewableAmount = Integer.parseInt(values[count]);
			resourceAvailabilities.renewableInitialAmount.add(renewableAmount);
			resourceAvailabilities.renewableConsumedAmount.add(0);
			resourceAvailabilities.remainingRenewableAmount.add(renewableAmount);
			
			log.info("Resource R" + (i + 1) +  ": "  + renewableAmount);
			
			count++;
		}
		
		for (int i = 0; i < instanceInformation.nonRenewableAmount; i++) {
			Integer nonRenewableAmount = Integer.parseInt(values[count]);
			resourceAvailabilities.nonRenewableInitialAmount.add(nonRenewableAmount);
			resourceAvailabilities.nonRenewableConsumedAmount.add(0);
			resourceAvailabilities.remainingNonRenewableAmount.add(nonRenewableAmount);
			
			log.info("Resource N" + (i + 1) +  ": "  + nonRenewableAmount);
			
			count++;
		}
		
		return resourceAvailabilities;
	}
	
	List<Job> readPredecessorsInformation(List<Job> jobs) {
		List<Integer> predecessors;
		
		// for each job
		for (Job job : jobs) {			
			predecessors = new ArrayList<Integer>();
			
			// looping on the previous jobs.
			// -2 because the id starts with 1 and eliminating the search of the job whit the same id of the loop
			for (int i = (job.id - 2); i >= 0; i--) {
				
				// if the successors list of the previous jobs contains the ID of this specific job
				if (jobs[i].successors.contains(job.id)) {
					job.predecessorsAmount = job.predecessorsAmount + 1
					predecessors.add(jobs[i].id)
				}				
			}
			
			job.predecessors = predecessors
			log.info(job.toString());
			
			for (Mode mode : job.availableModes) {
				log.info(job.toStringAvailableModes(mode))
			}
			System.out.println("\n");			
		}		
		
		return jobs;
	}
	
	File loadFile(String fileName) {
		String filePath = UrlUtils.instance.urlForInstanceFile
		return new File(filePath + fileName)
	}
	
	String getLine(File file, Integer lineNumber) {
		String lineContent
		
		try {
			lineContent = (String) org.apache.commons.io.FileUtils.readLines(file).get(lineNumber);
			return lineContent;
		} catch (IOException e) {
            log.error("Some problem with the file: " + file.getAbsolutePath(), e);
			return null;
		}
	}
	
	static String createDirectory(String path) {
        File f = new File(path);
        
        if (!f.exists()) {
            try {
				org.apache.commons.io.FileUtils.forceMkdir(f);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
        }
        
        return f.getAbsolutePath();
    }
	
	static void writeToFile(File file, String data, boolean append) {
        try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, data, append);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
    }
	
	static void removeAllFilesFromFolder(String folderPath) {
		File f = new File(folderPath);
        
		if (f.exists()) {
        
            try {
				org.apache.commons.io.FileUtils.cleanDirectory(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
        }
	}
	
	static List<File> getAllFilesInstances() {
		File baseDir = new File(UrlUtils.instance.urlForInstance);
		List<File> files = new ArrayList<File>();

        // adding the folders of the root folder
        for (File f: baseDir.listFiles()) {        	
        	files.add(f);
        }
        Collections.sort(files);
        return files;
	}
}
