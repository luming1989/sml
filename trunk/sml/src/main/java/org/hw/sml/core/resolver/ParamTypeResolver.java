package org.hw.sml.core.resolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hw.sml.model.SMLParam;
import org.hw.sml.model.SMLParams;
import org.hw.sml.support.el.El;
import org.hw.sml.tools.Assert;
import org.hw.sml.tools.ClassUtil;
import org.hw.sml.tools.MapUtils;
import org.hw.sml.tools.RegexUtils;
/**
 * 
 * @author wen
 *
 */
public class ParamTypeResolver implements SqlResolver{

	public Rst resolve(String dialect, String temp,SMLParams sqlParamMaps) {
		List<String> mathers=null;
		//对数据进行
		if(temp.contains("<jdbcType")){
			mathers=RegexUtils.matchGroup("<jdbcType name=\"\\w+\" type=\"\\S+\"(/?)>",temp);
			for(String mather:mathers){
				String tmt=mather;
				if(!temp.contains(tmt)){
					continue;
				}
				
				if(tmt.endsWith("/>")){
					String name=RegexUtils.subString(tmt, "name=\"", "\" type=\"");
					String type=RegexUtils.subString(tmt,"type=\"","\"/>");
					SMLParam sp=sqlParamMaps.getSmlParam(name);
					if(sp!=null){
						sp.setType(type);
						sp.handlerValue(sp.getValue()==null?null:String.valueOf(sp.getValue()));
					}
					temp=temp.replace(tmt,"");
				}else{
					//取标签值
					String name=RegexUtils.subString(tmt, "name=\"", "\" type=\"");
					String type=RegexUtils.subString(tmt,"type=\"","\">");
					int start=temp.indexOf(tmt);
					int end=temp.indexOf("</jdbcType>",start);
					Assert.isTrue(end!=-1,mather+" must has end!");
					String tm=temp.substring(start,end+("</jdbcType>").length());
					String content=RegexUtils.subString(tm,">",("</jdbcType>"));
					SMLParam sp=sqlParamMaps.getSmlParam(name);
					if(sp!=null){
						sp.setType(type);
						sp.handlerValue(sp.getValue()==null?null:String.valueOf(sp.getValue()));
						Object value=sp.getValue();
						if(value!=null){
							if(value.getClass().isArray()){
								Object[] tos=(Object[])value;
								for(int i=0;i<tos.length;i++){
									tos[i]=JsEngine.evel(content.replace("@value",String.valueOf(tos[i])));
								}
							}else{
								sp.setValue(JsEngine.evel(content.replace("@value",String.valueOf(value))));
							}
						}
					}
					temp=temp.replace(tm,"");
				}
			}
		}
		if(temp.contains("<import")){
			mathers=RegexUtils.matchGroup("<import id=\"\\w+\" classpath=\"[\\w|.]+\"/>",temp);
			Map<String,Object> classpaths=MapUtils.newHashMap();
			for(String mather:mathers){
				String id=RegexUtils.subString(mather, "<import id=\"","\" classpath");
				String cp=RegexUtils.subString(mather, "classpath=\"","\"/>");
				Assert.isTrue(ClassUtil.hasClass(cp), "class "+cp+" not exists!");
				classpaths.put(id,ClassUtil.newInstance(cp));
				temp=temp.replace(mather,"");
			}
			mathers=RegexUtils.matchGroup("\\$\\{\\w+.\\w+\\([\\S|,]+\\)\\}", temp);
			for(String mather:mathers){
				String[] sss=RegexUtils.matchSubString("\\$\\{(.\\w+).(.*?)\\((.*?)\\)\\}",mather);
				Object bean=classpaths.get(sss[0]);
				Assert.notNull(bean,sss[0]+" is not import class!");
					Method m;
					try {
						m = bean.getClass().getMethod(sss[1],getClassPath(sss[2]));
						String result=(String) m.invoke(bean,sss[2].split(","));
						temp=temp.replace(mather,result);
					} catch (NoSuchMethodException e) {
						Assert.isTrue(false,sss[0]+"-"+sss[1]+" method not find!");
					} catch (InvocationTargetException e) {
						Assert.isTrue(false,"inner error "+e.getTargetException());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
			}
		}
		return new Rst(temp);
	}
	private Class[] getClassPath(String sss){
		Class[] c=new Class[sss.split(",").length];
		for(int i=0;i<c.length;i++){
			c[i]=String.class;
		}
		return c;
	}
	public void setEl(El el) {
	}
	public static void main(String[] args) {
		String temp="<import id=\"time\" classpath=\"org.hw.sml.Sml\"/>"
				+ "select * from table_${time.add(hour,1,yyyyMMdd-HH:mm:ss)}   ";
		String tt=	new ParamTypeResolver().resolve(null, temp, new SMLParams()).getSqlString();
		System.out.println(tt);
	}
}
