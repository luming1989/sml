package com.eastcom_sw.inas.core.service.jdbc;
/**
 * @author wen
 */
public interface JsonMapper {
	public <T> T toObj(String json,Class<T> t);
}
