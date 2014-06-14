package mrcpsp.analytics

import groovy.json.JsonSlurper
import mrcpsp.process.MmProcessor
import org.apache.log4j.Logger

/**
 * Created by mateus on 6/9/14.
 */
class DesignOfExperimentsAnalytics {

    static final Logger log = Logger.getLogger(MmProcessor.class)

    static final Integer NR_PSPLIB = 16384
    static final Integer NR = -999
    static final String  J10 = "J10.json"
    static final String  J12 = "J12.json"
    static final String  J14 = "J14.json"
    static final String  J16 = "J16.json"
    static final String  J18 = "J18.json"
    static final String  J20 = "J20.json"
    static final String  J30 = "J30.json"

    def resultPsplib
    def resultToCompare
    def dataAnalytics

    List resultFiles
    List parametersForDoe

    DesignOfExperimentsAnalytics() {
        this.resultFiles = new File(System.getProperty("user.home") + "/results").listFiles() as List

        this.parametersForDoe = []
    }

    def removePreviousResultFile() {
        def resultFile = resultFiles.find { it.name == "results.txt" }

        if (resultFile) {
            resultFile.delete()
            resultFiles.remove(resultFile)
        }
    }

    def checkFactors(parametersForDoe, factor) {
        def result = [:]
        def doeAverageMakespan = 0

        parametersForDoe.sort{ it."$factor" }
        parametersForDoe = parametersForDoe.reverse()
        def groupFactorValues = parametersForDoe.groupBy{ it."$factor" }

        groupFactorValues.each { group ->

            group.value.each { experiment ->
                doeAverageMakespan+= experiment.averageMakespan
                if (!result."$factor-$group.key") {
                    result."$factor-$group.key" = [:]
                    result."$factor-$group.key".averageMakespan = 0
                    result."$factor-$group.key".count = 0
                }

                result."$factor-$group.key".averageMakespan+= experiment.averageMakespan
                result."$factor-$group.key".count++
            }
        }

        def countExperiments = 0
        def averageResult = [:]
        result.each { experiment ->
            averageResult."$experiment.key" = experiment.value.averageMakespan / experiment.value.count
            countExperiments+= experiment.value.count
        }

        averageResult.generalAverageMakespan = (doeAverageMakespan / countExperiments)

        return averageResult
    }


    public static void main(String[] args) {
        DesignOfExperimentsAnalytics dea = new DesignOfExperimentsAnalytics()
        CompareResults compareResults = new CompareResults()
        def factors = ["rclSize", "jobsMode", "temperature", "reductionCoefficient", "stoppingCriterion"]

        dea.removePreviousResultFile()

        File resultPsplibFile = dea.resultFiles.find { it.name == J10 }
        dea.resultFiles.remove(resultPsplibFile)
        dea.resultPsplib = new JsonSlurper().parseText(resultPsplibFile.text)

        dea.resultFiles.each { file ->
            dea.resultToCompare = new JsonSlurper().parseText(file.text)

            def resultAnalytics = compareResults.generateAnalytics(dea.resultPsplib)
            def resultAnalyticsCpr = compareResults.generateAnalytics(dea.resultToCompare)

            compareResults.compareResult(resultAnalytics, resultAnalyticsCpr)

            dea.parametersForDoe.add(compareResults.parametersForDoE)
        }

        def groupExecutionTimes = dea.parametersForDoe.groupBy{ it.executionTimes }

        groupExecutionTimes.each {

            factors.each { factor ->
                println "Execution times: $it.key -- Factor: $factor -- ${(dea.checkFactors(it.value, factor))}"
            }

        }

//        File parametersResultFile = new File(System.getProperty("user.home") + "/results/results.txt")
//        dea.parametersForDoe.sort { it.averageMakespan }
//        dea.parametersForDoe.each {
//            parametersResultFile << it
//            parametersResultFile << "\n"
//        }
    }
}
