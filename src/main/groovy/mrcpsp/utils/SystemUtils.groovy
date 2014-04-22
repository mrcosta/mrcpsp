package mrcpsp.utils;

import org.apache.log4j.Logger;

/**
 * @author mrcosta
 *
 */
final class SystemUtils {
	
	static final Logger log = Logger.getLogger(SystemUtils.class);
	
	static String getJavaHomeDir() {
        return System.getProperty("java.home")
    }

    static String getJavaInformation() {
        return System.getProperty("java.version") + " - " + System.getProperty("java.vendor")
    }

    static String getOsInfo() {
        return System.getProperty("os.name") + " - " +
               System.getProperty("os.version") + " - " +
               System.getProperty("os.arch")
    }

    static int getNumberOfAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors()
    }

    static String getUserName() {
        return System.getProperty("user.name")
    }

    static String getMemoryInfo() {
        /* Total amount of free memory available to the JVM */
        long free = Runtime.getRuntime().freeMemory()
        long total = Runtime.getRuntime().totalMemory()
        long max = Runtime.getRuntime().maxMemory()
        return String.format("(bytes) Free: %d(%2.1f) | Used: %d | Total: %d | Max: %d",
            free, ((float)free/total), total-free, total, max)
    }
    
    static void getSystemInformation() {
        log.info("System information: ")
        log.info("JAVA Home: " + SystemUtils.getJavaHomeDir())
        log.info("JAVA version: " + SystemUtils.getJavaInformation())
        log.info("OS: " + SystemUtils.getOsInfo())
        log.info("Available processors: " + SystemUtils.getNumberOfAvailableProcessors())
        log.info("Memory info: " + SystemUtils.getMemoryInfo())
        log.info("User: " + SystemUtils.getUserName())
    }

}
