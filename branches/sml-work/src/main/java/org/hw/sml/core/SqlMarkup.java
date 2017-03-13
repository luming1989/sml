package org.hw.sml.core;

import java.util.List;
import java.util.Map;

import org.hw.sml.jdbc.JdbcTemplate;
import org.hw.sml.plugin.Plugin;
import org.hw.sml.support.cache.CacheManager;

import com.eastcom_sw.inas.core.service.jdbc.SqlTemplate;
import com.eastcom_sw.inas.core.service.jdbc.build.Rslt;


public interface  SqlMarkup extends Plugin{

	 static final String CACHE_PRE="jdbc";
	
	 JdbcTemplate getJdbc(String dbid);
	
	 SqlTemplate getSqlTemplate(String id);
	
	 List<Map<String,Object>> mergeSql(SqlTemplate st);
	
	 Object builder(SqlTemplate st);
	
	 CacheManager getCacheManager();
	
	 Rslt queryRslt(SqlTemplate st);
	 
	 int update(SqlTemplate st);
}
