package org.hw.sml.core.resolver;

import java.util.List;

import org.hw.sml.support.el.El;

import com.eastcom_sw.inas.core.service.jdbc.SqlParam;
import com.eastcom_sw.inas.core.service.jdbc.SqlParams;
import com.eastcom_sw.inas.core.service.tools.RegexUtils;
/**
 * 
 * @author wen
 *
 */
public class ParamTypeResolver implements SqlResolver{

	public Rst resolve(String dialect, String temp,SqlParams sqlParamMaps) {
		List<String> mathers=null;
		//对数据进行
		mathers=RegexUtils.matchGroup("<jdbcType name=\"\\w+\" type=\"\\S+\"/>",temp);
		for(String mather:mathers){
			String tmt=mather;
			if(!temp.contains(tmt)){
				continue;
			}
			String name=RegexUtils.subString(tmt, "name=\"", "\" type=\"");
			String type=RegexUtils.subString(tmt,"type=\"","\"/>");
			SqlParam sp=sqlParamMaps.getSqlParam(name);
			if(sp!=null){
				sp.setType(type);
				sp.handlerValue(sp.getValue()==null?null:String.valueOf(sp.getValue()));
			}
			temp=temp.replace(tmt,"");
		}
		return new Rst(temp);
	}

	@Override
	public void setEl(El el) {
		
	}

}
