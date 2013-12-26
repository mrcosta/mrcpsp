package com.cefetmg.mmc.mrcpsp.process

import com.cefetmg.mmc.mrcpsp.model.main.Project



/**
 * @author mrcosta
 *
 */
interface MmProcessor {
	
	/**
     * generate a solution using GRASP
     */    
    abstract Project initialSolutionWithGrasp(String fileName)
    
    abstract Project localSearchDescentUphillMethod(Project project)
    
    abstract Project localSearchDescentUphillMethod()
    
    abstract boolean executeCheckRestrictionsAndGetJobTimes()
    
    abstract boolean executeCheckRestrictionsAndGetJobTimes(Project project)
    
    abstract void executeWriteResults()	
	
	abstract boolean setProjectMakespan()
}
