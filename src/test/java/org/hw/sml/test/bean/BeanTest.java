package org.hw.sml.test.bean;

import org.hw.sml.support.SmlAppContextUtils;
import org.hw.sml.support.ioc.BeanHelper;

public class BeanTest {
	public static void main(String[] args) throws InterruptedException {
		ICar hw=BeanHelper.getBean("carBus");
		System.out.println(SmlAppContextUtils.getSmlContextUtils().query("area-pm", ""));
		SmlAppContextUtils.getSmlContextUtils().getCacheManager().destroy();
	}
}
