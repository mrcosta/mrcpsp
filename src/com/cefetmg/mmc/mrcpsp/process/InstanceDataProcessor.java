package com.cefetmg.mmc.mrcpsp.process;

import com.cefetmg.mmc.mrcpsp.model.main.Project;

/**
 * @author mrcosta
 *
 */
public interface InstanceDataProcessor {
	
	/**
	 * get the instance's information
	 */
	public abstract Project getInstanceData(String fileName);

}
