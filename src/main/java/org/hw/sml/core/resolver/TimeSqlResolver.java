package org.hw.sml.core.resolver;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hw.sml.support.el.El;

import com.eastcom_sw.inas.core.service.jdbc.SqlParams;
import com.eastcom_sw.inas.core.service.tools.DateTools;
import com.eastcom_sw.inas.core.service.tools.RegexUtils;
/**
 * 时间处理类，用于时间的简化操作
 * 可考虑sql内引入这种时间，相对直观，减少数据函数的依赖
 * @author hw
 *后续引入sql中进行实现
 */
public class TimeSqlResolver implements SqlResolver {
	public  Object[] inner(String mather){
		String temp=mather.substring(mather.indexOf("(")+1,mather.indexOf(")"));
		if(temp==null||temp.trim().length()==0){
			return new Object[]{0,true};
		}else{
			String[] s=temp.split(",");
			return new Object[]{Integer.parseInt(s[0]),s.length==2?s[1]:true};
		}
	}
	public String timeSwitchBuilder(String dialect,Date date){
		String time=DateTools.sdf_mis.format(date);
		if(dialect.equals("SYBASEIQ")){
			return "cast('"+time+"' as timestamp)";
		}else if(dialect.equals("ORACLE")){
			return "to_timestamp('"+time+"','yyyy-mm-dd hh24:mi:ss.ff')";
		}
		return "to_date('"+time+"','yyyy-mm-dd hh24:mi:ss')";
	}
	private Date isNotLe(boolean flag,Date time){
		if(flag){
			return time;
		}
		return DateTools.addMills(time,-1);
	}
	////时间函数选择
	//min() hour() , day(), week(), month()
	public Rst resolve(String dialect, String sql, SqlParams sqlParamMaps) {
		String temp=sql;
		//"min\\(-?\\d*\\)"
		List<String> mathers=null;
		mathers=RegexUtils.matchGroup("minute\\(-?\\d*,?(true|false)?\\)",temp);
		for(String mather:mathers){
			Object[] it=inner(mather);
			int i=Integer.parseInt(String.valueOf(it[0]));
			boolean t=Boolean.valueOf(String.valueOf(it[1]));
			Date time=DateTools.addMinutes(DateTools.trunc(new Date(),Calendar.MINUTE), i);
			time=isNotLe(t, time);
			String replaceTime=timeSwitchBuilder(dialect,time);
			temp=temp.replace(mather,replaceTime);
		}
	    mathers=RegexUtils.matchGroup("hour\\(-?\\d*,?(true|false)?\\)",temp);
	    for(String mather:mathers){
	    	Object[] it=inner(mather);
			int i=Integer.parseInt(String.valueOf(it[0]));
			boolean t=Boolean.valueOf(String.valueOf(it[1]));
			Date time=DateTools.addHours(DateTools.trunc(new Date(),Calendar.HOUR_OF_DAY), i);
			time=isNotLe(t, time);
			String replaceTime=timeSwitchBuilder(dialect,time);
			temp=temp.replace(mather,replaceTime);
		}
	    mathers=RegexUtils.matchGroup("day\\(-?\\d*,?(true|false)?\\)",temp);
	    for(String mather:mathers){
	    	Object[] it=inner(mather);
			int i=Integer.parseInt(String.valueOf(it[0]));
			boolean t=Boolean.valueOf(String.valueOf(it[1]));
			Date time=DateTools.addDays(DateTools.trunc(new Date(),Calendar.DAY_OF_MONTH), i);
			time=isNotLe(t, time);
			String replaceTime=timeSwitchBuilder(dialect,time);
			temp=temp.replace(mather,replaceTime);
		}
	    mathers=RegexUtils.matchGroup("week\\(-?\\d*,?(true|false)?\\)",temp);
	    for(String mather:mathers){
	    	Object[] it=inner(mather);
			int i=Integer.parseInt(String.valueOf(it[0]));
			boolean t=Boolean.valueOf(String.valueOf(it[1]));
			Date time=DateTools.addDays(DateTools.trunc(new Date(),Calendar.DAY_OF_WEEK), i*7);
			time=isNotLe(t, time);
			String replaceTime=timeSwitchBuilder(dialect,time);
			temp=temp.replace(mather,replaceTime);
		}
	    mathers=RegexUtils.matchGroup("month\\(-?\\d*,?(true|false)?\\)",temp);
	    for(String mather:mathers){
	    	Object[] it=inner(mather);
			int i=Integer.parseInt(String.valueOf(it[0]));
			boolean t=Boolean.valueOf(String.valueOf(it[1]));
			Date time=DateTools.addMonths(DateTools.trunc(new Date(),Calendar.MONTH), i);
			time=isNotLe(t, time);
			String replaceTime=timeSwitchBuilder(dialect,time);
			temp=temp.replace(mather,replaceTime);
		}
		return new Rst(temp);
	}
	@Override
	public void setEl(El el) {
		// TODO Auto-generated method stub
		
	}
	
	
}
