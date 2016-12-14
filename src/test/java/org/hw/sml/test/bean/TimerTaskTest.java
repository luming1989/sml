package org.hw.sml.test.bean;

import java.util.Date;

import org.hw.sml.support.time.TimerTask;
import org.hw.sml.tools.DateTools;

public class TimerTaskTest extends TimerTask{

	public void execute() {
		System.out.println(DateTools.sdf_mi2.format(new Date()));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
