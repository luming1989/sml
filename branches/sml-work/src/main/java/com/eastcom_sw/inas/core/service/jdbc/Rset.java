package com.eastcom_sw.inas.core.service.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hw.sml.jdbc.ResultSetExtractor;
import org.hw.sml.jdbc.impl.DefaultJdbcTemplate;

import com.eastcom_sw.inas.core.service.jdbc.build.Rslt;

public class Rset implements ResultSetExtractor<Rslt>{
	
	public Rslt extractData(ResultSet rs) throws SQLException{
		 Rslt rt=new Rslt();
		 final List<List<Object>> listData = new ArrayList<List<Object>>();
		 rt.setDatas(listData);
		 ResultSetMetaData rsmd = rs.getMetaData();
	        int iterNum = rsmd.getColumnCount();
	        List<String> header =new ArrayList<String>();
	        for (int i = 0; i < iterNum; i++) {
	          String columnLabel = rsmd.getColumnLabel(i + 1);
	          header.add(columnLabel);
	        }
	        rt.setHeadMetas(header);
	        while (rs.next()) {
	          List<Object> data = new ArrayList<Object>();
	          for (int i = 1; i <= iterNum; i++)
	          {
	            data.add(DefaultJdbcTemplate.getResultSetValue(rs,i));
	          }
	          listData.add(data);
	        }
	        return rt;
	}
	
 };