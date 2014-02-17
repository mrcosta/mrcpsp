package mrcpsp.process

import mrcpsp.diagram.GanttDiagram
import mrcpsp.model.enums.EnumLogUtils
import mrcpsp.model.main.Project
import mrcpsp.process.initialsolution.GenerateInitialSolutionGRASP
import mrcpsp.process.job.JobPriorityRulesOperations
import mrcpsp.process.localsearch.LocalSearch
import mrcpsp.utils.LogUtils
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Logger

/**
 * @author mrcosta
 *
 */
class MmProcessor {
	
	static final Logger log = Logger.getLogger(MmProcessor.class)
	
	InstanceDataProcessor instanceDataProcessor
	JobsModeSelectProcessor jobsModeSelectProcessor
	JobPriorityRulesOperations	jobPriorityRulesOperations
	GenerateInitialSolutionGRASP generateInitialSolutionGRASP
	JobsModeInformationProcessor jobsModeInformationProcessor
	RestrictionsProcessor restrictionsProcessor
	JobTimeProcessor jobTimeProcessor
	ResultsProcessor resultsProcessor
	LocalSearch localSearch
    GanttDiagram ganttDiagram
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
				log.error(e.toString() + " --- " + e.getMessage())
				success = false
			}			
		}		
			
		success
	}
	
	private boolean executeGetJobModesInformation() {
		if (success) {			
			try {	
				log.info("==========================================================================")
				log.info("Getting job modes information . . .")
				jobsModeInformationProcessor = new JobsModeInformationProcessor()
				project.setJobs(jobsModeInformationProcessor.getJobModesInformation(project.getJobs(), project.resourceAvailabilities))
				success = true
				log.info("Getting job modes information . . . DONE \n")	
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
				success = false
			}			
		}
			
		success
	}
	
	private boolean executeJobsPriorityRules() {
		if (success) {			
			try {
				log.info("==========================================================================")
				log.info("Executing Job Priority Rules . . .")
				
				jobPriorityRulesOperations = new JobPriorityRulesOperations()
				project.setJobs(jobPriorityRulesOperations.setJobsPriorityRuleInformation(project.getJobs()))
				success = true
				log.info("Executing Job Priority Rules . . .DONE \n")	
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
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
				project.setStaggeredJobs(generateInitialSolutionGRASP.getInitialSolution(project))
				
				log.info(LogUtils.generateJobsIDListLog(project.getJobs(), EnumLogUtils.LIST_JOBS))
				log.info(LogUtils.generateJobsIDListLog(project.getStaggeredJobs(), EnumLogUtils.STAGGERED_JOBS))
				
				success = true
				log.info("Generating the Initial Solution with GRASP . . .DONE \n")
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
				success = false
			}			
		}
				
		success
	}
	
	private boolean executeJobsModeSelect() {
		if (success) {			
			try {	
				log.info("==========================================================================")
				log.info("Executing jobs mode select . . .")
				
				jobsModeSelectProcessor = new JobsModeSelectProcessor()
				project.setStaggeredJobs(jobsModeSelectProcessor.setJobsMode(project.getJobs()))
				
				log.info(LogUtils.generateJobsModeIDListLog(project.getStaggeredJobs(), EnumLogUtils.JOBS_MODE_LIST))
				
				success = true
				log.info("Executing jobs mode select . . .DONE \n")
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
				success = false
			}			
		}	
				
		success
	}
	
	private boolean executeCheckRestrictions() {
		if (success) {			
			try {	
				log.info("==========================================================================")
				log.info("Executing Restrictions Verification . . .")
				
				restrictionsProcessor = new RestrictionsProcessor()
				success = restrictionsProcessor.checkNonRenewableResourcesAmount(project)

                if (success) {
                    log.info(LogUtils.generateJobsModeIDListLog(project.getStaggeredJobs(), EnumLogUtils.JOBS_MODE_LIST))
                }
				log.info("Executing Restrictions Verification . . .DONE \n")	
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
				return success = false
			}			
		}	
		
		success
	}
	
	private boolean executeGetJobTimes() {
		if (success) {			
			try {	
				log.info("==========================================================================")
				log.info("Getting Initial and Finish Time for Jobs . . .")
				
				jobTimeProcessor = new JobTimeProcessor()
				success = jobTimeProcessor.getJobTimes(project.getResourceAvailabilities(), project.getStaggeredJobs())
				log.info("Getting Initial and Finish Time for Jobs . . .DONE \n")	
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
				return success = false
			}			
		}	
			
		success
	}

	private boolean executeCheckRestrictionsAndGetJobTimes(Project project) {
		if (success) {			
			try {
                this.project = project

				// checking if the restrictions are OK
				success = executeCheckRestrictions()
				
				// getting initial and finish time for jobs
				success = executeGetJobTimes()					
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
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
				
				localSearch = new LocalSearch()
				project = localSearch.executeLocalSearch(project)
				
				if (project) {
					success = true
				} else {
					success = false
				}
				
				log.info("Executing Local Search . . .DONE \n")	
			} catch (Exception e) {
				log.error(e.toString() + " --- " + e.getMessage())
				return success = false
			}			
		}	
		success
	}
	
	def void executeWriteResults() {
		try {
			log.info("==========================================================================")
			log.info("Writing the results. . .")
			
			resultsProcessor.checkExecutionTypeToGenerateResults(project)
			log.info("FILE: " + project.getFileName() + " - MAKESPAN: " + project.getMakespan())
						
			log.info("Writing the results . . .DONE \n")
		} catch (Exception e) {
			log.error(e.toString() + " --- " + e.getMessage())
		}
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
			log.error(e.toString() + " --- " + e.getMessage())
		}

        success
	}

    boolean generateDiagram(Project project) {
        if (success) {
            try {
                log.info("==========================================================================")
                log.info("Generating the gantt diagram. . .")

                ganttDiagram = new GanttDiagram()
                success = ganttDiagram.generateGanttDiagram(project)

                if (success) {
                    log.info("Diagram path: " + UrlUtils.instance.diagramPath)
                } else {
                    log.info("Something went wrong generating the gantt diagram.")
                }

                log.info("Generating the gantt diagram. . .DONE")

                true
            } catch (Exception e) {
                log.error(e.toString() + " --- " + e.getMessage())
            }
        }

        success
    }

    private Project callExecuteLocalSearch() {
		// localSearch
		success = executeLocalSearch()
		
		project
	}
	
	def Project initialSolutionWithGrasp(String fileName) {
		success = true
		
		// reading the file information and creating the objects
		success = executeGetInstanceData(fileName)
		
		// excluding the dumb modes and getting some useful information about the modes of each job - priority rules will use this information
		success = executeGetJobModesInformation()

        // getting a mode for each job
        success = executeJobsModeSelect()

        // checking if the restrictions for the NR resources are OK
        success = executeCheckRestrictions()

		// some priority rules can run without runtime information update (like NIS)
		success = executeJobsPriorityRules()
		
		// generating the initial solution
		success = executeGenerateInitialSolution()

        //getting initial and finish time for jobs and checking the R resources restrictions
        success = executeGetJobTimes()
		
		// setting the project makespan
		success = setProjectMakespan()
		
		return project		
	}
	
	def Project localSearchDescentUphillMethod(Project project) {
		// to set a different project if necessary
		this.project = project
		
		callExecuteLocalSearch()
	}
	
	def Project localSearchDescentUphillMethod() {
		if (success) {
            callExecuteLocalSearch()
        } else {
            log.info("==========================================================================")
            log.info("Some problem was previously found. Local Search won't be executed. . .")
        }
	}	
	
}
