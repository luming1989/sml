package org.hw.bean;

import org.hw.sml.support.ioc.BeanHelper;

public class BeanTest {
	public static void main(String[] args) {
		TestTask bus=BeanHelper.getBean(TestTask.class);
		System.out.println("------------");
		bus.busss.run();
	}
}
