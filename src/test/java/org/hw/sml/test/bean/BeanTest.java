package org.hw.sml.test.bean;

import org.hw.sml.core.SqlMarkupTemplate;
import org.hw.sml.support.ioc.BeanHelper;

public class BeanTest {
	public static void main(String[] args) throws InterruptedException {
		//SqlMarkupTemplate hw=BeanHelper.getBean("sml");
		/*String str="aaaaa_9,2,3-2015-02-19-1220_201502191510_00021321_12314.xml";
		String match="aaaaa_(.*?)_(.\\d+?)_00021321_(.\\d{1,5}).xml";
		String[] obj=RegexUtils.matchSubString(match, str);
		System.out.println(Arrays.asList(obj));*/
		ICar car=BeanHelper.getBean(Car.class);
		//car.run();
	}
}
