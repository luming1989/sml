package org.hw.sml.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hw.sml.tools.Assert;



public class DataSourceUtils {
	private static  ThreadLocal<Connection> connections=new ThreadLocal<Connection>();
	public static Connection getConnection(DataSource dataSource) throws SQLException{
		return doGetConnection(dataSource);
	}
	public static Connection doGetConnection(DataSource dataSource) throws SQLException {
		Assert.notNull(dataSource, "No DataSource specified");
		Connection conn=connections.get();
		if(conn==null){
			conn=dataSource.getConnection();
			connections.set(conn);
		}
		return conn;
	}
	public static void releaseConnection(DataSource dataSource) {
		Connection conn=connections.get();
		if(conn!=null){
			try {
				if(!conn.isClosed())
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connections.remove();
			}
		}
	}
	

}
