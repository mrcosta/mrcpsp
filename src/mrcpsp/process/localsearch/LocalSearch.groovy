package mrcpsp.process.localsearch

import mrcpsp.model.enums.EnumLocalSearch
import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
import mrcpsp.process.job.JobOperations
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

class LocalSearch {

	static final Logger log = Logger.getLogger(LocalSearch.class);
	
	Project bestProject
	Project bestNeighbor
	boolean checkSolution
	MmProcessor mmProcessor
    LowerNonRenewableConsumption lnrc
    JobOperations jobOperations

    LocalSearch() {
		mmProcessor = new MmProcessor()
        lnrc = new LowerNonRenewableConsumption()
        jobOperations = new JobOperations()
	}
	
	def Project executeLocalSearch(Project project) {
		def localSearch = UrlUtils.instance.localSearch
        bestProject = project
        bestNeighbor = project
        checkSolution = true

        log.info("LOCAL SEARCH: " + localSearch)
        switch (localSearch) {
            case EnumLocalSearch.LNRC.name:
                def realJobs = jobOperations.getOnlyRealJobs(project.staggeredJobs, project.instanceInformation.jobsAmount)
                lowerNonRenewableComsumption(project, realJobs)
                break
            case EnumLocalSearch.LNRCCP.name:
                def realJobs = jobOperations.getOnlyRealJobs(project.criticalPath, project.instanceInformation.jobsAmount)
                lowerNonRenewableComsumption(project, realJobs)
                break
            case EnumLocalSearch.BSFM.name:
                def realJobs = jobOperations.getOnlyRealJobs(project.staggeredJobs, project.instanceInformation.jobsAmount)
                jobsBlockSFM(project, realJobs)
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
	void lowerNonRenewableComsumption(Project project, List<Job> jobs) {
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

    void jobsBlockSFM(Project project, List<Job> jobs) {
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
    }
	
	private void checkBestNeighbor(Project project) {
		if (!bestNeighbor || bestNeighbor.makespan == PropertyConstants.INSTANCE_MAKESPAN_ERROR) {
			bestNeighbor = project
		} else {			
			if (project.makespan < bestNeighbor.makespan) {
				bestNeighbor = project
			}
		}
	}
	
	private void checkBestSolution(Project bestNeighbor, Project project) {
		if (bestNeighbor.makespan < bestProject.makespan) {
			bestProject = bestNeighbor
			log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + project.fileName + " - INITIAL SOLUTION MAKESPAN: " + project.makespan)
			log.info("LOOKING FOR A BETTER SOLUTION - FILE: " + bestProject.fileName + " - NEIGHBOR MAKESPAN: " + bestProject.makespan)
		} else  {
			checkSolution = false
		}
	}

}
