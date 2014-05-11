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

    static final Integer NR_PSPLIB = 16384
    static final Integer NR = -999

    def result
    def resultCpr
    def dataAnalytics

    def compareInstances() {
        checkResultsFolder()
    }

    def checkResultsFolder() {
        def resultsFolder

        resultsFolder = checkFilesAmount()
        parseJsonFiles(resultsFolder)

        def resultAnalytics = generateAnalytics(result)
        def resultAnalyticsCpr = generateAnalytics(resultCpr)

        String data = compareResult(resultAnalytics, resultAnalyticsCpr)
        FileUtils.writeToFile(new File(resultsFolder.absolutePath + "/results.json"), data, false)
    }

    def checkFilesAmount() {
        File resultsFolder = new File(System.getProperty("user.home") + "/results")

        if (resultsFolder.listFiles().size() == 2) {
            return resultsFolder
        } else if  (resultsFolder.listFiles().size() == 3) {
            resultsFolder.listFiles().each { file ->
                if (file.name == "results.json") {
                    file.delete()
                }
            }
            return resultsFolder
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
        def lesserRa = resultAnalytics.totalWithSolution < resultAnalyticsCpr.totalWithSolution ? resultAnalytics : resultAnalyticsCpr
        def biggerRa = resultAnalytics.totalWithSolution >= resultAnalyticsCpr.totalWithSolution ? resultAnalytics : resultAnalyticsCpr

        if (resultAnalytics.testName == "reading the results from psplib") {
            lesserRa = resultAnalyticsCpr
            biggerRa = resultAnalytics
        } else {
            lesserRa = resultAnalytics
            biggerRa = resultAnalyticsCpr
        }

        dataAnalytics = [:]

        log.info("Difference between $lesserRa.testName and $biggerRa.testName")
        println "Difference between $lesserRa.testName and $biggerRa.testName"

        dataAnalytics."$resultAnalytics.testName" = putGeneralResultsInformation(resultAnalytics)
        dataAnalytics."$resultAnalyticsCpr.testName" = putGeneralResultsInformation(resultAnalyticsCpr)


        def similarResults = 0
        def betterResults = 0
        def worstResults = 0
        dataAnalytics.diffMakespan = [:]
        dataAnalytics.results = [:]
        dataAnalytics.diffMakespan.averageDeviation = 0
        lesserRa.results.each { instanceResult ->
            def averageDeviation = ((instanceResult.value.makespan - biggerRa.results."$instanceResult.key".makespan) /  biggerRa.results."$instanceResult.key".makespan) * 100
            dataAnalytics.diffMakespan.averageDeviation+= averageDeviation
            dataAnalytics.results."$instanceResult.key" = [:]
            dataAnalytics.results."$instanceResult.key"."$lesserRa.testName" = instanceResult.value.makespan
            dataAnalytics.results."$instanceResult.key"."$biggerRa.testName" = biggerRa.results."$instanceResult.key".makespan

            if (instanceResult.value.makespan == biggerRa.results."$instanceResult.key".makespan) {
                similarResults++
            } else if (instanceResult.value.makespan < biggerRa.results."$instanceResult.key".makespan) {
                betterResults++
            } else {
                def psplibMakespan = biggerRa.results."$instanceResult.key".makespan
                println "Instance: $instanceResult.key --- psplib: $psplibMakespan --- this work: $instanceResult.value.makespan -- difference: $averageDeviation"
                worstResults++
            }
        }

        dataAnalytics.diffMakespan.similarResults = [:]
        dataAnalytics.diffMakespan.similarResults.total = similarResults
        dataAnalytics.diffMakespan.similarResults.percentage = similarResults / lesserRa.totalWithSolution * 100

        dataAnalytics.diffMakespan.betterResults = [:]
        dataAnalytics.diffMakespan.betterResults.total = betterResults
        dataAnalytics.diffMakespan.betterResults.percentage = betterResults / lesserRa.totalWithSolution * 100

        dataAnalytics.diffMakespan.worstResults = [:]
        dataAnalytics.diffMakespan.worstResults.total = worstResults
        dataAnalytics.diffMakespan.worstResults.percentage = worstResults / lesserRa.totalWithSolution * 100

        def totalInstances = (lesserRa.totalFiles - lesserRa.totalWithoutSolution)
        println "Total instances: $totalInstances"
        dataAnalytics.diffMakespan.averageDeviation = dataAnalytics.diffMakespan.averageDeviation / totalInstances
        dataAnalytics.diffMakespan.diffFoundSolutions = 100 + ((lesserRa.totalWithSolution - biggerRa.totalWithSolution) / biggerRa.totalWithSolution * 100)

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
