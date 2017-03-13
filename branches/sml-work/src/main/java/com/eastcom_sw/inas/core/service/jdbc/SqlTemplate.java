package com.eastcom_sw.inas.core.service.jdbc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.eastcom_sw.inas.core.service.tools.SerializationUtils;
/**
 * @author hw
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SqlTemplate")
@XmlRootElement(name = "SqlTemplate")
public class SqlTemplate   implements Cloneable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -251852631091770199L;
	/**
	 * 
	 */
	//
	@XmlElement(name="ID")
	private String id;
	//主体sql
	@XmlElement(name="MAINSQL")
	private String mainSql;
	
	//主体sql
	@XmlElement(name="REBUILD_INFO")
	private String jsonRebuildParamMap;
	
	@XmlElement(name="CONDITION_INFO")
	private String jsonConditionMap;
	
	@XmlElement(name="CACHE_ENABLED")
	private Integer isCache=0;
	
	@XmlElement(name="CACHE_MINUTES")
	private int cacheMinutes=5;
	
	@XmlElement(name="DBID")
	private String dbid;
	@XmlElement(name="DESCRIBE")
	private String remark;
	
	@XmlTransient
	private  RebuildParam rebuildParam;
	@XmlTransient
	private SqlParams SqlParamMap=new SqlParams();
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMainSql() {
		return mainSql;
	}

	public void setMainSql(String mainSql) {
		this.mainSql = mainSql;
	}

	
	public Integer getIsCache() {
		return isCache;
	}

	public void setIsCache(Integer isCache) {
		this.isCache = isCache;
	}

	public int getCacheMinutes() {
		return cacheMinutes;
	}

	public void setCacheMinutes(int cacheMinutes) {
		this.cacheMinutes = cacheMinutes;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getJsonRebuildParamMap() {
		return jsonRebuildParamMap;
	}

	public void setJsonRebuildParamMap(String jsonRebuildParamMap) {
		this.jsonRebuildParamMap = jsonRebuildParamMap;
	}

	public RebuildParam getRebuildParam() {
		if(this.rebuildParam==null){
			this.rebuildParam=new RebuildParam();
		}
		return rebuildParam;
	}

	public void setRebuildParam(RebuildParam rebuildParam) {
		this.rebuildParam = rebuildParam;
	}

	public String getJsonConditionMap() {
		return jsonConditionMap;
	}

	public void setJsonConditionMap(String jsonConditionMap) {
		this.jsonConditionMap = jsonConditionMap;
	}

	public SqlParams getSqlParamMap() {
		return SqlParamMap;
	}

	public void setSqlParamMap(SqlParams sqlParamMap) {
		SqlParamMap = sqlParamMap;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 用于缓存中的参数克隆，不用原始类，有必要做后续深度序列化克隆
	 */
	public SqlTemplate clone() {
		return (SqlTemplate) SerializationUtils.clone(this);
	}
	
}
