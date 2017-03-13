package com.eastcom_sw.inas.core.service.jdbc;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class RebuildParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7520900140116309892L;
	//classpath路径名|类名
	private String classpath;
	//jar包完整路径
	private String filepath;
	
	private int type=0;
	//返回参数
	private String[] oriFields;
	//重命名返回参数
	private String[] newFields;
	//分组字段
	private String groupname;
	//分组返回字段
	private String[] groupFields;
	
	
	private Map<String,String> extMap=new LinkedHashMap<String,String>();
	
	

	private String index="0";
	
	
	private int topN=10;
	
	private String orderType="asc";
	
	private String orderName;
	public int getType() {
		return type;
	}

	public RebuildParam setType(int type) {
		this.type = type;
		return this;
	}

	

	public String getGroupname() {
		return groupname;
	}

	public RebuildParam setGroupname(String groupname) {
		this.groupname = groupname;
		return this;
	}

	public String[] getOriFields() {
		return oriFields;
	}

	public RebuildParam setOriFields(String[] oriFields) {
		this.oriFields = oriFields;
		return this;
	}

	public String[] getNewFields() {
		return newFields;
	}

	public RebuildParam setNewFields(String[] newFields) {
		this.newFields = newFields;
		return this;
	}

	public String[] getGroupFields() {
		return groupFields;
	}

	public RebuildParam setGroupFields(String[] groupFields) {
		this.groupFields = groupFields;
		return this;
	}

	public String getIndex() {
		return index;
	}

	public RebuildParam setIndex(String index) {
		this.index = index;
		return this;
	}

	public int getTopN() {
		return topN;
	}

	public RebuildParam setTopN(int topN) {
		this.topN = topN;
		return this;
	}

	public String getOrderType() {
		return orderType;
	}

	public RebuildParam setOrderType(String orderType) {
		this.orderType = orderType;
		return this;
	}

	public String getOrderName() {
		return orderName;
	}

	public RebuildParam setOrderName(String orderName) {
		this.orderName = orderName;
		return this;
	}

	public String getClasspath() {
		return classpath;
	}

	public RebuildParam setClasspath(String classpath) {
		this.classpath = classpath;
		return this;
	}

	public Map<String, String> getExtMap() {
		return extMap;
	}

	public RebuildParam setExtMap(Map<String, String> extMap) {
		this.extMap = extMap;
		return this;
	}
	
	public String get(String extParams){
		return extMap.get(extParams);
	}
	public String get(String extParams,String defaultV){
		return extMap.get(extParams)==null?defaultV:extMap.get(extParams);
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	
	
	
}
