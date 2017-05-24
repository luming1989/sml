package org.hw.sml.support.time;

import java.util.Date;
import java.util.Timer;

import org.hw.sml.plugin.Plugin;
import org.hw.sml.tools.DateTools;

public class Scheduler implements Plugin{
	
	private TimerTask task;
	
	private long delay=1;
	
	private Timer timer;
	
	private Date firstTime;
	
	private boolean fixedRate=false;
	
	
	public Scheduler(){
		this.timer=new Timer();
		if(firstTime==null){
			firstTime=DateTools.trunc(DateTools.addMinutes(new Date(),1),1000);
		}
	}
	
	public void cancel(){
		this.timer.cancel();
	}

	public void init() {
		if(task==null){
			return;
		}
		
		task.setScheduler(this);
		if(!fixedRate)
			this.timer.schedule(task,firstTime,delay*1000);
		else
			this.timer.schedule(task, firstTime, delay*1000);
	}
	
	public void destroy() {
		this.timer.cancel();
		
	}

	public TimerTask getTask() {
		return task;
	}

	public void setTask(TimerTask task) {
		this.task = task;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Date getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}

	public boolean isFixedRate() {
		return fixedRate;
	}

	public void setFixedRate(boolean fixedRate) {
		this.fixedRate = fixedRate;
	}

	
}
