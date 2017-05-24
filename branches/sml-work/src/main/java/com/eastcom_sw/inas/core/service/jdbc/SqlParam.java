package com.eastcom_sw.inas.core.service.jdbc;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.hw.sml.jdbc.JdbcTemplate;

import com.eastcom_sw.inas.core.service.tools.Assert;
import com.eastcom_sw.inas.core.service.tools.DateTools;


public class SqlParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5335788865766697965L;

	public SqlParam(){
		
	}
	public SqlParam(String name,Object value){
		this.name=name;
		this.value=value;
	}
	private JdbcTemplate jdbcTemplate;
	
	private String name;
	
	private String type="char";//类型[date,char,number,array]
	
	private String encode="utf-8";
	/**
	 * 可以为值
	 * 也可为sql,select开头
	 */
	private String defaultValue;
	
	private String format;
	
	private Object value;
	
	private int orderIndex;
	
	private String descr;
	
	private Integer enabled=0;
	
	private String split=",";
	
	private String id;
	
	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}


	
	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String toString(){
		return name+":"+value;
	}

	public void handlerValue(String value2) {
		if(this.type.equals("date")){
			this.value=DateTools.parse(value2);
		}else if(this.type.equals("time")){
			this.value=getTime(DateTools.parse(value2));
		}else if(this.type.equals("array")){
			this.value=buildStr(value2);
		}else if(this.type.equals("array-char")||this.type.equals("array_char")){
			this.value=value2.split(split);
		}else if(this.type.equals("array-date")||this.type.equals("array-time")||this.type.equals("array_date")){
			String vs[]=value2.split(split);
			Object[] objs=new Object[vs.length];
			for(int i=0;i<vs.length;i++){
				objs[i]=this.type.endsWith("date")?DateTools.parse(vs[i]):getTime(DateTools.parse(vs[i]));
			}
			this.value=objs;
		}else if(this.type.equals("timestamp")){
			this.value=new Timestamp(DateTools.parse(value2).getTime());
		}else{
			this.value=value2;
		}
	}
	private Time getTime(Date date){
		Assert.notNull(date,"params "+this.name+" is time but is null");
		return new Time(date.getTime());
	}

	public void handlerDefaultValue(JdbcTemplate jdbc) {
		if(defaultValue!=null&&this.defaultValue.toLowerCase().startsWith("select")&&this.defaultValue.toLowerCase().contains("from")){
			this.value=jdbc.queryForMap(defaultValue,new Object[]{}).get("RESULT");
		}else if(defaultValue!=null){
			handlerValue(defaultValue);
		}
		
	}
	
	private String buildStr(String val){
		StringBuffer sb=new StringBuffer();
		String[] vs=val.split(split);
		for(int i=0;i<vs.length;i++){
			sb.append("'"+vs[i]+"'");
			if(i<vs.length-1){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
