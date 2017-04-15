package org.hw.sml.test.bean;

import org.hw.sml.support.ioc.BeanHelper;

public class BeanTest {
	public static void main(String[] args) throws InterruptedException {
		BeanHelper.start();
		System.out.println(BeanHelper.getValue("sml.ssss"));
	}
}
