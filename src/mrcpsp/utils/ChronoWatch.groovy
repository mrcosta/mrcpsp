package mrcpsp.utils

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit;


/**
 * @author mrcosta
 *
 */
class ChronoWatch {
	
	static ChronoWatch instance;
	long start;
	long finish;
	long time;
	String name;
	
	private ChronoWatch(String name) {
		this.name = name;
	}
	
	public static ChronoWatch getInstance(String name) {
		
		if (instance == null) {
			instance = new ChronoWatch(name);
		}
		
		instance.name = name;
		return instance;
	}
	
	public ChronoWatch start() {
		this.start = System.currentTimeMillis();
		this.finish = 0;
		this.time = 0;		
		return this;
	}
	
	public String time() {
		this.finish = System.currentTimeMillis()
		this.time = finish - start
        def sfm

        sfm = new SimpleDateFormat("mm:ss:SSS").format(new Date(time))

		return "$name DONE($time ms) - TIME: $sfm"
	}
}
