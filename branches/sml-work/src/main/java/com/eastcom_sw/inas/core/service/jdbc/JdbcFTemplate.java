package com.eastcom_sw.inas.core.service.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.FrameworkConstant.Type;
import org.hw.sml.jdbc.RowMapper;



public class JdbcFTemplate extends  AbstractJdbcFTemplate{	
	public SqlTemplate getSqlTemplate(String id){
		String key=CACHE_PRE+":"+id+":getSqlTemplate";
		if(getCacheManager().get(key)==null){
			SqlTemplate sqt= getSqlTemplateWithOutCache(id);
			getCacheManager().set(key,sqt,cacheMinutes);
		}
		SqlTemplate stp= ((SqlTemplate) getCacheManager().get(key)).clone();
		reInitSqlTemplate(stp);
		return stp;
	}
	
	private SqlTemplate getSqlTemplateWithOutCache(String id) {
		try{
			SqlTemplate sqt= getJdbc("defJt").queryForObject(FrameworkConstant.getSupportKey(frameworkMark,Type.FRAMEWORK_CFG_JDBC_SQL),new Object[]{id},new RowMapper<SqlTemplate>(){
				public SqlTemplate mapRow(ResultSet rs, int arg1)
						throws SQLException {
					SqlTemplate st=new SqlTemplate();
					st.setId(rs.getString("id"));
					st.setMainSql(rs.getString("mainsql"));
					String rebuildParam=rs.getString("jsonrebuildparammap");
					st.setJsonRebuildParamMap(rebuildParam);
					String jsonConditionMap=rs.getString("jsonconditionmap");
					st.setJsonConditionMap(jsonConditionMap);
					st.setIsCache(rs.getInt("isCache"));
					st.setCacheMinutes(rs.getInt("cacheMinutes"));
					st.setDbid(rs.getString("dbid"));
					return st;
				}
			});
			return sqt;
		}catch(Exception e){
			throw new IllegalArgumentException("ifId:["+id+"] not exists or can't get ifInfo from datasource!"+",detail:"+e.getMessage());
		}
	}
	
	
	
}
