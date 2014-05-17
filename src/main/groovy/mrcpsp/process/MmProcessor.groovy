package mrcpsp.process

import mrcpsp.diagram.GanttDiagram
import mrcpsp.model.enums.EnumLogUtils
import mrcpsp.model.main.Project
import mrcpsp.process.initialsolution.GenerateInitialSolutionGRASP
import mrcpsp.process.initialsolution.LowerBoundProcessor
import mrcpsp.process.job.JobPriorityRulesOperations
import mrcpsp.process.localsearch.LocalSearch
import mrcpsp.process.perturbation.IteratedLocalSearch
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.LogUtils
import mrcpsp.utils.PropertyConstants
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
    LowerBoundProcessor lowerBoundProcessor
	LocalSearch localSearch
    GanttDiagram ganttDiagram
    IteratedLocalSearch ils

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
	
	private boolean executeGetJobModesInformation() {
		if (success) {			
			try {	
				log.info("==========================================================================")
				log.info("Getting job modes information . . .")
				jobsModeInformationProcessor = new JobsModeInformationProcessor()
				project.jobs = jobsModeInformationProcessor.getJobModesInformation(project.getJobs(), project.resourceAvailabilities)
				success = true
				log.info("Getting job modes information . . . DONE \n")	
			} catch (Exception e) {
                log.error("Exception during the executeGetJobModesInformation phase", e)
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
				project.jobs = jobPriorityRulesOperations.setJobsPriorityRuleInformation(project)
				success = true
				log.info("Executing Job Priority Rules . . .DONE \n")	
			} catch (Exception e) {
                log.error("Exception during the executeJobsPriorityRules phase", e)
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
                project.staggeredJobsModesId = generateInitialSolutionGRASP.getInitialSolution(project)

				log.info("The JOBS list has this index: $project.jobs.id")
                log.info("The STAGGERED list has this index: " + project.staggeredJobsModesId*.key)

				success = true
				log.info("Generating the Initial Solution with GRASP . . .DONE \n")
			} catch (Exception e) {
                log.error("Exception during the executeGenerateInitialSolution phase", e)
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
				project.jobs = jobsModeSelectProcessor.setJobsMode(project)
				
				log.info(LogUtils.generateJobsModeIDListLog(project.jobs, EnumLogUtils.JOBS_MODE_LIST))
				
				success = true
				log.info("Executing jobs mode select . . .DONE \n")
			} catch (Exception e) {
                log.error("Exception during the executeJobsModeSelect phase", e)
				success = false
			}			
		}	
				
		success
	}
	
	private boolean executeCheckRestrictionsAndSetOriginalNonRenewableConsumedAmount() {
		if (success) {			
			try {	
				log.info("==========================================================================")
				log.info("Executing Restrictions Verification . . .")
				
				restrictionsProcessor = new RestrictionsProcessor()
				success = restrictionsProcessor.checkAndSetTheNonRenewableResourcesAmount(project)
                project.resourceAvailabilities.setOriginalNonRenewableConsumedAndRemainingAmount()

                if (success) {
                    log.info(LogUtils.generateJobsModeIDListLog(project.jobs, EnumLogUtils.JOBS_MODE_LIST))
                }
				log.info("Executing Restrictions Verification . . .DONE \n")	
			} catch (Exception e) {
                log.error("Exception during the executeCheckRestrictions phase", e)
				return success = false
			}			
		}	
		
		success
	}

    private boolean executeGetLowerBound() {
        if (success) {
            try {
                def showLowerBound = UrlUtils.instance.showLowerBound

                if (showLowerBound == PropertyConstants.TRUE) {
                    log.info("==========================================================================")
                    log.info("Getting the solution's lower bound. . .")

                    lowerBoundProcessor = new LowerBoundProcessor()
                    success = lowerBoundProcessor.getLowerBoundFromSolution(project)
                    log.info("FILE: " + project.getFileName() + " - MAKESPAN'S LOWER BOUND: " + project.lowerBound)
                    log.info("Getting the solution's lower bound. . .DONE \n")
                }
            } catch (Exception e) {
                log.error("Exception during the executeGetLowerBound phase", e)
                return success = false
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
				success = jobTimeProcessor.getJobTimes(project)
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

    def boolean getCriticalPath() {
        if (success) {
            try {
                def showCriticalPath = UrlUtils.instance.showCriticalPath

                if (showCriticalPath == PropertyConstants.TRUE) {
                    log.info("Getting the critical path. . .")

                    success = resultsProcessor.getCriticalPath(project)
                    log.info(LogUtils.generateJobsIDListLog(project.criticalPath, EnumLogUtils.CRITICAL_PATH_JOBS))

                    log.info("Getting the critical path. . .DONE \n")
                    true
                }
            } catch (Exception e) {
                log.error("Exception during the getCriticalPath phase", e)
            }
        }
        return success
    }

    def boolean perturbation() {
        if (success) {
            try {
                log.info("Doing the perturbation. . .")

                ils = new IteratedLocalSearch()
                ChronoWatch.instance.startSolutionTime()
                project = ils.ils(project)
                /**
                 * Finish here, if the user passed the "perturbation"==1
                 */
                ChronoWatch.instance.pauseSolutionTime()
                project.totalTimeSolutionFormated = ChronoWatch.instance.totalTimeSolutionFormated

                log.info("Doing the perturbation. . .DONE \n")
                return true
            } catch (Exception e) {
                log.error("Exception during the perturbation phase", e)
            }
        }
        return success
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

               return true
            } catch (Exception e) {
                log.error("Exception during the generateDiagram phase", e)
            }
        }

        return success
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

    def setStartAndFinishJobTimes() {
        try {
            if (success) {
                project.staggeredJobsModesId.each { jobModeId ->
                    project.startJobTimes[jobModeId.key] = project.jobs[jobModeId.key - 1].startTime
                    project.finishJobTimes[jobModeId.key] = project.jobs[jobModeId.key - 1].endTime
                }
            }
        } catch (Exception e) {
            log.error("Exception during the setProjectMakespan phase", e)
        }
    }

    def basicOperationsInstance(String fileName) {
        success = true

        // reading the file information and creating the objects
        success = executeGetInstanceData(fileName)

        // excluding the dumb modes and getting some useful information about the modes of each job - priority rules will use this information
        success = executeGetJobModesInformation()

        // getting a mode for each job
        ChronoWatch.instance.startSolutionTime()
        success = executeJobsModeSelect()

        // checking if the restrictions for the NR resources are OK
        success = executeCheckRestrictionsAndSetOriginalNonRenewableConsumedAmount()
        ChronoWatch.instance.pauseSolutionTime()

        // some priority rules can run without runtime information update (like NIS)
        success = executeJobsPriorityRules()
    }
	
	def Project initialSolutionWithGrasp() {
        // backing the non renewable consumed amount to the first set of modes
        project.resourceAvailabilities.backNonRenewableConsumedAndRemainingAmountToOriginal()
        project.jobs.runningJobInformation*.restartEligibilityAndStaggeredPredecessors()

		// generating the initial solution
        ChronoWatch.instance.startSolutionTime()
		success = executeGenerateInitialSolution()
        ChronoWatch.instance.pauseSolutionTime()

        // getting the lower bound
        success = executeGetLowerBound()

        ChronoWatch.instance.startSolutionTime()
        //getting initial and finish time for jobs and checking the R resources restrictions
        success = executeGetJobTimes()
        ChronoWatch.instance.pauseSolutionTime()

        // getting the critical path
        success = getCriticalPath()

        // setting the project makespan
        success = setProjectMakespan()

        setStartAndFinishJobTimes()

		return project		
	}
	
}
