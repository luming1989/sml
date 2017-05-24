package org.hw.sml.core.resolver;

import java.util.List;
import java.util.Map;

public class Rst{
	
	private String sqlString;
	
	private List<Object> paramObjects;
	
	private Map<String,Object> extInfo;
	
	public Rst() {
		super();
	}
	
	public Rst(String sqlString) {
		super();
		this.sqlString = sqlString;
	}

	public Rst(String sqlString, List<Object> paramObjects) {
		super();
		this.sqlString = sqlString;
		this.paramObjects = paramObjects;
	}

	public String getSqlString() {
		return sqlString;
	}
	public void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}
	public List<Object> getParamObjects() {
		return paramObjects;
	}
	public void setParamObjects(List<Object> paramObjects) {
		this.paramObjects = paramObjects;
	}

	public Map<String, Object> getExtInfo() {
		return extInfo;
	}

	public Rst setExtInfo(Map<String, Object> extInfo) {
		this.extInfo = extInfo;
		return this;
	}
	
}
