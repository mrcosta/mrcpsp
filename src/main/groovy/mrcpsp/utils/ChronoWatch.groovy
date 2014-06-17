package mrcpsp.utils

import java.text.SimpleDateFormat

/**
 * @author mrcosta
 *
 */
class ChronoWatch {
	
	static ChronoWatch instance;
	long start;
	long finish;
	long time;
    String totalTimeExecutionFormated

    long startSolution
    long finishSolution
    long timeSolution
    long totalTimeSolution
    String totalTimeSolutionFormated

	static ChronoWatch getInstance() {
		
		if (instance == null) {
			return instance = new ChronoWatch();
		} else {
            return instance
        }
	}
	
	def start() {
		start = System.currentTimeMillis();
		finish = 0;
		time = 0;
	}
	
	def String getTimeExecution() {
		finish = System.currentTimeMillis()
		time = finish - start
        def sfm

        sfm = new SimpleDateFormat("mm:ss:SSS").format(new Date(time))
        totalTimeExecutionFormated = "$time--$sfm"

		return "$time--$sfm"
	}

    def startSolutionTime() {
        startSolution = System.currentTimeMillis();
        finishSolution = 0;
        timeSolution = 0;
    }

    def pauseSolutionTime() {
        finishSolution = System.currentTimeMillis()
        timeSolution = finishSolution - startSolution
        totalTimeSolution+= timeSolution

        def sfm

        sfm = new SimpleDateFormat("mm:ss:SSS").format(new Date(totalTimeSolution))
        totalTimeSolutionFormated = "$totalTimeSolution--$sfm"

        return totalTimeSolutionFormated
    }
}
