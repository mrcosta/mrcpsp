package mrcpsp.process;

import mrcpsp.model.main.Project;
import mrcpsp.utils.FileUtils;

/**
 * @author mrcosta
 *
 */
public class InstanceDataProcessor {
	
	public Project getInstanceData(String fileName) {
		FileUtils fileUtils = new FileUtils();		
		
		return fileUtils.loadInstanceInformation(fileName);
	}

}
