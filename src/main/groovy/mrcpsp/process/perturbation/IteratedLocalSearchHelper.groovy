package mrcpsp.process.perturbation

import org.apache.log4j.Level
import org.apache.log4j.Logger

/**
 * Created by mateus on 4/26/14.
 */
class IteratedLocalSearchHelper {

    private static final Logger log = Logger.getLogger(IteratedLocalSearch.class)

    List<Integer> getJobIntervals(List<Integer> jobsId, Integer interval) {
        def intervalSize = jobsId.size().intdiv(3)
        def rest = jobsId.size() % 3
        def endSize = intervalSize + rest

        switch (interval) {
            case 1:
                return getJobIdsByRange(jobsId, 0, intervalSize)
                break
            case 2:
                return getJobIdsByRange(jobsId, intervalSize, intervalSize)
                break
            case 3:
                return getJobIdsByRange(jobsId, 2 * intervalSize, endSize)
                break
            default:
                log.log(Level.ERROR, "Invalid Option to get interval: " + IteratedLocalSearch.class)
                throw new IllegalArgumentException("Invalid Option to get interval: " + IteratedLocalSearch.class)
        }
    }

    private def getJobIdsByRange(List<Integer> jobsId, Integer begin, Integer interval) {
        def jobsIntervalId = []
        def count = begin
        interval.times {
            jobsIntervalId.add(jobsId[count])
            count++
        }
        return jobsIntervalId
    }
}
