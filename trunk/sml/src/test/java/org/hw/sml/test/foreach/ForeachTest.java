package org.hw.sml.test.foreach;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hw.sml.core.resolver.JsEngine;
import org.hw.sml.core.resolver.Rst;
import org.hw.sml.core.resolver.SqlResolvers;
import org.hw.sml.model.SMLParams;
import org.hw.sml.model.SMLTemplate;
import org.hw.sml.support.el.JsEl;
import org.hw.sml.tools.MapUtils;

public class ForeachTest {
 
	public static void main(String[] args) throws IOException {
		JsEngine.evel("");
		String sql=IOUtils.toString(ForeachTest.class.getResourceAsStream("test.txt"));
		SqlResolvers sqlResolvers=new SqlResolvers(new JsEl());
		sqlResolvers.init();
		long start=System.currentTimeMillis();
		for(int i=0;i<1;i++){
			Rst rst=sqlResolvers.resolverLinks(sql,new SMLParams().add("a","v1").add("cars",new String[]{"a","b","c"}).add("c","vvv").add("d","1,2,3,4").reinit());
			//rst=sqlResolvers.resolverLinks(sql,new SMLParams().add("a","v1").add("b",new String[]{"v2","v3","v4"}).add("c","vvv").add("d","1,2,3,4").reinit());
			//System.out.println(rst.getSqlString());
			//System.out.println(rst.getParamObjects());
		}
		long end=System.currentTimeMillis();
		System.out.println(end-start);
	}
}
