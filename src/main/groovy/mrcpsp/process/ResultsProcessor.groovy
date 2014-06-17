package mrcpsp.process

import mrcpsp.model.main.Project

public class ResultsProcessor {
	
	Project bestProject
    Double averageMakespan

    ResultsProcessor() {
        averageMakespan = 0.0

        bestProject = new Project(staggeredJobsId: [], modes: [:], times: [:])
    }

	public void checkLowerMakespan(Project project) {
        bestProject.averageMakespan = averageMakespan
        if (!bestProject.makespan || (project.makespan < bestProject.makespan)) {
            bestProject.setProject(project)
        }

        averageMakespan+= project.makespan
        bestProject.averageMakespan = averageMakespan
	}
}
