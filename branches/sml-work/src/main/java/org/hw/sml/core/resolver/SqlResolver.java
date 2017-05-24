package org.hw.sml.core.resolver;

import org.hw.sml.support.el.El;

import com.eastcom_sw.inas.core.service.jdbc.SqlParams;

/**
 * 定义sql语句解析父类
 * @author hw
 *主要实现类有   逻辑判断类，select子sql语句，参数绑定类
 *后续，增加时间处理类，更好操作数据库时间
 *完善参数   sql的使用
 */
public interface SqlResolver{
	/**
	 * 
	 * @param dialect 数据库类型
	 * @param sql     sql
	 * @return
	 */
	public Rst resolve(String dialect, String sql,SqlParams sqlParamMaps);
	void setEl(El el);
	
}

