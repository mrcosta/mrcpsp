package mrcpsp.process

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.utils.FileUtils
import mrcpsp.utils.LogUtils
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils

public class ResultsProcessor {
	
	Project lowerProjectMakespan

	def boolean getMakespanFromScheduledJobs(Project project, boolean success) {
		if (success) {
			Integer lastIndex = project.staggeredJobs.size() - 1
			Integer makespan = project.staggeredJobs.get(lastIndex).getEndTime()
			
			project.makespan = makespan
			success
		} else {
			project.instanceResultStatus = " - Check results."
			project.makespan = PropertyConstants.INSTANCE_MAKESPAN_ERROR
			false
		}
	}

	public void checkLowerMakespan(Project project) {
		if (lowerProjectMakespan == null || lowerProjectMakespan.makespan == PropertyConstants.INSTANCE_MAKESPAN_ERROR) {
			lowerProjectMakespan = project
		} else {			
			if (project.makespan < lowerProjectMakespan.makespan) {
				lowerProjectMakespan = project
			}
		}
	}

    def getCriticalPath(Project project) {
        Job lastJob = project.staggeredJobs.last()
        project.criticalPath = []
        project.criticalPath.add(lastJob)

        while(lastJob.id != 1 || !lastJob.predecessors.isEmpty()) {
            def predecessorsJobList = []

            lastJob.predecessors.each { predecessor ->
                predecessorsJobList.add(project.staggeredJobs.find{it.id == predecessor})
            }

            lastJob = predecessorsJobList.max { it.endTime }
            project.criticalPath.add(lastJob)
        }

        return project.criticalPath
    }
}