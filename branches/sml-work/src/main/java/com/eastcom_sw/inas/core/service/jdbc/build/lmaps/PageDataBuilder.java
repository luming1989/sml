package com.eastcom_sw.inas.core.service.jdbc.build.lmaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hw.sml.FrameworkConstant;

import com.eastcom_sw.inas.core.service.report.model.Result;
import com.eastcom_sw.inas.core.service.tools.MapUtils;
/**
 * 通过rebuildParam来包装数据结构
 * extMap k-v  可无限设定参数
 * 		limitMark   每页大小标识
 * 		pageMark    当前页值标识
 * @author wen
 *
 */
public class PageDataBuilder extends AbstractDataBuilder {
	public Object build(List<Map<String, Object>> datas) {
		String[] oriFields=rebuildParam.getOriFields();
		String[] newFields=rebuildParam.getNewFields();
		Result result=new Result();
		int page=Integer.parseInt(String.valueOf(sqlTemplate.getSqlParamMap().getSqlParam(MapUtils.getString(rebuildParam.getExtMap(),"pageMark","page")).getValue()));
		int limit=Integer.parseInt(String.valueOf(sqlTemplate.getSqlParamMap().getSqlParam(MapUtils.getString(rebuildParam.getExtMap(),"limitMark","limit")).getValue()));
		if(sqlTemplate.getSqlParamMap().getSqlParam(FrameworkConstant.PARAM_QUERYTYPE).getValue().equals("count")){
			Long count=Long.parseLong(String.valueOf(datas.get(0).get(datas.get(0).keySet().iterator().next())));
			result.setCount(count);
			if(count>0){
				sqlTemplate.getSqlParamMap().getSqlParam(FrameworkConstant.PARAM_QUERYTYPE).setValue("select");
				List<Map<String,Object>> data=jfContextUtils.getJdbcFTemplate().mergeSql(sqlTemplate);
				if(rebuildParam.getExtMap().get(FrameworkConstant.PARAM_TOLOWERCASEFORKEY)!=null&&rebuildParam.getExtMap().get(FrameworkConstant.PARAM_TOLOWERCASEFORKEY).equals("true"))
					data=MapUtils.toLowerCaseForKey(data);
				if(oriFields!=null&&newFields!=null){
					data=MapUtils.rebuildMp(data, oriFields,newFields);
				}
				result.setDatas(data);
			}else{
				result.setDatas(new ArrayList<Map<String,Object>>());
			}
		}else{
			if(oriFields!=null&&newFields!=null){
				datas=MapUtils.rebuildMp(datas, oriFields,newFields);
			}
			result.setDatas(datas);
			int size=datas.size();
			if(size<limit&&page==1){
				result.setCount(Long.valueOf(size));
			}else{
				sqlTemplate.getSqlParamMap().getSqlParam(FrameworkConstant.PARAM_QUERYTYPE).setValue("count");
				List<Map<String,Object>> data=jfContextUtils.getJdbcFTemplate().mergeSql(sqlTemplate);
				Long count=Long.parseLong(String.valueOf(data.get(0).get(data.get(0).keySet().iterator().next())));
				result.setCount(count);
			}
		}
		return result;
	}
}