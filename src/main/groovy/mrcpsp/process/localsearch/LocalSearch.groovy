package mrcpsp.process.localsearch

import mrcpsp.model.main.Project
import mrcpsp.process.MmProcessor
import mrcpsp.process.job.JobOperations

class LocalSearch {

	Project bestProject
	Project bestNeighbor
	boolean checkSolution
	MmProcessor mmProcessor
    JobOperations jobOperations

    LocalSearch() {
		mmProcessor = new MmProcessor()
        jobOperations = new JobOperations()
	}
	
	def Project executeLocalSearch(Project project) {
        bestProject = project
        bestNeighbor = project
        checkSolution = true

        simulatedAnnealing(project)

        if (bestProject) {
            return bestProject
        } else {
            return null
        }
	}

    def simulatedAnnealing(Project project) {
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing()

        bestProject = simulatedAnnealing.executeSimulatedAnnealing(project)
    }

}
