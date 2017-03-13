package org.hw.sml.test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hw.sml.core.SqlMarkupTemplate;
import org.hw.sml.jdbc.JdbcTemplate;
import org.hw.sml.jdbc.impl.DefaultDataSource;
import org.hw.sml.jdbc.impl.DefaultJdbcTemplate;
import org.hw.sml.queryplugin.JsonMapper;
import org.hw.sml.tools.Maps;
import org.junit.Test;



public class SqlMarkupAbstractTemplateDemo {
	@Test
	public static  void testQuery() throws SQLException {
		DefaultDataSource dataSource2=new DefaultDataSource();
		dataSource2.setDriverClassName("oracle.jdbc.driver.OracleDriver");
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
		SqlMarkupTemplate st=new SqlMarkupTemplate();
		st.setDss(dss);
		st.init();
		//System.out.println(jdbcTemplate.queryForList("select 1 from DM_CO_BA_CFG_RCPT_IF where id like '%'||?||'%'", new Object[]{"-test"}));
		for(int i=0;i<10;i++){
			Object obj=st.getSmlContextUtils().query("area-pm",new Maps().put("FLUSHCACHE","true").getMap());
			System.out.println(obj);
			}
	}
	public static void main(String[] args) throws SQLException, InterruptedException {
		testQuery();
	}
}
