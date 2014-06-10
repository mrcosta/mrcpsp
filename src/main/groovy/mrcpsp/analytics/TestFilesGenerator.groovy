package mrcpsp.analytics

import groovy.json.JsonBuilder
import mrcpsp.utils.FileUtils
import mrcpsp.utils.UrlUtils

/**
 * Created by mateus on 6/8/14.
 */
class TestFilesGenerator {

    def results = new HashSet()

    public static void main(String[] args) {
        TestFilesGenerator tfg = new TestFilesGenerator()

        tfg.getCombinations()
        def combinations = tfg.buildTestsCombinations()
        tfg.generateFile(combinations)
    }

    def buildTestsCombinations() {
        def combinations = []

        def count = 1
        results.each { configuration ->
            def combination = [:]

            combination.executionType = "ALL_TIMES"
            combination.instanceFolder = "/data/data_tests/${configuration.instanceFolder.folder}/"
            combination.localSearchType = "SA"
            combination.testName = "${configuration.instanceFolder.folder}_${count}.json"
            combination.testDescription = """
                iterations: ${configuration.iterations},
                instance: ${configuration.instanceFolder.folder},
                modesAssigner: ${configuration.modesAssigner},
                temperature: ${configuration.temperature},
                reductionCoefficient: ${configuration.reductionCoefficient},
                stoppingCriterion: ${configuration.stoppingCriterion},
                rclSize: ${configuration.rclSize}
            """

            combination.generalConfig = [:]
            combination.generalConfig.executionTimes = configuration.iterations
            combination.generalConfig.executeLocalSearch = 1

            combination.instanceConfig = [:]
            combination.instanceConfig.modesRankingCriteria = "RK_ALL"
            combination.instanceConfig.rankingJobsReverseOrder = 1
            combination.instanceConfig.startLineJobs = 18
            combination.instanceConfig.startLineModes = configuration.instanceFolder.startLineModes
            combination.instanceConfig.startLineResourceAvailabilities = configuration.instanceFolder.startLineResourceAvailabilities
            combination.instanceConfig.rclSize = configuration.rclSize
            combination.instanceConfig.jobsMode = configuration.modesAssigner
            combination.instanceConfig.jobPriorityRule = "TOTAL_SUCCESSORS"
            combination.instanceConfig.temperature = configuration.temperature
            combination.instanceConfig.reductionCoefficient = configuration.reductionCoefficient
            combination.instanceConfig.stoppingCriterion = configuration.stoppingCriterion

            combinations.add(combination)

            count++
        }

        return combinations
    }

    def generateFile(def combinationMap) {


        combinationMap.each { combination ->
            String folder = combination.testName.substring(0, 3)
            String pathFile = "${System.getProperty("user.home")}/generated_results/$folder/$combination.testName"
            String data = new JsonBuilder(combination).toPrettyString()

            FileUtils.writeToFile(new File(pathFile), data, false)
        }

    }

    def buildCombinations(Map partialCombinations, inputsLeft) {
        def first = inputsLeft.entrySet().toList().get(0)
        def partialResults = [ ]

        first.value.each{
            def next = [(first.key):it]
            next.putAll(partialCombinations)
            partialResults << next
        }
        if (inputsLeft.size() == 1) {
            results.addAll(partialResults)
        } else {
            partialResults.each{
                def rest = inputsLeft.clone()
                rest.remove(first.key)
                buildCombinations(it, rest)
            }
        }
    }

    def getCombinations() {
        def configurations = [
            temperature: [40, 20, 10],
            reductionCoefficient: [0.9, 0.65, 0.25],
            stoppingCriterion: [1, 0.1, 0.01],
            rclSize: [0.9, 0.65, 0.25],
            iterations: [1, 5],
            modesAssigner: ["ranking_frfm", "shortest_feasible_mode"],
            instanceFolder: [[folder: "j10_doe", startLineModes: 34, startLineResourceAvailabilities: 69],
                             [folder: "j12_doe", startLineModes: 36, startLineResourceAvailabilities: 77],
                             [folder: "j14_doe", startLineModes: 38, startLineResourceAvailabilities: 85],
                             [folder: "j16_doe", startLineModes: 40, startLineResourceAvailabilities: 93],
                             [folder: "j18_doe", startLineModes: 42, startLineResourceAvailabilities: 101],
                             [folder: "j20_doe", startLineModes: 44, startLineResourceAvailabilities: 109],
                             [folder: "j30_doe", startLineModes: 54, startLineResourceAvailabilities: 149]
            ]
        ]

        buildCombinations([:], configurations)
        println results.size() + " combinations:"
    }

}
