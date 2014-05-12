package mrcpsp.utils;

import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.Project;
import com.esotericsoftware.kryo.Kryo;

/**
 * @author mateus
 *
 */
final class CloneUtils {

	static Project cloneProject(Project projectToClone) {
		Kryo kryo = new Kryo()
	    Project projectCloned = kryo.copy(projectToClone)
		
		return projectCloned
	}
	
	static Job cloneJob(Job jobToClone) {
		Kryo kryo = new Kryo()
	    Job jobCloned = kryo.copy(jobToClone)
		
		return jobCloned
	}
	
	static List<Mode> cloneModeList(List<Mode> modeListToClone) {
		Kryo kryo = new Kryo()
		List<Mode> modeListCloned = kryo.copy(modeListToClone)
		
		return modeListCloned
	}

    static List<Job> cloneJobList(List<Job> jobListToClone) {
        Kryo kryo = new Kryo()
        List<Job> jobListCloned = kryo.copy(jobListToClone)

        return jobListCloned
    }
}
