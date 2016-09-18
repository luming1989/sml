package org.hw.sml.core.resolver;

import java.util.List;

import org.hw.sml.model.SMLParam;
import org.hw.sml.model.SMLParams;
import org.hw.sml.support.el.El;
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
		mathers=RegexUtils.matchGroup("<jdbcType name=\"\\w+\" type=\"\\S+\"/>",temp);
		for(String mather:mathers){
			String tmt=mather;
			if(!temp.contains(tmt)){
				continue;
			}
			String name=RegexUtils.subString(tmt, "name=\"", "\" type=\"");
			String type=RegexUtils.subString(tmt,"type=\"","\"/>");
			SMLParam sp=sqlParamMaps.getSmlParam(name);
			if(sp!=null){
				sp.setType(type);
				sp.handlerValue(sp.getValue()==null?null:String.valueOf(sp.getValue()));
			}
			temp=temp.replace(tmt,"");
		}
		return new Rst(temp);
	}

	public void setEl(El el) {
		
	}

}
