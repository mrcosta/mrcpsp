package mrcpsp.results

import groovy.json.JsonBuilder
import mrcpsp.model.main.Job
import mrcpsp.model.main.Project
import mrcpsp.utils.ChronoWatch
import mrcpsp.utils.PropertyConstants
import mrcpsp.utils.PropertyManager
import mrcpsp.utils.UrlUtils

/**
 * Created by mateus on 3/20/14.
 */
class ResultJsonBuilder {

    def resultMap
    def instanceResultAllFiles

    ResultJsonBuilder() {
        resultMap = [:]
        instanceResultAllFiles = [:]
    }

    def buildInstanceResultJson(Project project) {
        def instanceResult = addResults(project)

        instanceResultAllFiles."$project.fileName" = [:]
        instanceResultAllFiles."$project.fileName" = instanceResult
    }

    def mergeConfigurationAndResults() {
        resultMap = addConfigurationProperties()
        resultMap.results = instanceResultAllFiles

        return new JsonBuilder(resultMap).toPrettyString()
    }

    def addResults(Project project) {
        def instanceResult = [:]

        instanceResult.makespan = project.makespan
        instanceResult.jobsId = project.staggeredJobsModesId*.key.toString() ?: "null"
        instanceResult.modesId = project.staggeredJobsModesId*.value.toString() ?: "null"
        instanceResult.executionTime = ChronoWatch.instance.totalTimeSolutionFormated

        instanceResult.times = [:]
        project.staggeredJobsModesId.each { jobModeId ->
            def start = project.startJobTimes[jobModeId.key]
            def finish = project.finishJobTimes[jobModeId.key]

            instanceResult.times."$jobModeId.key" = "$start - $finish"
        }

        return instanceResult
    }

    def addConfigurationProperties() {
        resultMap.executionType = UrlUtils.instance.executionType
        resultMap.instanceFolder = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCES_FOLDER)
        resultMap.localSearchType = UrlUtils.instance.localSearch
        resultMap.testName = UrlUtils.instance.testName
        resultMap.testDescription = UrlUtils.instance.testDescription

        ChronoWatch.instance.getTimeExecution()
        resultMap.totalExecutionTime = ChronoWatch.instance.totalTimeExecutionFormated

        resultMap.generalConfig = [:]
        resultMap.generalConfig.executionTimes = UrlUtils.instance.executionTimes
        resultMap.generalConfig.thread = UrlUtils.instance.hasThread
        resultMap.generalConfig.concurrentPoolSize = PropertyManager.instance.getProperty(PropertyConstants.CONCURRENT_POOLSIZE)
        resultMap.generalConfig.instanceFile = PropertyManager.instance.getProperty(PropertyConstants.INSTANCE_FILE)
        resultMap.generalConfig.executeLocalSearch = UrlUtils.instance.executeLocalSearch
        resultMap.generalConfig.perturbation = UrlUtils.instance.perturbation
        resultMap.generalConfig.generateDiagram = UrlUtils.instance.generateDiagram
        resultMap.generalConfig.diagramPath = UrlUtils.instance.diagramPath
        resultMap.generalConfig.showPredecessorsInDiagram = UrlUtils.instance.showPredecessors
        resultMap.generalConfig.writeLowerboundForAllInstances = UrlUtils.instance.writeLowerboundForAllInstances
        resultMap.generalConfig.showCriticalPath = UrlUtils.instance.showCriticalPath
        resultMap.generalConfig.showLowerBound = UrlUtils.instance.showLowerBound

        resultMap.instanceConfig = [:]
        resultMap.instanceConfig.modesRankingCriteria = UrlUtils.instance.modesRankingCriteria
        resultMap.instanceConfig.rankingJobsReverseOrder = UrlUtils.instance.modesRankingJobsReverseOrder
        resultMap.instanceConfig.startLineJobs = UrlUtils.instance.startLineJobs
        resultMap.instanceConfig.startLineModes = UrlUtils.instance.startLineModes
        resultMap.instanceConfig.startLineResourceAvailabilities = UrlUtils.instance.startLineResourceAvailabilities
        resultMap.instanceConfig.rclSize = UrlUtils.instance.RCLSize
        resultMap.instanceConfig.jobsMode = UrlUtils.instance.jobsMode
        resultMap.instanceConfig.modeShorterNearToLowerNrPercentage = UrlUtils.instance.modeShorterNearToLowerNrPercentage
        resultMap.instanceConfig.modeShorterNearToLowerNrUnit = UrlUtils.instance.modeShorterNearToLowerNrUnit
        resultMap.instanceConfig.jobPriorityRule = UrlUtils.instance.jobPriorityRule

        return resultMap
    }
}
