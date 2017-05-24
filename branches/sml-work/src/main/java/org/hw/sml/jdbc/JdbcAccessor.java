package org.hw.sml.jdbc;

import javax.sql.DataSource;

public abstract class JdbcAccessor {
	protected DataSource dataSource;
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
}
