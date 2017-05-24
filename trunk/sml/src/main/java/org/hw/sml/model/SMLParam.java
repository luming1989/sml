package org.hw.sml.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hw.sml.tools.DateTools;


public class SMLParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5335788865766697965L;

	
	public SMLParam() {
		super();
	}

	public SMLParam(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}
	private String name;
	
	private String type="char";//类型[date,char,number,array]
	
	private String encode="utf-8";
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
		if(value2==null){
			return;
		}
		this.value=convertValue(this.type, value2);
	}
	public Object convertValue(String typev,String value2){
		Object result=null;
		if(typev.equals("date")){
			result=DateTools.parse(value2);
		}else if(typev.equals("array")){
			result=buildStr(value2);
		}else if(typev.equals("array-char")||typev.equals("array_char")){
			result=value2.split(split);
		}else if(typev.equals("array-date")||typev.equals("array_date")){
			String vs[]=value2.split(split);
			Object[] objs=new Object[vs.length];
			for(int i=0;i<vs.length;i++){
				objs[i]=DateTools.parse(vs[i]);
			}
			result=objs;
		}else if(typev.equals("timestamp")){
			result=new Timestamp(DateTools.parse(value2).getTime());
		}else{
			result=value2;
		}
		return result;
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
