package org.hw.sml.support.el;

import java.util.Map;

import org.hw.sml.tools.MapUtils;

public abstract class ElContext{
	public ElContext(){
		addBean("smlElHelper",this);
	}
	protected Map<String,Object> beanMap=MapUtils.newHashMap();
	
	protected Map<String,String> properties=MapUtils.newHashMap();
	@SuppressWarnings("unchecked")
	public  <T extends ElContext>T withBeanMap( Map<String,Object> beanMap){
		this.beanMap=beanMap;
		addBean("elContext",this);
		return (T)this;
	}
	@SuppressWarnings("unchecked")
	public  <T extends ElContext>T withProperties(Map<String,String> properties){
		this.properties=properties;
		return (T)this;
	}
	@SuppressWarnings("unchecked")
	public  <T extends ElContext>T  addProperty(String key,String value){
		properties.put(key, value);
		return (T)this;
	}
	@SuppressWarnings("unchecked")
	public <T extends ElContext>T addBean(String key,Object value){
		beanMap.put(key, value);
		return (T)this;
	}
	public  Object evel(String elp) throws ElException{
		BeanType b= evelBeanType(elp);
		return b==null?null:b.getV();
	}
	public Object getBean(String key){
		return beanMap.get(key);
	}
	public String getValue(String key){
		return properties.get(key);
	}
	public abstract BeanType evelBeanType(String elp) throws ElException;
}
