package org.hw.sml.test.bean;

import java.util.Arrays;

import org.hw.sml.core.SqlMarkupTemplate;
import org.hw.sml.support.ioc.BeanHelper;
import org.hw.sml.support.time.Scheduler;
import org.hw.sml.tools.RegexUtils;

public class BeanTest {
	public static void main(String[] args) throws InterruptedException {
		SqlMarkupTemplate hw=BeanHelper.getBean("sml");
		System.out.println(hw.getSmlContextUtils().query("area-pm",""));
		hw.getCacheManager().destroy();
		/*String str="aaaaa_9,2,3-2015-02-19-1220_201502191510_00021321_12314.xml";
		String match="aaaaa_(.*?)_(.\\d+?)_00021321_(.\\d{1,5}).xml";
		String[] obj=RegexUtils.matchSubString(match, str);
		System.out.println(Arrays.asList(obj));*/
	}
}
