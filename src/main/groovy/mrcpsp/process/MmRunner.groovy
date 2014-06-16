package mrcpsp.process

import mrcpsp.utils.ChronoWatch;
import mrcpsp.utils.PropertyManager;
import mrcpsp.utils.SystemUtils;

/**
 * @author mrcosta
 * 
 */
class MmRunner {

	public static void main(String[] args) {
        ExecutionTypeProcessor executionTypeProcessor = new ExecutionTypeProcessor()
        File testsFolder = new File(System.getProperty("user.home") + "/tests_temp");

        executionTypeProcessor.removeOldResultFiles();
        SystemUtils.getSystemInformation()

        if (testsFolder.listFiles().length > 0) {
            testsFolder.listFiles().each {
                println "$it.name will be executed"
                ChronoWatch.instance.time = 0
                ChronoWatch.instance.start();
                PropertyManager.getInstance(it.text)

                executionTypeProcessor = new ExecutionTypeProcessor()
                executionTypeProcessor.execute();
                println "$it.name was executed"
                it.delete()
            }
        } else {
            ChronoWatch.instance.time = 0
            ChronoWatch.instance.start();
            PropertyManager.getInstance();
            executionTypeProcessor.execute();
        }
	}
}
