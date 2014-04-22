package mrcpsp.results

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import mrcpsp.utils.FileUtils
import mrcpsp.utils.UrlUtils
import org.apache.log4j.Logger

/**
 * Created by mateus on 3/13/14.
 */
class PsplibProcessor {

    private static final Logger log = Logger.getLogger(FileUtils.class);

    static final Integer FIRST_LINE_RESULTS = 26;
    static final Integer LAST_LINE_RESULTS = 666;
    static final Integer INSTANCE_SET_LINE = 3;

    def resultMap

    def readResultsFromFile(String fileName) {
        resultMap = [:]
        FileUtils fu = new FileUtils()
        File results = fu.loadFile(fileName)

        def instance = fu.getLine(results, INSTANCE_SET_LINE).split(":")[1]
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
            instanceResult.makespan = Integer.parseInt(makespan)
            instanceResult.executionTime = lineValues[3].trim()
            resultMap.results."$file" = instanceResult

            filesResult.add(instanceResult)
        }

        def jsonText = new JsonBuilder(resultMap).toPrettyString()
        def allResultFile = new File("data/psplibresults/json" , instance + ".json")
        FileUtils.writeToFile(allResultFile, jsonText, false)
    }

    def createFileName(String fileName, String instance, String parameter) {
        String instanceName = fileName.split("opt")[0]
        return instanceName+= instance + "_" + parameter + ".mm"
    }

    def addConfigurationToJson(String instance) {
        resultMap.instanceSet = instance
        resultMap.executionType = UrlUtils.instance.executionType
        resultMap.testName = "reading the results from psplib"

        resultMap.instanceConfig = [:]
        resultMap.instanceConfig.firstLineResults = FIRST_LINE_RESULTS
        resultMap.instanceConfig.lastLineResults = LAST_LINE_RESULTS
        resultMap.instanceConfig.instanceSetLine = INSTANCE_SET_LINE
    }
}
