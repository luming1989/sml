package org.hw.sml.test.bean;

import java.util.Date;

import org.hw.sml.support.time.TimerTask;
import org.hw.sml.test.mq.MqClient;
import org.hw.sml.tools.DateTools;

public class TimerTaskTest extends TimerTask{
	private MqClient mqClient;
	public void execute() {
		String key=DateTools.sdf_mi2.format(new Date());
		mqClient.send(key);
		System.out.println(key);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
