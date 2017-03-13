package org.hw.sml.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Callback {
	void call(ResultSet rs,int rowNum)  throws SQLException;
}
