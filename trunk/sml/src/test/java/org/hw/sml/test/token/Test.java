package org.hw.sml.test.token;

import java.util.Arrays;

import org.hw.sml.tools.Strings;

public class Test {
	static String test= "(sml).a.getJdbc('def.Jt').b.dataSource.toString('....').equals(#{sml.data().a().a.b.c(#{a.b.c().end.c.e})}).toString().length()";
	//
	public static void  main(String[] args) {
		String[] str=new Strings(test).splitToken('.','(',')');
		System.out.println(Arrays.asList(str));
		//test.concat(str)
		System.out.println("aaaa".startsWith(""));
	}
}
