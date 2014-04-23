package mrcpsp.analytics

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import mrcpsp.process.MmProcessor
import mrcpsp.utils.FileUtils
import org.apache.log4j.Level
import org.apache.log4j.Logger

/**
 * Created by mateus on 3/20/14.
 */
class CompareResults {

    static final Logger log = Logger.getLogger(MmProcessor.class)

    public static final Integer NR_PSPLIB = 16384
    public static final Integer NR = -999

    def result
    def resultCpr
    def dataAnalytics

    def compareInstances() {
        checkResultsFolder()
    }

    def checkResultsFolder() {
        File resultsFolder = new File(System.getProperty("user.home") + "/results")

        if (resultsFolder.listFiles().size() == 2 || resultsFolder.listFiles().size() == 3) {
            parseJsonFiles(resultsFolder)

            def resultAnalytics = generateAnalytics(result)
            def resultAnalyticsCpr = generateAnalytics(resultCpr)

            String data = compareResult(resultAnalytics, resultAnalyticsCpr)
            FileUtils.writeToFile(new File(resultsFolder.absolutePath + "/results.json"), data, false)
        } else {
            log.log(Level.ERROR, "Need two results files to compare")
            throw new IllegalArgumentException("Need two results files to compare")
        }
    }

    def parseJsonFiles(File resultsFolder) {
        JsonSlurper js = new JsonSlurper()

        result = js.parseText(resultsFolder.listFiles()[0].text)
        resultCpr = js.parseText(resultsFolder.listFiles()[1].text)
    }

    def generateAnalytics(result) {
        def analytics = [:]

        analytics.testName = result.testName

        analytics.totalFiles = 0
        analytics.results = [:]
        analytics.totalWithoutSolution = 0
        analytics.totalWithSolution = 0
        result.results.each { instanceResult ->
            analytics.totalFiles++

            if (instanceResult.value.makespan == NR_PSPLIB || instanceResult.value.makespan == NR ) {
                analytics.totalWithoutSolution++
            } else {
                analytics.results."$instanceResult.key" = instanceResult.value
                analytics.totalWithSolution++
            }
        }

        return analytics
    }

    def compareResult(resultAnalytics, resultAnalyticsCpr) {
        def lesserRa = resultAnalytics.totalWithSolution <= resultAnalyticsCpr.totalWithSolution ? resultAnalytics : resultAnalyticsCpr
        def biggerRa = resultAnalytics.totalWithSolution > resultAnalyticsCpr.totalWithSolution ? resultAnalytics : resultAnalyticsCpr
        dataAnalytics = [:]

        log.info("Difference between $lesserRa.testName and $biggerRa.testName")

        dataAnalytics."$resultAnalytics.testName" = putGeneralResultsInformation(resultAnalytics)
        dataAnalytics."$resultAnalyticsCpr.testName" = putGeneralResultsInformation(resultAnalyticsCpr)

        dataAnalytics.diffMakespan = [:]
        dataAnalytics.averageDiffMakespan = 0
        lesserRa.results.each { instanceResult ->
            def diffMakespan = (instanceResult.value.makespan - biggerRa.results."$instanceResult.key".makespan) /  biggerRa.results."$instanceResult.key".makespan * 100
            dataAnalytics.averageDiffMakespan+= diffMakespan
        }

        dataAnalytics.averageDiffMakespan = dataAnalytics.averageDiffMakespan / (lesserRa.totalFiles - lesserRa.totalWithoutSolution)
        dataAnalytics.diffFoundSolutions = 100 + ((lesserRa.totalWithSolution - biggerRa.totalWithSolution) / biggerRa.totalWithSolution * 100)

        return new JsonBuilder(dataAnalytics).toPrettyString()
    }

    def putGeneralResultsInformation(generalResults) {
        def results = [:]

        results.testName = generalResults.testName
        results.totalFiles = generalResults.totalFiles
        results.totalWithSolution = generalResults.totalWithSolution
        results.totalWithoutSolution = generalResults.totalWithoutSolution

        return results
    }
}
