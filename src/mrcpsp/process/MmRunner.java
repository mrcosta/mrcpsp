package mrcpsp.process;

import mrcpsp.utils.ChronoWatch;
import mrcpsp.utils.SystemUtils;

/**
 * @author mrcosta
 * 
 */
public class MmRunner {

	public static void main(String[] args) {
        ExecutionTypeProcessor executionTypeProcessor = new ExecutionTypeProcessor();

        executionTypeProcessor.removeOldResultFiles();
        SystemUtils.getSystemInformation();
        ChronoWatch.getInstance().start();

        // if there is some file in the userDir/tests execute for each file
        executionTypeProcessor.execute();
	}
}
