package com.cefetmg.mmc.mrcpsp.runner;

import com.cefetmg.mmc.mrcpsp.process.ExecutionTypeProcessor;
import com.cefetmg.mmc.mrcpsp.process.impl.ExecutionTypeProcessorImpl;

/**
 * @author mrcosta
 * 
 */
public class MmRunner {

	public static void main(String[] args) {
		ExecutionTypeProcessor executionTypeProcessor = new ExecutionTypeProcessorImpl();
		
		executionTypeProcessor.execute();		
	}
}
