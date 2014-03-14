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
public final class FileUtils {
	
	private static final Logger log = Logger.getLogger(FileUtils.class);

	public Project loadInstanceInformation(String fileName) {
		Project project = new Project();
		
		// load the file and pass to the other methods
		File file = loadFile(fileName);
		
		System.out.println("\n");
		log.info("-----Instance Information-----");
		log.info("File name: " + file.getName());
		
		// file name
		project.setFileName(file.getName());
		
		// instance information
		project.setInstanceInformation(readInstanceInformation(file));
		
		// project information
		project.setProjectInformation(readProjectInformation(file));
		
		// jobs information
		project.setJobs(readJobsInformation(file, project.getInstanceInformation()));
		
		// jobs information + modes
		project.setJobs(readModesInformation(project.getJobs(), file, project.getInstanceInformation()));
		
		// jobs information + predecessors
		project.setJobs(readPredecessorsInformation(project.getJobs()));
		
		// resource availabilities
		project.setResourceAvailabilities(readResourceAvailabilities(file, project.getInstanceInformation()));
		
		return project;
	}
	
	/**
	 * Get the instance information from the test file.
	 * @param file
	 * @return
	 */
	private InstanceInformation readInstanceInformation(File file) {
		InstanceInformation instanceInformation = new InstanceInformation();		
		
		// initial value random generator
		String initialValueRandomGenerator = getLine(file, 2).split(": ")[1];
		instanceInformation.setInitialValueRandomGenerator(initialValueRandomGenerator);
		
		// jobs (incl. supersource/sink )
		Integer jobsAmount = Integer.parseInt(getLine(file, 5).split(":  ")[1]);
		instanceInformation.setJobsAmount(jobsAmount);
		log.info("1- Jobs Amount: " + jobsAmount);
		
		// horizon
		Integer horizon = Integer.parseInt(getLine(file, 6).split(":  ")[1]);
		instanceInformation.setHorizon(horizon);
		
		// renewable
		String values[] = getLine(file, 8).replaceAll("\\s+", " ").trim().split(" ");
		Integer renewableAmount = Integer.parseInt(values[3]);
		instanceInformation.setRenewableAmount(renewableAmount);
		log.info("2- Jobs Renewable Amount: " + renewableAmount);
		
		// nonrenewable
		values = getLine(file, 9).replaceAll("\\s+", " ").trim().split(" ");
		Integer nonRenewableAmount = Integer.parseInt(values[3]);
		instanceInformation.setNonRenewableAmount(nonRenewableAmount);
		log.info("3- Jobs Non Renewable Amount: " + nonRenewableAmount);		
		
		return instanceInformation;
	}
	
	/**
	 * Get the project information from the test file
	 * @param file
	 * @return
	 */
	private ProjectInformation readProjectInformation(File file) {
		ProjectInformation projectInformation = new ProjectInformation();
		String values[] = getLine(file, 14).replaceAll("\\s+", " ").trim().split(" ");
		
		Integer jobsAmount = Integer.parseInt(values[1]);
		projectInformation.setJobsAmount(jobsAmount);
		
		Integer releaseDate = Integer.parseInt(values[2]);
		projectInformation.setReleaseDate(releaseDate);
				
		Integer dueDate = Integer.parseInt(values[3]);
		projectInformation.setDueDate(dueDate);
		log.info("4- Due Date: " + dueDate);
		System.out.println("\n");
		
		Integer tardinessCost = Integer.parseInt(values[4]);
		projectInformation.setTardinessCost(tardinessCost);
		
		return projectInformation;
	}
	
	/**
	 * get the jobs information
	 * @param file
	 * @param instanceInformation
	 * @return
	 */
	private List<Job> readJobsInformation(File file, InstanceInformation instanceInformation) {
		List<Job> jobs = new ArrayList<Job>();
		Job job;
		Integer count = UrlUtils.getInstance().getStartLineJobs();
		String values[];
		
		for (int i = 0; i < instanceInformation.getJobsAmount(); i++, count++) {
			job = new Job();
			values = getLine(file, count).replaceAll("\\s+", " ").trim().split(" ");
			
			// job ID
			Integer id = Integer.parseInt(values[0]);
			job.setId(id);

			// modes amount
			Integer modesAmount = Integer.parseInt(values[1]);
			job.setModesAmount(modesAmount);
			
			// successors amount
			Integer successorsAmount = Integer.parseInt(values[2]);
			job.setSuccessorsAmount(successorsAmount);
			
			job.setSuccessors(getJobSuccessors(values));
			
			jobs.add(job);
		}
		
		return jobs;
	}
	
	private List<Integer> getJobSuccessors(String values[]) {
		List<Integer> successors = new ArrayList<Integer>();		
		
		// means that this specific job has successors
		if (values.length > 3) {			
			for (int i = 3; i < values.length; i++) {
				Integer successor = Integer.parseInt(values[i]);
				successors.add(successor);
			}			
		}
		
		return successors;
	}
	
	private List<Job> readModesInformation(List<Job> jobs, File file, InstanceInformation instanceInformation) {
		Integer startLineModes = UrlUtils.getInstance().getStartLineModes();
		
		for (Job job : jobs) {			
			job.setAvailableModes(getJobModes(file, job, startLineModes, instanceInformation));
			
			startLineModes+= job.getModesAmount();
		}
		
		return jobs;
	}
	
	private List<Mode> getJobModes(File file, Job job, Integer starLineModes, InstanceInformation instanceInformation) {
		List<Mode> modes = new ArrayList<Mode>();		
		String values[];
		
		for (int i = 0; i < job.getModesAmount(); i++, starLineModes++) {
			values = getLine(file, starLineModes).replaceAll("\\s+", " ").trim().split(" ");
			
			modes.add(createModeFromLineValues(instanceInformation, values));
		}
		
		return modes;
	}
	
	private Mode createModeFromLineValues(InstanceInformation instanceInformation, String values[]) {
		Mode mode = new Mode();
		int modeValues[] = new int[PropertyConstants.PREFIX_AMOUNT_MODES_SIZE + instanceInformation.getRenewableAmount() + instanceInformation.getNonRenewableAmount()];
		Integer countValuesPosition = 0;
		
		if (values.length == PropertyConstants.LINE_MODES_SIZE_WITH_ID) {
			modeValues = getModeValues(1, modeValues.length, values);
		} else {
			modeValues = getModeValues(0, modeValues.length, values);
		}
		
		// mode id
		Integer id = modeValues[0];
		mode.setId(id);
		countValuesPosition++;

		// mode's duration
		Integer duration = modeValues[1];
		mode.setDuration(duration);
		countValuesPosition++;
		
		// amount and sum renewable
		List<Integer> renewable = new ArrayList<Integer>();
		Integer amountRenewable = 0;
		for (int i = 0; i < instanceInformation.getRenewableAmount(); i++, countValuesPosition++) {
			renewable.add(modeValues[countValuesPosition]);
			amountRenewable+= modeValues[countValuesPosition];
		}
		mode.setRenewable(renewable);
		mode.setAmountRenewable(amountRenewable);
		
		// amount and sum nonRenewable
		List<Integer> nonRenewable = new ArrayList<Integer>();
		Integer amountNonRenewable = 0;
		for (int i = 0; i < instanceInformation.getNonRenewableAmount(); i++, countValuesPosition++) {
			nonRenewable.add(modeValues[countValuesPosition]);
			amountNonRenewable+= modeValues[countValuesPosition];
		}
		mode.setNonRenewable(nonRenewable);		
		mode.setAmountNonRenewable(amountNonRenewable);
		
		// sum renewable + nonRenewable
		mode.setSumResources(amountRenewable + amountNonRenewable);
		
		return mode;
	}
	
	private int[] getModeValues(Integer valuesStartPosition, Integer modeValuesLenght, String values[]) {
		int modeValues[] = new int[modeValuesLenght];
		
		for (int i = 0; i < modeValues.length; i++, valuesStartPosition++) {
			modeValues[i] = Integer.parseInt(values[valuesStartPosition]);
		}
		
		return modeValues;
	}
	
	private ResourceAvailabilities readResourceAvailabilities(File file, InstanceInformation instanceInformation) {
		ResourceAvailabilities resourceAvailabilities = new ResourceAvailabilities();
		Integer numberLineRA = UrlUtils.getInstance().getStartLineResourceAvailabilities();	
		String values[] = getLine(file, numberLineRA).replaceAll("\\s+", " ").trim().split(" ");
		Integer count = 0;
				
		log.info("-----Resources Avaialabilities-----");
		
		for (int i = 0; i < instanceInformation.getRenewableAmount(); i++) {
			Integer renewableAmount = Integer.parseInt(values[count]);
			resourceAvailabilities.getRenewableInitialAmount().add(renewableAmount);
			resourceAvailabilities.getRenewableConsumedAmount().add(0);
			resourceAvailabilities.getRemainingRenewableAmount().add(renewableAmount);
			
			log.info("Resource R" + (i + 1) +  ": "  + renewableAmount);
			
			count++;
		}
		
		for (int i = 0; i < instanceInformation.getNonRenewableAmount(); i++) {
			Integer nonRenewableAmount = Integer.parseInt(values[count]);
			resourceAvailabilities.getNonRenewableInitialAmount().add(nonRenewableAmount);
			resourceAvailabilities.getNonRenewableConsumedAmount().add(0);
			resourceAvailabilities.getRemainingNonRenewableAmount().add(nonRenewableAmount);
			
			log.info("Resource N" + (i + 1) +  ": "  + nonRenewableAmount);
			
			count++;
		}
		
		return resourceAvailabilities;
	}
	
	private List<Job> readPredecessorsInformation(List<Job> jobs) {
		List<Integer> predecessors;
		
		// for each job
		for (Job job : jobs) {			
			predecessors = new ArrayList<Integer>();
			
			// looping on the previous jobs.
			// -2 because the id starts with 1 and eliminating the search of the job whit the same id of the loop
			for (int i = (job.getId() - 2); i >= 0; i--) {
				
				// if the successors list of the previous jobs contains the ID of this specific job
				if (jobs.get(i).getSuccessors().contains(job.getId())) {
					job.setPredecessorsAmount(job.getPredecessorsAmount() + 1);
					predecessors.add(jobs.get(i).getId());
				}				
			}
			
			job.setPredecessors(predecessors);
			log.info(job.toString());
			
			for (Mode mode : job.getAvailableModes()) {
				log.info(job.toStringAvailableModes(mode));				
			}
			System.out.println("\n");			
		}		
		
		return jobs;
	}
	
	public File loadFile(String fileName) {
		String filePath = UrlUtils.getInstance().getUrlForInstanceFile();
		return new File(filePath + fileName);		
	}
	
	private String getLine(File file, Integer lineNumber) {
		String lineContent;
		
		try {
			lineContent = (String) org.apache.commons.io.FileUtils.readLines(file).get(lineNumber);
			return lineContent;
		} catch (IOException e) {
            log.error("Some problem with the file: " + file.getAbsolutePath(), e);
			return null;
		}
	}
	
	public static String createDirectory(String path) {
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
	
	public static void writeToFile(File file, String data, boolean append) {
        try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, data, append);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
    }
	
	public static void removeAllFilesFromFolder(String folderPath) {
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
	
	public static List<File> getAllFilesInstances() {
		File baseDir = new File(UrlUtils.getInstance().getUrlForInstance());
		List<File> files = new ArrayList<File>();

        // adding the folders of the root folder
        for (File f: baseDir.listFiles()) {        	
        	files.add(f);
        }
        Collections.sort(files);
        return files;
	}
	
}
