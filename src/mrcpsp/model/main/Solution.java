package mrcpsp.model.main;

import java.util.HashMap;

/**
 * @author mrcosta
 *
 */
public class Solution {
	
	private static HashMap<String, Job> jobs;
	
	public Solution() {
		jobs = new HashMap<String, Job>();
	}

	public static HashMap<String, Job> getJobs() {
		return jobs;
	}

	public static void setJobs(HashMap<String, Job> jobs) {
		Solution.jobs = jobs;
	}
	
}
