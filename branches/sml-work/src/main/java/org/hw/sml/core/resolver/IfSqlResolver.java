package org.hw.sml.core.resolver;

import java.util.List;
import java.util.Map;

import org.hw.sml.support.el.El;
import org.hw.sml.tools.MapUtils;

import com.eastcom_sw.inas.core.service.jdbc.SqlParam;
import com.eastcom_sw.inas.core.service.jdbc.SqlParams;
import com.eastcom_sw.inas.core.service.tools.Assert;
import com.eastcom_sw.inas.core.service.tools.RegexUtils;
/**
 * 复杂逻辑的实现
 * 改变sql走向，借鉴mybatis ibaits语法  不引入ognl xml语法，而是自己实现
 * 减少依赖
 * @author hw
 *后续解决：  if else逻辑    case when 逻辑
 *2016-03-09  empty 把空字符串纳入空
 */
public class IfSqlResolver implements SqlResolver{
	
	private El el;
	public Rst resolve(String dialect, String temp,SqlParams sqlParamMaps) {
		List<String> mathers=null;
		Map<String,Boolean> tempMap=MapUtils.newHashMap();
		Map<String,Object> mapParam=sqlParamMaps.getMap();
		if(temp.contains("<isNotEmpty")){
		//非空函数   \\d*用于嵌套
			mathers=RegexUtils.matchGroup("<isNotEmpty\\d* property=\"\\w+\">",temp);
			for(String mather:mathers){
				String tmt=mather;
				int start=temp.indexOf(tmt);
				if(!temp.contains(tmt)){
					continue;
				}
				//取标签值
				String mark=RegexUtils.subString(tmt, "<", " property=");
				int end=temp.indexOf("</"+mark+">",start);
				Assert.isTrue(end!=-1,mather+" must has end!");
				//整个逻辑字符串 tm
				String tm=temp.substring(start,end+("</"+mark+">").length());
				boolean flag=false;
				if(!tempMap.containsKey(tmt)){
					String property=RegexUtils.subString(tm,"property=\"","\">");
					//内容
					SqlParam sp=sqlParamMaps.getSqlParam(property);
					Assert.notNull(sp, property+" is not config for "+mark);
					flag=sp!=null?(sp.getValue()!=null&&String.valueOf(sp.getValue()).length()>0):false;
					tempMap.put(tmt,flag);
				}else{
					flag=tempMap.get(tmt);
				}
				String content=RegexUtils.subString(tm,">",("</"+mark+">"));
				Assert.notRpeatMark(content,mark);
				if(flag){
					temp=temp.replace(tm, content);
				}else{
					temp=temp.replace(tm," ");
				}
			}
		}
		if(temp.contains("<isEmpty")){
		//空函数
			mathers=RegexUtils.matchGroup("<isEmpty\\d* property=\"\\w+\">",temp);
			for(String mather:mathers){
				String tmt=mather;
				if(!temp.contains(tmt)){
					continue;
				}
				int start=temp.indexOf(tmt);
				String mark=RegexUtils.subString(tmt, "<", " property=");
				int end=temp.indexOf("</"+mark+">",start);
				Assert.isTrue(end!=-1,mather+" must has end!");
				String tm=temp.substring(start,end+("</"+mark+">").length());
				boolean flag=false;
				if(!tempMap.containsKey(tmt)){
					String property=RegexUtils.subString(tm,"property=\"","\">");
					SqlParam sp=sqlParamMaps.getSqlParam(property);
					Assert.notNull(sp, property+" is not config for "+mark);
					flag=sp==null?true:(sp.getValue()==null||String.valueOf(sp.getValue()).length()==0);
					tempMap.put(tmt,flag);
				}else{
					flag=tempMap.get(flag);
				}
				String content=RegexUtils.subString(tm,">",("</"+mark+">"));
				Assert.notRpeatMark(content,mark);
				if(flag){
					temp=temp.replace(tm, content);
				}else{
					temp=temp.replace(tm," ");
				}
			}
		}
		if(temp.contains("<isEqual")){
			//相等函数
			mathers=RegexUtils.matchGroup("<isEqual\\d* property=\"\\w+\" compareValue=\"\\w+\">",temp);
			for(String mather:mathers){
				String tmt=mather;
				if(!temp.contains(tmt)){
					continue;
				}
				int start=temp.indexOf(tmt);
				String mark=RegexUtils.subString(tmt, "<", " property=");
				int end=temp.indexOf("</"+mark+">",start);
				Assert.isTrue(end!=-1,mather+" must has end!");
				String tm=temp.substring(start,end+("</"+mark+">").length());
				String property=RegexUtils.subString(tm,"property=\"","\" compareValue");
				String value=RegexUtils.subString(tm,"compareValue=\"","\">");
				String content=RegexUtils.subString(tm,">",("</"+mark+">"));
				Assert.notRpeatMark(content,mark);
				SqlParam sp=sqlParamMaps.getSqlParam(property);
				Assert.notNull(sp, property+" is not config for "+mark);
				boolean flag=sp==null?false:(value.equals(sp.getValue()));
				if(flag){
					temp=temp.replace(tm, content);
				}else{
					temp=temp.replace(tm," ");
				}
			}
		}
		if(temp.contains("<if")){
			//最复杂函数实现 引入表达示语言实现
			mathers=RegexUtils.matchGroup("<if\\d* test=\"\\s+\\S*\\s+\">",temp);
			for(String mather:mathers){
				String tmt=mather;
				if(!temp.contains(tmt)){
					continue;
				}
				int start=temp.indexOf(tmt);
				String mark=RegexUtils.subString(tmt, "<", " test=");
				int end=temp.indexOf("</"+mark+">",start);
				Assert.isTrue(end!=-1,mather+" must has end!");
				String tm=temp.substring(start,end+("</"+mark+">").length());
				String text=RegexUtils.subString(tm,"test=\"","\">");
				String content=RegexUtils.subString(tm,">",("</"+mark+">"));
				Assert.notRpeatMark(content,mark);
				//对text内容进行处理
				//参数报错直接跳出，减少对数据库的压力
				boolean flag=false;
				if(!tempMap.containsKey(tmt)){
					try {
						flag = el.parser(text,mapParam);
					} catch (Exception e) {//
						throw new IllegalArgumentException("jsElP["+text+"] exception "+e );
					}
					tempMap.put(tmt,flag);
				}else{
					flag=tempMap.get(tmt);
				}
				if(flag){
					temp=temp.replace(tm, content);
				}else{
					temp=temp.replace(tm," ");
				}
			}
		}
		return new Rst(temp);
	}

	public El getEl() {
		return el;
	}

	public void setEl(El el) {
		this.el = el;
	}


}
