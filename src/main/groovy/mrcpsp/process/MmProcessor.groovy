package mrcpsp.process

import mrcpsp.model.main.Project
import mrcpsp.process.initialsolution.GenerateInitialSolutionGRASP
import mrcpsp.process.localsearch.LocalSearch
import mrcpsp.utils.ChronoWatch
import org.apache.log4j.Logger

class MmProcessor {
	
	static final Logger log = Logger.getLogger(MmProcessor.class)
	
	InstanceDataProcessor instanceDataProcessor
	GenerateInitialSolutionGRASP generateInitialSolutionGRASP
	JobTimeProcessor jobTimeProcessor
	LocalSearch localSearch

    Project project
	
	boolean success

    MmProcessor() {
		init()
	}
	
	void init() {
		log.info("Starting the execution . . .")		
		success = true
	}
	
	private boolean executeGetInstanceData(fileName) {
		if (success) {			
			try {	
				log.info("Loading instance's data . . .")
				instanceDataProcessor = new InstanceDataProcessor()
				project = instanceDataProcessor.getInstanceData(fileName)
				success = true
			} catch (Exception e) {
                log.error("Exception during the executeGetInstanceData phase", e)
				success = false
			}			
		}		
			
		success
	}
	
	private boolean executeGenerateInitialSolution() {
		if (success) {			
			try {
				log.info("Generating the Initial Solution with GRASP . . .")
				
				generateInitialSolutionGRASP = new GenerateInitialSolutionGRASP()
                project.staggeredJobsId = generateInitialSolutionGRASP.getInitialSolution(project)

				success = true
			} catch (Exception e) {
                log.error("Exception during the executeGenerateInitialSolution phase", e)
				success = false
			}			
		}
				
		success
	}
	
	boolean executeGetJobTimesAndSetMakespan() {
		if (success) {			
			try {	
				log.info("Getting Initial and Finish Time for Jobs . . .")
				
				jobTimeProcessor = new JobTimeProcessor()
				project.times = jobTimeProcessor.getJobTimes(project)
                project.makespan = project.jobs.last().endTime
			} catch (Exception e) {
                log.error("Exception during the executeGetJobTimes phase", e)
				return success = false
			}			
		}	
			
		success
	}
	
	private boolean executeLocalSearch() {		
		if (success) {			
			try {	
				log.info("Executing Local Search . . .")

                ChronoWatch.instance.startSolutionTime()
				localSearch = new LocalSearch()
				project = localSearch.executeLocalSearch(project)
                /**
                 * Finish here, if the user passed the "perturbation"==0
                 */
                ChronoWatch.instance.pauseSolutionTime()
                project.totalTimeSolutionFormated = ChronoWatch.instance.totalTimeSolutionFormated

				if (project) {
					success = true
				} else {
					success = false
				}
				
			} catch (Exception e) {
                log.error("Exception during the executeLocalSearch phase", e)
				return success = false
			}			
		}	
		success
	}

    def Project localSearchDescentUphillMethod() {
        if (success) {
            success = executeLocalSearch()

            return project
        } else {
            log.info("Some problem was previously found. Local Search won't be executed. . .")
        }
    }

    def basicOperationsInstance(String fileName) {
        success = true

        // reading the file information and creating the objects
        success = executeGetInstanceData(fileName)
    }
	
	def Project initialSolutionWithGrasp() {
        // backing the non renewable consumed amount to the first set of modes
        project.resourceAvailabilities.backNonRenewableConsumedAndRemainingAmountToOriginal()

		// generating the initial solution
        ChronoWatch.instance.startSolutionTime()
		success = executeGenerateInitialSolution()
        ChronoWatch.instance.pauseSolutionTime()

        ChronoWatch.instance.startSolutionTime()
        //getting initial and finish time for jobs and checking the R resources restrictions
        success = executeGetJobTimesAndSetMakespan()
        ChronoWatch.instance.pauseSolutionTime()

		return project		
	}
	
}
