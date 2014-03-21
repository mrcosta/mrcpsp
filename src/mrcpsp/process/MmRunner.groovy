package mrcpsp.process;

import mrcpsp.utils.ChronoWatch;
import mrcpsp.utils.PropertyManager;
import mrcpsp.utils.SystemUtils;

import java.io.File;

/**
 * @author mrcosta
 * 
 */
class MmRunner {

	public static void main(String[] args) {
        ExecutionTypeProcessor executionTypeProcessor = new ExecutionTypeProcessor();
        File resultsFolder = new File(System.getProperty("user.home") + "/tests");

        executionTypeProcessor.removeOldResultFiles();
        SystemUtils.getSystemInformation()

        if (resultsFolder.listFiles().length > 0) {
            resultsFolder.listFiles().each {
                /*ChronoWatch.instance.time = 0
                ChronoWatch.instance.start();

                executionTypeProcessor.execute();*/

                println it.name
            }
        } else {
            ChronoWatch.instance.time = 0
            ChronoWatch.instance.start();
            PropertyManager.getInstance();
            executionTypeProcessor.execute();
        }
	}
}
