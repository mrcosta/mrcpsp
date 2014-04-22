package mrcpsp.results

import groovy.json.JsonBuilder
import mrcpsp.utils.FileUtils
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Logger

/**
 * Created by mateus on 3/20/14.
 */
class J30PsplibProcessor {

    private static final Logger log = Logger.getLogger(FileUtils.class);

    static final Integer FIRST_LINE_RESULTS = 4;
    static final Integer LAST_LINE_RESULTS = 556;

    def resultMap

    def readResultsFromFile(String fileName) {
        resultMap = [:]
        FileUtils fu = new FileUtils()
        File results = fu.loadFile(fileName)

        def instance = fileName.split("hrs")[0].capitalize()
        addConfigurationToJson(instance)
        resultMap.results = [:]

        log.info("File name: $results.name -- Instance Set: $instance");

        def filesResult = []
        for (int i = FIRST_LINE_RESULTS; i < LAST_LINE_RESULTS; i++) {
            String line = fu.getLine(results, i)
            def lineValues = line.split(",")

            def file = createFileName(fileName, lineValues[0].trim(), lineValues[1].trim())
            def makespan = lineValues[2].trim()

            def instanceResult = [:]
            resultMap.results."$file" = [:]
            instanceResult.makespan = makespan
            resultMap.results."$file" = instanceResult

            filesResult.add(instanceResult)
        }

        def jsonText = new JsonBuilder(resultMap).toPrettyString()
        def allResultFile = new File("data/psplibresults/json" , instance + ".json")
        FileUtils.writeToFile(allResultFile, jsonText, false)
    }

    def createFileName(String fileName, String instance, String parameter) {
        String instanceName = fileName.split("hrs")[0]
        return instanceName+= instance + "_" + parameter + ".mm"
    }

    def addConfigurationToJson(String instance) {
        resultMap.instanceSet = instance
        resultMap.executionType = UrlUtils.instance.executionType
        resultMap.testName = "reading the results from psplib"

        resultMap.instanceConfig = [:]
        resultMap.instanceConfig.firstLineResults = FIRST_LINE_RESULTS
        resultMap.instanceConfig.lastLineResults = LAST_LINE_RESULTS
    }
}
