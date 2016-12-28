package org.hw.sml.jdbc.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hw.sml.support.LoggerHelper;
import org.hw.sml.support.ManagedThread;

public class DefaultDataSource implements DataSource{
	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private int initialSize;
	private BlockingQueue<Connection> connections; 
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	class ThreadDaemon extends ManagedThread{
		protected boolean prepare() {
			return true;
		}
		protected void doWorkProcess() {
			try {
				if(connections.size()<initialSize){
					connections.add(getConnectionFromDriver());
				}else{
					Thread.sleep(100);
				}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		protected void cleanup() {
		}
		protected boolean extraExitCondition() {
			return stopFlag;
		}
		
	}
	public void init(){
		if(initialSize>0){
			connections=new ArrayBlockingQueue<Connection>(initialSize);
			LoggerHelper.info(getClass(),"datasource pool initd["+initialSize+"]");
			Thread thread=new ThreadDaemon();
			thread.setName("daemonDsThread");
			thread.setDaemon(true);
			thread.start();
		}

	}
	public Connection getConnection() throws SQLException {
		if(connections!=null&&connections.size()>0){
			try {
				return connections.take();
			} catch (InterruptedException e) {
			}
		}
		return getConnectionFromDriver();
	}
	public Connection getConnectionFromDriver() throws SQLException {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(url, username, password);
	}
	public Connection getConnection(String username, String password)
			throws SQLException {
		return null;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Logger getParentLogger() throws SQLFeatureNotSupportedException{
		return null;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}
	
}
