package mrcpsp.utils;

import java.util.List;

import mrcpsp.model.main.Job;
import mrcpsp.model.main.Mode;
import mrcpsp.model.main.Project;
import com.esotericsoftware.kryo.Kryo;

/**
 * @author mateus
 *
 */
public final class CloneUtils {

	public static Project cloneProject(Project projectToClone) {
		Kryo kryo = new Kryo();
	    Project projectCloned = kryo.copy(projectToClone);
		
		return projectCloned;
	}
	
	public static Job cloneJob(Job jobToClone) {
		Kryo kryo = new Kryo();	    
	    Job jobCloned = kryo.copy(jobToClone);
		
		return jobCloned;
	}
	
	public static List<Mode> cloneModeList(List<Mode> modeListToClone) {
		Kryo kryo = new Kryo();	    
		List<Mode> modeListCloned = kryo.copy(modeListToClone);
		
		return modeListCloned;
	}

}
