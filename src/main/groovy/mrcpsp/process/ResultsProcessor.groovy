package mrcpsp.process

import mrcpsp.model.main.Project

public class ResultsProcessor {
	
	Project bestProject
    Double averageMakespan

    ResultsProcessor() {
        averageMakespan = 0.0
    }

	def boolean getMakespanFromScheduledJobs(Project project) {
        Integer lastIndex = project.staggeredJobs.size() - 1
        Integer makespan = project.end.get(lastIndex)

        project.makespan = makespan
	}

	public void checkLowerMakespan(Project project) {
        bestProject.averageMakespan = averageMakespan
        if (project.makespan < bestProject.makespan) {
            bestProject = CloneUtils.cloneProject(project)
        }

        averageMakespan+= project.makespan
        bestProject.averageMakespan = averageMakespan
	}
}
