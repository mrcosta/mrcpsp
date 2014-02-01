package mrcpsp.process;

import mrcpsp.model.main.Project;
import mrcpsp.utils.FileUtils;

/**
 * @author mrcosta
 *
 */
public class InstanceDataProcessor {

    /**
     * get the instance's information
     */
    public Project getInstanceData(String fileName) {
		FileUtils fileUtils = new FileUtils();		
		
		return fileUtils.loadInstanceInformation(fileName);
	}

}
