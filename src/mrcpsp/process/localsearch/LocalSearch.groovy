package mrcpsp.process.localsearch

import mrcpsp.model.enums.EnumLocalSearch
import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
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

    LocalSearch() {
		mmProcessor = new MmProcessor()
        lnrc = new LowerNonRenewableConsumption();
	}
	
	def Project executeLocalSearch(Project project) {
		def localSearch = UrlUtils.instance.localSearch
        bestProject = project
        bestNeighbor = project
        checkSolution = true

        log.info("LOCAL SEARCH: " + localSearch)
        switch (localSearch) {
            case EnumLocalSearch.LNRC.name:
                lowerNonRenewableComsumption(project)
                break
            case EnumLocalSearch.LNRCCP.name:
                criticalPathLowerNonRenewableComsumption(project)
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
     * for each job in the critical path i changed it's mode and check what is the best
     * @param project
     */
    private void criticalPathLowerNonRenewableComsumption(Project project) {
        while (checkSolution) {
            def realJobs = getOnlyRealJobs(project.criticalPath, project.instanceInformation.jobsAmount)

            realJobs.each { job ->
                def neighborProject = lnrc.changeExecutionModeJob(bestProject, project.staggeredJobs.findIndexOf {it.id == job.id});

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
	
	/**
	 * while i have better neighbors, i should do a new local search
	 * for each job in the project i changed it's mode and check what is the best
	 * @param project
	 */
	private void lowerNonRenewableComsumption(Project project) {
		while (checkSolution) {
			def realJobs = getOnlyRealJobs(project.staggeredJobs, project.instanceInformation.jobsAmount)

            realJobs.each { job ->
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
			log.info("LOOKING FOR A BETTER SOLUTION")
			log.info("FILE: " + project.fileName + " - INITIAL SOLUTION MAKESPAN: " + project.makespan)
			log.info("FILE: " + bestProject.fileName + " - NEIGHBOR MAKESPAN: " + bestProject.makespan)
		} else  {
			checkSolution = false
		}
	}

    private List<Job> getOnlyRealJobs(List<Job> jobs, Integer lastJobId) {
        return jobs.findAll{ job -> ![1, lastJobId].contains(job.id) }
    }

}
