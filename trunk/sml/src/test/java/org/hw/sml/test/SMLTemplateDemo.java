package org.hw.sml.test;

import org.hw.sml.core.resolver.Rst;
import org.hw.sml.core.resolver.SqlResolvers;
import org.hw.sml.model.SMLParams;
import org.hw.sml.support.el.JsEl;
import org.junit.Test;


public class SMLTemplateDemo {
	static String sql="select * from table t where 1=1   " +
			"<isNotEmpty property=\"a\"> and t.a=#a#</isNotEmpty>" +
			"<isNotEmpty property=\"b\"> and t.b in(#b#)</isNotEmpty>" +
			"<isNotEmpty property=\"d\"> and t.d in(#d#)</isNotEmpty>" +
			"<if test=\" '@c'!='vv' \"> and t.c like '%'||#c#||'%'</if>" +
			"<jdbcType name=\"d\" type=\"array-char\">'@value'+'100'</jdbcType>";
	@Test
	public  void testSml() {
		SqlResolvers sqlResolvers=new SqlResolvers(new JsEl());
		sqlResolvers.init();
		Rst rst=sqlResolvers.resolverLinks(sql,new SMLParams().add("a","v1").add("b",new String[]{"v2","v3","v4"}).add("c","vvv").add("d","1,2,3,4").reinit());
		rst=sqlResolvers.resolverLinks(sql,new SMLParams().add("a","v1").add("b",new String[]{"v2","v3","v4"}).add("c","vvv").add("d","1,2,3,4").reinit());
		System.out.println(rst.getSqlString());
		System.out.println(rst.getParamObjects());
	}
	
}
