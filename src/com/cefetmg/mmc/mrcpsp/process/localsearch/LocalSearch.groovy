package com.cefetmg.mmc.mrcpsp.process.localsearch

import com.cefetmg.mmc.mrcpsp.model.main.Project

interface LocalSearch {
	
	abstract Project executeLocalSearch(Project project)

}
