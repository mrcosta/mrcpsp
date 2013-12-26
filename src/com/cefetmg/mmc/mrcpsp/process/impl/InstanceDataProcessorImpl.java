package com.cefetmg.mmc.mrcpsp.process.impl;

import com.cefetmg.mmc.mrcpsp.model.main.Project;
import com.cefetmg.mmc.mrcpsp.process.InstanceDataProcessor;
import com.cefetmg.mmc.mrcpsp.utils.FileUtils;

/**
 * @author mrcosta
 *
 */
public class InstanceDataProcessorImpl implements InstanceDataProcessor {
	
	@Override
	public Project getInstanceData(String fileName) {
		FileUtils fileUtils = new FileUtils();		
		
		return fileUtils.loadInstanceInformation(fileName);
	}

}
