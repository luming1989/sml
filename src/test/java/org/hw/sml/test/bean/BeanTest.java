package org.hw.sml.test.bean;

import org.hw.sml.support.SmlAppContextUtils;
import org.hw.sml.support.ioc.BeanHelper;
import org.hw.sml.support.time.Scheduler;

public class BeanTest {
	public static void main(String[] args) throws InterruptedException {
		Scheduler hw=BeanHelper.getBean("scheduler");
		System.out.println(hw);
	}
}
