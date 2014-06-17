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
	ResultsProcessor resultsProcessor
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
				log.info("==========================================================================")
				log.info("Loading instance's data . . .")
				instanceDataProcessor = new InstanceDataProcessor()
				project = instanceDataProcessor.getInstanceData(fileName)
				success = true
				log.info("Loading instance's data . . . DONE \n")	
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
				log.info("==========================================================================")
				log.info("Generating the Initial Solution with GRASP . . .")
				
				generateInitialSolutionGRASP = new GenerateInitialSolutionGRASP()
                project.staggeredJobsId = generateInitialSolutionGRASP.getInitialSolution(project)

				success = true
				log.info("Generating the Initial Solution with GRASP . . .DONE \n")
			} catch (Exception e) {
                log.error("Exception during the executeGenerateInitialSolution phase", e)
				success = false
			}			
		}
				
		success
	}
	
	boolean executeGetJobTimes() {
		if (success) {			
			try {	
				log.info("==========================================================================")
				log.info("Getting Initial and Finish Time for Jobs . . .")
				
				jobTimeProcessor = new JobTimeProcessor()
				success = jobTimeProcessor.getJobTimes(project.getResourceAvailabilities(), project.getStaggeredJobs())
				log.info("Getting Initial and Finish Time for Jobs . . .DONE \n")
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
				log.info("==========================================================================")
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
				
				log.info("Executing Local Search . . .DONE \n")	
			} catch (Exception e) {
                log.error("Exception during the executeLocalSearch phase", e)
				return success = false
			}			
		}	
		success
	}
	
	public boolean setProjectMakespan() {
		try {
			log.info("==========================================================================")
			log.info("Setting the project makespan. . .")
			
			resultsProcessor = new ResultsProcessor()
			success = resultsProcessor.getMakespanFromScheduledJobs(project, success)
			log.info("FILE: " + project.getFileName() + " - MAKESPAN: " + project.getMakespan())
						
			log.info("Setting the project makespan. . .DONE \n")
			true
		} catch (Exception e) {
            log.error("Exception during the setProjectMakespan phase", e)
		}

        success
	}

    def Project localSearchDescentUphillMethod() {
        if (success) {
            success = executeLocalSearch()

            return project
        } else {
            log.info("==========================================================================")
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
        success = executeGetJobTimes()
        ChronoWatch.instance.pauseSolutionTime()

        // setting the project makespan
        success = setProjectMakespan()

		return project		
	}
	
}
