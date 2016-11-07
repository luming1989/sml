package org.hw.sml.jdbc.impl;

import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hw.sml.jdbc.DataSourceUtils;
import org.hw.sml.jdbc.JdbcTemplate;
import org.hw.sml.jdbc.ResultSetExtractor;
import org.hw.sml.jdbc.RowMapper;
import org.hw.sml.tools.Assert;
import org.hw.sml.tools.ClassUtil;
import org.hw.sml.tools.MapUtils;

public class DefaultJdbcTemplate extends JdbcTemplate{
	public DefaultJdbcTemplate(){}
	public DefaultJdbcTemplate(DataSource dataSource){
		super(dataSource);
	}
	
	public int update(String sql,Object[] params){
		Connection con=null;
		PreparedStatement pst = null;
		int result=0;
		try{
			con =DataSourceUtils.getConnection(getDataSource());
			pst = con.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					setPreparedState(pst, i+1,params[i]);
				}
			}
			result=pst.executeUpdate();
		}catch(SQLException  e){
			e.printStackTrace();
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				DataSourceUtils.releaseConnection();
			}
		}
		return result;
	}
	public int[] batchUpdate(String sql,List<Object[]> objs){
		Connection con=null;
		PreparedStatement pst = null;
		try {
			con=DataSourceUtils.getConnection(getDataSource());
			con.setAutoCommit(false);
			pst=con.prepareStatement(sql);
			for(int i=0;i<objs.size();i++){
				Object[] params=objs.get(i);
				for(int j=0;j<params.length;j++){
					setPreparedState(pst, j+1, params[j]);
				}
				pst.addBatch();
			}
			int[] result=pst.executeBatch();
			con.commit();
			return result;
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Assert.isTrue(false, e.getMessage());
		}finally{
			try{
				if(pst!=null)
					pst.close();
			}catch(Exception e){
				
			}
			
			DataSourceUtils.releaseConnection();
		}
		return null;
	}
	public <T> T query(String sql, Object[] params, ResultSetExtractor<T> rset) {
		Connection con=null;
		PreparedStatement pst = null;
		ResultSet rs=null;
		try {
			con =DataSourceUtils.getConnection(getDataSource());
			pst = con.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					setPreparedState(pst, i+1,params[i]);
				}
			}
			rs=pst.executeQuery();
			return rset.extractData(rs);
		} catch (SQLException e) {
			Assert.isTrue(false, e.getMessage());
		}finally{
			try{
				if(rs!=null)
				rs.close();
				if(pst!=null)
				pst.close();
			}catch(Exception e){
				
			}finally{
				DataSourceUtils.releaseConnection();
			}
		}
		
		return null;
	}
	public int update(String sql){
		return update(sql,null);
	}
	public <T> T queryForObject(String sql,Object[] params,RowMapper<T> rowMapper){
		List<T> result=query(sql, params, rowMapper);
		if(result.size()==0){
			Assert.isTrue(false,"not exists objects");
		}
		if(result.size()>1){
			Assert.isTrue(false,"has more objects");
		}
		return result.get(0);
	}
	public int queryForInt(String sql,Object[] params){
		return queryForObject(sql, params, Integer.class);
	}
	
	public long queryForLong(String sql,Object[] params) {
		return queryForObject(sql, params, Long.class);
	}
	public Map<String,Object> queryForMap(String sql,Object[] params){
		return queryForObject(sql, params, new MapRowMapper());
	}
	public List<Map<String,Object>> queryForList(String sql,Object[] params){
		return query(sql, params,new MapRowMapper());
	}
	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String sql,Object[] params,Class<T> clazz){
		Map<String,Object> result=queryForMap(sql, params);
		return (T) ClassUtil.convertValueToRequiredType(result.get(result.keySet().iterator().next()),clazz);
	}
	public <T> List<T> queryForList(String sql,Object[] params,Class<T> clazz) throws SQLException{
		List<Map<String,Object>> trs=queryForList(sql, params);
		List<T> result=MapUtils.newArrayList();
		for(Map<String,Object> tr:trs){
			result.add((T)ClassUtil.convertValueToRequiredType(tr.get(tr.keySet().iterator().next()),clazz));
		}
		return result;
	}
	interface BatchPreparedStatementSetter{
		void setValues(PreparedStatement ps, int i) throws SQLException;
		int getBatchSize();
	}
	public void execute(String sql) throws SQLException{
		execute(sql,null);
	}
	public void execute(String sql,Object[] params){
		update(sql, params);
	}
	public <T> List<T> query(String sql,Object[] params,RowMapper<T> rowMapper){
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try{
			con = DataSourceUtils.getConnection(getDataSource());
			stmt=con.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					setPreparedState(stmt, i+1,params[i]);
				}
			}
			rs=stmt.executeQuery();
			int i=0;
			List<T> result=MapUtils.newArrayList();
			while(rs.next()){
				T t=rowMapper.mapRow(rs,i++);
				result.add(t);
			}
			return result;
		}catch(SQLException e){
			e.printStackTrace();
			Assert.isTrue(false, e.getMessage());
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
		return null;
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
	public static void setPreparedState(PreparedStatement ps,int paramIndex,Object inValue) throws SQLException{
		if(inValue==null){
			ps.setNull(paramIndex,Types.NULL);
		}else if (isStringValue(inValue.getClass())) {
			ps.setString(paramIndex, inValue.toString());
		}
		else if (isDateValue(inValue.getClass())) {
			ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
		}
		else if (inValue instanceof Calendar) {
			Calendar cal = (Calendar) inValue;
			ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
		}
		else {
			ps.setObject(paramIndex, inValue);
		}
	}
	private static boolean isStringValue(Class inValueType) {
		// Consider any CharSequence (including StringBuffer and StringBuilder) as a String.
		return (CharSequence.class.isAssignableFrom(inValueType) ||
				StringWriter.class.isAssignableFrom(inValueType));
	}
	private static boolean isDateValue(Class inValueType) {
		return (java.util.Date.class.isAssignableFrom(inValueType) &&
				!(java.sql.Date.class.isAssignableFrom(inValueType) ||
						java.sql.Time.class.isAssignableFrom(inValueType) ||
						java.sql.Timestamp.class.isAssignableFrom(inValueType)));
	}
}
