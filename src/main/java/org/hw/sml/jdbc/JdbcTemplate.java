package org.hw.sml.jdbc;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.hw.sml.tools.MapUtils;
import org.hw.sml.tools.NumberUtils;

public class JdbcTemplate extends JdbcAccessor{
	public void execute(String sql) throws SQLException{
		Connection con = DataSourceUtils.getConnection(getDataSource());
		Statement stmt = null;
		try{
		stmt = con.createStatement();
		stmt.execute(sql);
		}catch(SQLException  e){
			throw e;
		}finally{
			try{
				if(stmt!=null){
					stmt.close();
				}
			}catch(Exception e){
			}
			DataSourceUtils.releaseConnection();
		}
	}
	public <T> T queryForObject(String sql,Object[] params,RowMapper<T> rowMapper) throws SQLException{
		List<T> result=query(sql, params, rowMapper);
		if(result.size()==0){
			throw new SQLException("not exists objects");
		}
		if(result.size()>1){
			throw new SQLException("has more objects");
		}
		return result.get(0);
	}
	public int queryForInt(String sql,Object[] params) throws SQLException{
		return queryForObject(sql, params, Integer.class);
	}
	public long queryForLong(String sql,Object[] params) throws SQLException{
		return queryForObject(sql, params, Long.class);
	}
	public Map<String,Object> queryForMap(String sql,Object[] params) throws SQLException{
		return queryForObject(sql, params, new MapRowMapper());
	}
	public List<Map<String,Object>> queryForList(String sql,Object[] params) throws SQLException{
		return query(sql, params,new MapRowMapper());
	}
	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String sql,Object[] params,Class<T> clazz) throws SQLException{
		Map<String,Object> result=queryForMap(sql, params);
		return (T) convertValueToRequiredType(result.get(result.keySet().iterator().next()),clazz);
		
	}
	public <T> List<T> query(String sql,Object[] params,RowMapper<T> rowMapper) throws SQLException{
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			stmt=con.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
			stmt.setObject(i, params[i]);
			rs=stmt.executeQuery(sql);
			int i=0;
			List<T> result=MapUtils.newArrayList();
			while(rs.next()){
				T t=rowMapper.mapRow(rs,i++);
				result.add(t);
			}
			return result;
		}catch(SQLException e){
			throw e;
		}finally{
			try{
				if(stmt!=null){
					rs.close();
					stmt.close();
				}
			}catch(Exception e){
			}
			DataSourceUtils.releaseConnection();
		}
	}
	class MapRowMapper implements  RowMapper<Map<String,Object>> {
		public Map<String, Object> mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			Map<String, Object> mapOfColValues = MapUtils.newLinkedHashMap();
			for (int i = 1; i <= columnCount; i++) {
				String key = lookupColumnName(rsmd, i);
				Object obj = getResultSetValue(rs, i);
				mapOfColValues.put(key, obj);
			}
			return mapOfColValues;
		}
		
	}
	public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		}
		else if (obj instanceof Clob) {
			obj = rs.getString(index);
		}
		else if (className != null &&
				("oracle.sql.TIMESTAMP".equals(className) ||
				"oracle.sql.TIMESTAMPTZ".equals(className))) {
			obj = rs.getTimestamp(index);
		}
		else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) ||
					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			}
			else {
				obj = rs.getDate(index);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
	}
	public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1) {
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}
	protected Object convertValueToRequiredType(Object value, Class requiredType) {
		if (String.class.equals(requiredType)) {
			return value.toString();
		}
		else if (Number.class.isAssignableFrom(requiredType)) {
			if (value instanceof Number) {
				return NumberUtils.convertNumberToTargetClass(((Number) value), requiredType);
			}
			else {
				return NumberUtils.parseNumber(value.toString(), requiredType);
			}
		}
		else {
			throw new IllegalArgumentException(
					"Value [" + value + "] is of type [" + value.getClass().getName() +
					"] and cannot be converted to required type [" + requiredType.getName() + "]");
		}
	}
}
