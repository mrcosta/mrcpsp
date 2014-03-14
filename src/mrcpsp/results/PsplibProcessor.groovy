package mrcpsp.results

import mrcpsp.utils.FileUtils
import org.apache.log4j.Logger

/**
 * Created by mateus on 3/13/14.
 */
class PsplibProcessor {

    private static final Logger log = Logger.getLogger(FileUtils.class);

    static final Integer FIRST_LINE_RESULTS = 26;
    static final Integer LAST_LINE_RESULTS = 666;

    def readResultsFromFile(String fileName) {
        FileUtils fu = new FileUtils()
        File file = fu.loadFile(fileName)

        log.info("File name: " + file.getName());

        for (int i = FIRST_LINE_RESULTS; i < LAST_LINE_RESULTS; i++) {
            String line = fu.getLine(file, i)
            def lineValues = line.split(",")

            def instanceName = createFileName(fileName, lineValues[0].trim(), lineValues[1].trim())
            def result = lineValues[2].trim()

            println "instanceName: $instanceName - result: $result"
        }



    }

    def createFileName(String fileName, String instance, String parameter) {
        String instanceName = fileName.split("opt")[0]
        return instanceName+= instance + "_" + parameter + ".mm"
    }


}
