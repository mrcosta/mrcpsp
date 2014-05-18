package mrcpsp.process

import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.utils.CloneUtils
import mrcpsp.utils.FileUtils
import mrcpsp.utils.LogUtils
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.UrlUtils

public class ResultsProcessor {
	
	Project lowerProjectMakespan

	def boolean getMakespanFromScheduledJobs(Project project, boolean success) {
		if (success) {
			Integer lastIndex = project.getStaggeredJobsModesId().size() - 1
			Integer makespan = project.jobs[lastIndex].endTime
			
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
            lowerProjectMakespan = new Project()
            restartLowerProjectMakespanValues()
            setLowerProjectMakespanValues(project)
            lowerProjectMakespan.averageMakespan+= project.makespan
		} else {
			if (project.makespan < lowerProjectMakespan.makespan) {
                restartLowerProjectMakespanValues()
                setLowerProjectMakespanValues(project)
			}
            lowerProjectMakespan.averageMakespan+= project.makespan
		}
	}

    def getCriticalPath(Project project) {
        Job lastJob = project.staggeredJobsId.last()
        project.criticalPath = []
        project.criticalPath.add(lastJob)

        while(lastJob.id != 1 || !lastJob.predecessors.isEmpty()) {
            def predecessorsJobList = []

            lastJob.predecessors.each { predecessor ->
                predecessorsJobList.add(project.staggeredJobsId.find{it.id == predecessor})
            }

            lastJob = predecessorsJobList.max { it.endTime }
            project.criticalPath.add(lastJob)
        }

        return project.criticalPath
    }

    def restartLowerProjectMakespanValues() {
        lowerProjectMakespan.makespan = 0
        lowerProjectMakespan.staggeredJobsModesId.clear()
        lowerProjectMakespan.startJobTimes.clear()
        lowerProjectMakespan.finishJobTimes.clear()
    }

    def setLowerProjectMakespanValues(Project project) {
        lowerProjectMakespan.fileName = project.fileName
        lowerProjectMakespan.makespan = project.makespan

        lowerProjectMakespan.staggeredJobsModesId.putAll(project.staggeredJobsModesId)
        lowerProjectMakespan.startJobTimes.putAll(project.startJobTimes)
        lowerProjectMakespan.finishJobTimes.putAll(project.finishJobTimes)
    }
}
