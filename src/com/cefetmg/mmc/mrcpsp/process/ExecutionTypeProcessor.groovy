package com.cefetmg.mmc.mrcpsp.process

interface ExecutionTypeProcessor {
	
	abstract void execute()
	
	abstract void executeOneFile()
	
	abstract void executeOneFileTimes()
	
	abstract void executeAllFiles()
	
	abstract void executeAllFilesTimes()
	
	abstract void executeAllFilesConcurrent()

}
