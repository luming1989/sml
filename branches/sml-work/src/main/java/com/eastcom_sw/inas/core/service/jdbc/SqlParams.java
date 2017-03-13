package com.eastcom_sw.inas.core.service.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eastcom_sw.inas.core.service.tools.MapUtils;
/**
 * 
 */
public class SqlParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1794719971434841200L;
	private List<SqlParam> sqlParams=new ArrayList<SqlParam>();
	private Map<String,SqlParam> mapParams=new LinkedHashMap<String,SqlParam>();
	public List<SqlParam> getSqlParams() {
		return sqlParams;
	}
	public SqlParams reinit(){
		setSqlParams(getSqlParams());
		return this;
	}
	public void setSqlParams(List<SqlParam> sqlParams) {
		for(SqlParam sp:sqlParams){
			mapParams.put(sp.getName(),sp);
		}
		this.sqlParams =sqlParams;
		mapParams=MapUtils.sort(mapParams);//解决字符串替换时的问题
	}
	public SqlParam getSqlParam(String name){
		return mapParams.get(name);
	}
	public SqlParam getSqlParamFromList(String name){
		for(SqlParam sqlParam:sqlParams){
			if(sqlParam.getName().equals(name)){
				return sqlParam;
			}
		}
		return null;
	}
	public Map<String, Object> getMap() {
		Map<String,Object> varMap=new LinkedHashMap<String, Object>();
		for(Map.Entry<String,SqlParam> entry:this.mapParams.entrySet()){
			varMap.put(entry.getKey(),entry.getValue().getValue());
		}
		return varMap;
	}
	public Map<String, SqlParam> getMapParams() {
		return mapParams;
	}

	public void setMapParams(Map<String, SqlParam> mapParams) {
		this.mapParams = mapParams;
	}
	public SqlParams add(String name, String value, String type) {
		SqlParam sqlParam=new SqlParam(name, value);
		sqlParam.setType(type);
		sqlParam.handlerValue(value);
		this.sqlParams.add(sqlParam);
		return this;
	}
	
	
}
