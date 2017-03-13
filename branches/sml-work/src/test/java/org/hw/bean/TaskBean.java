package org.hw.bean;

import org.hw.sml.support.ManagedThread;
import org.hw.sml.support.SmlAppContextUtils;
import org.hw.sml.support.ioc.annotation.Init;

public class TaskBean extends ManagedThread{
	private static int count=0;
	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	protected void doWorkProcess() {
			System.out.println(count++);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(count==6){
				this.stopFlag=true;
			}
	}

	@Override
	protected void cleanup() {
		SmlAppContextUtils.getSqlMarkupAbstractTemplate().destroy();
	}

	@Override
	protected boolean extraExitCondition() {
		return stopFlag;
	}
	@Init
	public void init(){
		this.start();
	}
}
