package mrcpsp.process.localsearch

import mrcpsp.model.enums.EnumLocalSearch
import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

class LocalSearch {

	static final Logger log = Logger.getLogger(LocalSearch.class);
	
	Project bestProject
	Project bestNeighbor
	boolean checkSolution
	MmProcessor mmProcessor

    LocalSearch() {
		mmProcessor = new MmProcessor()
	}
	
	def Project executeLocalSearch(Project project) {
		def localSearch = UrlUtils.instance.localSearch

        log.info("LOCAL SEARCH: " + localSearch)
        switch (localSearch) {
            case EnumLocalSearch.LNRC.name:
                lowerNonRenewableComsumption(project)
                if (bestProject) {
                    return bestProject
                } else {
                    return null
                }
                break
            case EnumLocalSearch.LNRCCP.name:

                break
            default:
                log.log(Level.ERROR, "LOCAL SEARCH " + localSearch + " is not valid! Please check the argument 'type.localSearch' in mrcpsp.properties file");
                throw new IllegalArgumentException("LOCAL SEARCH " + localSearch + " is not valid! Please check the argument 'type.localSearch' in mrcpsp.properties file");
                break
        }
		
		null
	}
	
	/**
	 * while i have better neighbors, i should do a new local search
	 * for each job in the project i changed it's mode and check what is the best
	 * @param project
	 */
	private void lowerNonRenewableComsumption(Project project) {
		def jobsAmount = project.getInstanceInformation().getJobsAmount();
		LowerNonRenewableConsumption lnrc = new LowerNonRenewableConsumption();
		
		// before start, the best project is the actual one
		bestProject = project;
		checkSolution = true;
		
		while (checkSolution) {
			for (int i = 0; i < jobsAmount; i++) {
				def neighborProject = lnrc.changeExecutionModeJob(bestProject, i);
				
				if (neighborProject) {
					mmProcessor.executeCheckRestrictionsAndGetJobTimes(neighborProject)
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
			log.info("FILE: " + project.getFileName() + " - INITIAL SOLUTION MAKESPAN: " + project.getMakespan())
			log.info("FILE: " + bestProject.getFileName() + " - NEIGHBOR MAKESPAN: " + bestProject.getMakespan())
		} else  {
			checkSolution = false
		}
	}

}
