package mrcpsp.process.file

import groovy.json.JsonBuilder
import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.model.main.ResourceAvailabilities
import mrcpsp.utils.FileUtils
import org.apache.commons.io.FilenameUtils

/**
 * Created by mateus on 6/16/14.
 */
class ProjectFileWriter {

    def writeProjectToJson(Project project) {
        def instance = [:]

        instance.jobsId = project.jobs.id
        instance.modes = getJobsModeMap(project.jobs)
        instance.fileName = project.fileName
        instance.jobsAmount = project.instanceInformation.jobsAmount

        instance = getProjectResourceAvailabilities(project.resourceAvailabilities, instance)
        instance = getJobsInformation(project.jobs, instance)

        printToFile(instance)
    }

    Map getJobsModeMap(List<Job> jobs) {
        def jobsMode = [:]

        jobs.each {
            jobsMode."$it.id" = it.mode.id
        }

        return jobsMode
    }

    Map getProjectResourceAvailabilities(ResourceAvailabilities ra, Map instance) {
        instance.resourceAvailabilities = [:]

        instance.resourceAvailabilities.renewableInitialAmount = ra.renewableInitialAmount
        instance.resourceAvailabilities.nonRenewableInitialAmount = ra.nonRenewableInitialAmount

        instance.resourceAvailabilities.renewableConsumedAmount = ra.renewableConsumedAmount
        instance.resourceAvailabilities.nonRenewableConsumedAmount = ra.nonRenewableConsumedAmount

        instance.resourceAvailabilities.remainingRenewableAmount = ra.remainingRenewableAmount
        instance.resourceAvailabilities.remainingNonRenewableAmount = ra.remainingNonRenewableAmount

        instance.resourceAvailabilities.originalNonRenewableConsumedAmount = ra.originalNonRenewableConsumedAmount
        instance.resourceAvailabilities.originalNonRenewableRemainingAmount = ra.originalNonRenewableRemainingAmount

        return instance
    }

    Map getJobsInformation(List<Job> jobs, Map instance) {
        instance.jobs = [:]

        jobs.each { job ->
            instance.jobs."$job.id" = [:]
            instance.jobs."$job.id".modesAmount = job.modesAmount
            instance.jobs."$job.id".successorsAmount = job.successorsAmount
            instance.jobs."$job.id".predecessorsAmount = job.predecessorsAmount

            instance.jobs."$job.id".successors = job.originalSuccessors
            instance.jobs."$job.id".predecessors = job.predecessors

            instance.jobs."$job.id".totalSuccessors = job.runningJobInformation.totalSuccessors

            instance.jobs."$job.id".availableModes = [:]
            job.availableModes.each { mode ->
                instance.jobs."$job.id".availableModes."$mode.id" = mode.id
                instance.jobs."$job.id".availableModes."$mode.id" = [:]

                instance.jobs."$job.id".availableModes."$mode.id".duration = mode.duration
                instance.jobs."$job.id".availableModes."$mode.id".renewable = mode.renewable
                instance.jobs."$job.id".availableModes."$mode.id".nonRenewable = mode.nonRenewable
            }
        }

        return instance
    }

    def printToFile(instance) {
        String folder = instance.fileName.substring(0, 3)
        String fileName = FilenameUtils.removeExtension(instance.fileName)
        String pathFile = "${System.getProperty("user.home")}/instances/$folder/${fileName}.json"
        String data = new JsonBuilder(instance).toPrettyString()

        FileUtils.writeToFile(new File(pathFile), data, false)
    }
}
