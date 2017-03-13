package org.hw.sml.support.time;

public abstract class TimerTask extends java.util.TimerTask{
	
	private int times=Integer.MAX_VALUE;
	
	private int counts=1;
	
	private Scheduler scheduler;
	
	public abstract void execute();
	public void run(){
		execute();
		if(counts++==times){
			scheduler.cancel();
		}
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public Scheduler getScheduler() {
		return scheduler;
	}
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	
}
