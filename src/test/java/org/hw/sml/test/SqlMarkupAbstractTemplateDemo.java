package org.hw.sml.test;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hw.sml.core.SqlMarkupAbstractTemplate;
import org.hw.sml.core.SqlMarkupTemplate;
import org.hw.sml.jdbc.JdbcTemplate;
import org.hw.sml.jdbc.impl.DefaultDataSource;
import org.hw.sml.jdbc.impl.DefaultJdbcTemplate;
import org.hw.sml.support.SmlAppContextUtils;
import org.hw.sml.tools.MapUtils;
import org.junit.Test;



public class SqlMarkupAbstractTemplateDemo {
	@Test
	public static  void testQuery() throws SQLException {
		DefaultDataSource dataSource2=new DefaultDataSource();
		dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource2.setUrl("jdbc:oracle:thin:@10.221.247.43:1521/ipms");
		dataSource2.setUsername("ipmsdm");
		dataSource2.setPassword("SHipmsdm!23$");
		DefaultDataSource dataSource = new DefaultDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://23.247.25.117:3306/hw");
		dataSource.setUsername("root");
		dataSource.setPassword("hlw");
		//库集
		Map<String,DataSource> dss=new HashMap<String,DataSource>();
		dss.put("defJt", dataSource2);
		JdbcTemplate jdbcTemplate=new DefaultJdbcTemplate();
		jdbcTemplate.setDataSource(dataSource2);
		//System.out.println(jdbcTemplate.queryForList("select 1 from DM_CO_BA_CFG_RCPT_IF where id like '%'||?||'%'", new Object[]{"-test"}));
		
		System.out.println(jdbcTemplate.queryForObject("select sysdate from dual",Date.class));
		List<Object[]> objs=MapUtils.newArrayList();
		for(int i=0;i<100000;i++){
			objs.add(new Object[]{"a"+i,"b"+i*2});
			if(i==99999){//471266
				objs.add(new Object[]{"","a"});
			}
		}
		//int[] i=jdbcTemplate.batchUpdate("insert into hw_temp values(?,?)", objs);
		//System.out.println(i.length+"|"+i[0]+"|"+i[1]);
		//对象
		SqlMarkupAbstractTemplate jf=new SqlMarkupTemplate();
		
		jf.setDss(dss);//
		jf.init();
		//使用核心方法-迪士尼客流量查询
		try{
			for(int i=0;i<1;i++){
			Object data=SmlAppContextUtils.getSmlContextUtils().query("test-query-links", new HashMap<String,String>(){{
				put("isRemoteParams","true");
				put("opLinksV","delete");
				put("opLinks","delete${type=4}-insert${type=1|oriFields=SP_IP~SP_NAME|newFields=a~b}-update${1}");
			}});
			System.out.println(data);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//对象销毁操作
			jf.destroy();
		}
		/*int i=jf.getSmlContextUtils().update("test-update-links", new HashMap<String,String>(){
			{
				put("sp_ip","女儿2");
				put("sp_name","黄丽文");
				put("sp_name_update","黄楚伊");
			}
		});
		System.out.println(i);*/
	}
	public static void main(String[] args) throws SQLException {
		testQuery();
	}
}
