package mrcpsp.utils;


/**
 * @author mrcosta
 *
 */
public final class ChronoWatch {
	
	private static ChronoWatch instance;
	private long start;
	private long finish;
	private long time;
	private String name;
	
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
		this.finish = System.currentTimeMillis();
		this.time = finish - start;
		long seconds = ((time / 1000) % 60);
		
		return String.format("%s DONE(%d ms) - DONE(%d s)", name, time, seconds);
	}

	public long getStart() {
		return start;
	}

	public long getFinish() {
		return finish;
	}

	public long getTime() {
		return time;
	}

	public String getName() {
		return name;
	}
	
}
