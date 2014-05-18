package mrcpsp.process.localsearch

import mrcpsp.model.enums.EnumLocalSearch
import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
import mrcpsp.process.job.JobOperations
import mrcpsp.process.mode.ModeOperations
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

class LocalSearch {

	static final Logger log = Logger.getLogger(LocalSearch.class);
	
	Integer bestMakespan
    Project bestProject

	Map<Integer, Integer> bestNeighbor
    Integer bestNeighborMakespan

    Integer bestJobNeighborModification
    Integer bestModeNeighborModification

    Map<Integer, Integer> bestStartJobTimes
    Map<Integer, Integer> bestFinishJobTimes

	boolean checkSolution
	MmProcessor mmProcessor
    LowerNonRenewableConsumption lnrc
    JobOperations jobOperations
    ModeOperations modeOperations

    LocalSearch() {
		mmProcessor = new MmProcessor()
        lnrc = new LowerNonRenewableConsumption()
        jobOperations = new JobOperations()
        modeOperations = new ModeOperations()

        bestProject = [:]
	}
	
	def Project executeLocalSearch(Project project) {
		def localSearch = UrlUtils.instance.localSearch
        checkSolution = true
        bestProject = project
        bestMakespan = project.makespan

        log.info("LOCAL SEARCH: " + localSearch)
        switch (localSearch) {
            case EnumLocalSearch.LNRC.name:
                def realJobsId = jobOperations.getOnlyRealJobsId(project.staggeredJobsId, project.instanceInformation.jobsAmount)
                lowerNonRenewableComsumption(project, realJobsId)
                break
            case EnumLocalSearch.LNRCCP.name:
                def realJobsId = jobOperations.getOnlyRealJobsId(project.criticalPath, project.instanceInformation.jobsAmount)
                lowerNonRenewableComsumption(project, realJobsId)
                break
            case EnumLocalSearch.BSFM.name:
                def realJobsId = jobOperations.getOnlyRealJobsId(project.staggeredJobsId, project.instanceInformation.jobsAmount)
                jobsBlockSFM(project, realJobsId)
                break
            case EnumLocalSearch.BMS.name:
                def realJobsId = jobOperations.getOnlyRealJobsId(project.staggeredJobsModesId, project.instanceInformation.jobsAmount)
                bestModeToSchedule(project, realJobsId)
                break
            default:
                log.log(Level.ERROR, "LOCAL SEARCH " + localSearch + " is not valid! Please check the argument 'type.localSearch' in mrcpsp.properties file");
                throw new IllegalArgumentException("LOCAL SEARCH " + localSearch + " is not valid! Please check the argument 'type.localSearch' in mrcpsp.properties file");
                break
        }

        if (bestProject) {
            return bestProject
        } else {
            return null
        }
	}
	
	/**
	 * while i have better neighbors, i should do a new local search
	 * for each job in the project i changed it's mode and check what is the best
	 * @param project
	 */
	void lowerNonRenewableComsumption(Project project, List<Integer> realJobsId) {
		while (checkSolution) {

            jobs.each { job ->
				def neighborProject = lnrc.changeExecutionModeJob(bestProject, job.id);
				
				if (neighborProject) {
                    mmProcessor.project = neighborProject
                    mmProcessor.executeGetJobTimes()
					mmProcessor.setProjectMakespan()
				}

				if (neighborProject) {
					checkBestNeighbor(neighborProject);
				}
			}			
			
			checkBestSolution(bestNeighbor, project)
		}		
	}

    def jobsBlockSFM(Project project, List<Integer> realJobsId) {
        ShortestFeasibleModeLS sfmLS = new ShortestFeasibleModeLS()

        while (checkSolution) {

            jobs.each { job ->
                def neighborProject = sfmLS.changeExecutionModeBlockJob(bestProject, job.id)

                if (neighborProject) {
                    mmProcessor.project = neighborProject
                    mmProcessor.executeGetJobTimes()
                    mmProcessor.setProjectMakespan()
                }

                if (neighborProject) {
                    checkBestNeighbor(neighborProject);
                }
            }

            checkBestSolution(bestNeighbor, project)
        }

        return bestProject
    }

    def bestModeToSchedule(Project project, List<Integer> realJobsId) {
        BestModeScheduling bms = new BestModeScheduling()

        while (checkSolution) {

            realJobsId.each { jobId ->
                Job job = project.jobs[jobId - 1]
                def remainingModes = job.availableModes.findAll { it.id != job.mode.id }

                remainingModes.each { mode ->
                    def neighborJobsModesId
                    neighborJobsModesId = bms.changeExecutionModeJob(bestProject, job, mode);

                    if (neighborJobsModesId) {
                        def originalStaggeredJobsModesId = project.staggeredJobsModesId
                        project.staggeredJobsModesId = neighborJobsModesId
                        mmProcessor.project = project
                        mmProcessor.executeGetJobTimes()
                        mmProcessor.setProjectMakespan()
                        project.staggeredJobsModesId = originalStaggeredJobsModesId
                    }

                    if (neighborJobsModesId) {
                        checkBestNeighbor(neighborJobsModesId, jobId, mode.id);
                    }
                }
            }

            checkBestSolution(bestNeighbor, project)
        }

        setBestProjectMakespan()
    }
	
	private void checkBestNeighbor(Map<Integer, Integer> neighborJobsModesId, Integer jobId, Integer modeId) {
		if (!bestNeighbor || bestProject.makespan == PropertyConstants.INSTANCE_MAKESPAN_ERROR) {
			bestNeighbor = neighborJobsModesId
            bestNeighborMakespan = bestProject.makespan
            bestJobNeighborModification = jobId
            bestModeNeighborModification = modeId
		} else {			
			if ((bestProject.makespan < bestNeighborMakespan) && (bestProject.makespan != PropertyConstants.INSTANCE_MAKESPAN_ERROR)) {
                bestNeighbor = neighborJobsModesId
                bestNeighborMakespan = bestProject.makespan
                bestJobNeighborModification = jobId
                bestModeNeighborModification = modeId
			}
		}
	}
	
	private void checkBestSolution(Map<Integer, Integer> bestNeighbor, Project project) {
        if (bestNeighborMakespan < bestMakespan) {
			bestProject.staggeredJobsModesId = bestNeighbor
            bestMakespan = bestNeighborMakespan
            setJobModeModification(bestProject)
			log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + project.fileName + " - INITIAL SOLUTION MAKESPAN: " + project.makespan)
			log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + bestProject.fileName + " - NEIGHBOR MAKESPAN: " + bestProject.makespan)
		} else  {
			checkSolution = false
		}
	}

    private void setJobModeModification(Project bestProject) {
        Job bestJob = bestProject.jobs[bestJobNeighborModification - 1]
        Mode bestMode = bestJob.availableModes.find { it.id == bestModeNeighborModification }

        modeOperations.removingNonRenewableResources(bestProject.resourceAvailabilities, bestJob.mode)
        bestJob.mode = bestMode
        modeOperations.addingNonRenewableResources(bestProject.resourceAvailabilities, bestJob.mode)
    }

    private void setBestProjectMakespan() {
        bestProject.makespan = bestMakespan
    }

}
