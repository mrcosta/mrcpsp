package mrcpsp.process

import groovy.json.JsonSlurper
import mrcpsp.model.main.Job
import mrcpsp.model.main.Mode
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.utils.FileUtils

/**
 * @author mrcosta
 *
 */
class InstanceDataProcessor {

    Project getInstanceData(String fileName) {
        FileUtils fileUtils = new FileUtils()
        File instanceFile = fileUtils.loadFile(fileName)

        def instanceInformation = new JsonSlurper().parseText(instanceFile.text)

        def project = createProjectFromInstanceInformation(instanceInformation)

        return project
	}

    Project createProjectFromInstanceInformation(Map instanceInformation) {
        Project project = new Project()

        project.jobsId = instanceInformation.jobsId
        project.modes = instanceInformation.modes

        project.fileName = instanceInformation.fileName
        project.jobsAmount = instanceInformation.jobsAmount

        project = getResourcesAvailabilities(project, instanceInformation)

        project = getJobsInformation(project, instanceInformation)

        return project
    }

    Project getResourcesAvailabilities(Project project, Map instanceInformation) {
        project.resourceAvailabilities = new ResourceAvailabilities()
        project.resourceAvailabilities.renewableInitialAmount = instanceInformation.resourceAvailabilities.renewableInitialAmount
        project.resourceAvailabilities.nonRenewableInitialAmount = instanceInformation.resourceAvailabilities.nonRenewableInitialAmount

        project.resourceAvailabilities.renewableConsumedAmount = instanceInformation.resourceAvailabilities.renewableConsumedAmount
        project.resourceAvailabilities.nonRenewableConsumedAmount = instanceInformation.resourceAvailabilities.nonRenewableConsumedAmount

        project.resourceAvailabilities.remainingRenewableAmount = instanceInformation.resourceAvailabilities.remainingRenewableAmount
        project.resourceAvailabilities.remainingNonRenewableAmount = instanceInformation.resourceAvailabilities.remainingNonRenewableAmount

        project.resourceAvailabilities.originalNonRenewableConsumedAmount = instanceInformation.resourceAvailabilities.originalNonRenewableConsumedAmount
        project.resourceAvailabilities.originalNonRenewableRemainingAmount = instanceInformation.resourceAvailabilities.originalNonRenewableRemainingAmount

        return project
    }

    Project getJobsInformation(Project project, Map instanceInformation) {
        project.jobs = []

        def count = project.jobsAmount

        count.times {
            def index = it + 1
            Job job = new Job()

            job.id = index
            job.modesAmount = instanceInformation.jobs."$index".modesAmount
            job.successorsAmount = instanceInformation.jobs."$index".successorsAmount
            job.predecessorsAmount = instanceInformation.jobs."$index".predecessorsAmount
            job.successors = instanceInformation.jobs."$index".successors
            job.predecessors = instanceInformation.jobs."$index".predecessors
            job.totalSuccessors = instanceInformation.jobs."$index".totalSuccessors

            job.availableModes = getAvailableModes(instanceInformation.jobs."$index".availableModes)

            def modeId = project.modes."$job.id"
            job.mode = job.availableModes.find {it.id == modeId}

            project.jobs.add(job)
        }

        return project
    }

    List<Mode> getAvailableModes(Map mapAvailableModes) {
        List<Mode> availableModes = []
        def modesId = mapAvailableModes.keySet().sort()

        modesId.each { id ->
            Mode mode = new Mode()

            mode.id = Integer.parseInt(id)
            mode.duration = mapAvailableModes."$id".duration
            mode.renewable = mapAvailableModes."$id".renewable
            mode.nonRenewable = mapAvailableModes."$id".nonRenewable

            availableModes.add(mode)
        }

        return availableModes
    }

}
