package mrcpsp.process;

import mrcpsp.model.main.Project;
import mrcpsp.utils.FileUtils;

/**
 * @author mrcosta
 *
 */
class InstanceDataProcessor {

    /**
     * get the instance's information
     */
    Project getInstanceData(String fileName) {
		FileUtils fileUtils = new FileUtils()
		
		return fileUtils.loadInstanceInformation(fileName)
	}

}
