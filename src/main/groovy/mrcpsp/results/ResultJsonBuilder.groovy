package mrcpsp.results

import groovy.json.JsonBuilder
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
        instanceResult.averageMakespan = (project.averageMakespan == 0.0) ? project.makespan : getAverageMakespan(project.averageMakespan)
        instanceResult.jobsId = project.staggeredJobsId?.toString()
        instanceResult.modesId = getJobsModeId(project.staggeredJobsId, project.modes)
        instanceResult.executionTime = ChronoWatch.instance.totalTimeSolutionFormated

        instanceResult.times = [:]
        project.staggeredJobsId.each { jobId ->
            instanceResult.times."$jobId" = project.times."$jobId"
        }

        return instanceResult
    }

    def addConfigurationProperties() {
        resultMap.executionType = UrlUtils.instance.executionType
        resultMap.instanceFolder = PropertyManager.getInstance().getProperty(PropertyConstants.INSTANCES_FOLDER)
        resultMap.testName = UrlUtils.instance.testName
        resultMap.testDescription = UrlUtils.instance.testDescription

        ChronoWatch.instance.getTimeExecution()
        resultMap.totalExecutionTime = ChronoWatch.instance.totalTimeExecutionFormated

        resultMap.generalConfig = [:]
        resultMap.generalConfig.executionTimes = UrlUtils.instance.executionTimes
        resultMap.generalConfig.instanceFile = PropertyManager.instance.getProperty(PropertyConstants.INSTANCE_FILE)
        resultMap.generalConfig.executeLocalSearch = UrlUtils.instance.executeLocalSearch

        resultMap.instanceConfig = [:]
        resultMap.instanceConfig.modesRankingCriteria = UrlUtils.instance.modesRankingCriteria
        resultMap.instanceConfig.rankingJobsReverseOrder = UrlUtils.instance.modesRankingJobsReverseOrder
        resultMap.instanceConfig.startLineJobs = UrlUtils.instance.startLineJobs
        resultMap.instanceConfig.startLineModes = UrlUtils.instance.startLineModes
        resultMap.instanceConfig.startLineResourceAvailabilities = UrlUtils.instance.startLineResourceAvailabilities
        resultMap.instanceConfig.rclSize = UrlUtils.instance.RCLSize
        resultMap.instanceConfig.jobsMode = UrlUtils.instance.jobsMode
        resultMap.instanceConfig.jobPriorityRule = UrlUtils.instance.jobPriorityRule
        resultMap.instanceConfig.temperature = UrlUtils.instance.SATemperature
        resultMap.instanceConfig.reductionCoefficient = UrlUtils.instance.SAReductionCoefficient
        resultMap.instanceConfig.stoppingCriterion = UrlUtils.instance.SAStoppingCriterion

        return resultMap
    }

    Double getAverageMakespan(Double totalMakespan) {
        def executionType = UrlUtils.instance.executionType
        def executionTimes = UrlUtils.instance.executionTimes
        def totalExecutions = (executionType == "ALL" || executionType == "ONE_FILE") ? 1 : executionTimes

        return totalMakespan / totalExecutions
    }

    String getJobsModeId(List<Integer> staggeredJobsId, Map modes) {
        def modesId = []

        staggeredJobsId.each { jobId ->
            modesId.add(modes."$jobId")
        }

        return modesId.toString()
    }
}
