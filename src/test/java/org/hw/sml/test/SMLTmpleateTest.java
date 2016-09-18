package org.hw.sml.test;

import org.hw.sml.core.resolver.Rst;
import org.hw.sml.core.resolver.SqlResolvers;
import org.hw.sml.model.SMLParams;
import org.hw.sml.support.el.JsEl;

public class SMLTmpleateTest {
	static String sql="select * from table t where 1=1 <isNotEmpty property=\"a\"> and t.a=#a#</isNotEmpty><isNotEmpty property=\"b\"> and t.b in(#b#)</isNotEmpty>";
	public static void main(String[] args) {
		System.out.println(sql);
		SqlResolvers sqlResolvers=new SqlResolvers(new JsEl());
		sqlResolvers.init();
		Rst rst=sqlResolvers.resolverLinks(sql,SMLParams.newSMLParams().add("a","v1").add("b",new String[]{"v2","v3","v4"}).reinit());
		System.out.println(rst.getSqlString());
		System.out.println(rst.getParamObjects());
	}
}
