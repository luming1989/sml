package com.eastcom_sw.inas.core.service.jdbc.build.lmaps;

import java.util.List;
import java.util.Map;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.support.el.Links;
import org.hw.sml.tools.MapUtils;

import com.eastcom_sw.inas.core.service.jdbc.RebuildParam;
import com.eastcom_sw.inas.core.service.jdbc.SqlParams;
import com.eastcom_sw.inas.core.service.jdbc.build.DataBuilderHelper;

public class OpLinkDataBuilder extends AbstractDataBuilder{
	public Object build(List<Map<String, Object>> datas) {
		SqlParams params=sqlTemplate.getSqlParamMap();
		Links links=new Links(params.getSqlParam(FrameworkConstant.PARAM_OPLINKS).getValue().toString()).parseLinks();
		String opLinksV=params.getSqlParam(FrameworkConstant.PARAM_OPLINKS+"V").getValue().toString();
		Map<String,String> firstBps=links.getOpLinksInfo().get(opLinksV);
		Object firstResult=DataBuilderHelper.build(create(firstBps),datas);
		if(links.getOpLinks().length==0){
			return firstResult;
		}
		Map<String,Object> result=MapUtils.newLinkedHashMap();
		result.put(opLinksV, firstResult);
		for(int i=1;i<links.getOpLinks().length;i++){
			String linksName=links.getOpLinks()[i];
			params.getSqlParam(FrameworkConstant.PARAM_OPLINKS+"V").setValue(linksName);
			result.put(linksName,DataBuilderHelper.build(create(links.getOpLinksInfo().get(linksName)),jfContextUtils.getJdbcFTemplate().mergeSql(sqlTemplate)));
		}
		return result;
	}
	
	public RebuildParam create(Map<String,String> data){
		RebuildParam rp=new RebuildParam();
		rp.setClasspath(MapUtils.getString(data,"classpath"));
		rp.setIndex(MapUtils.getString(data,"index","0"));
		rp.setGroupname(MapUtils.getString(data,"groupname"));
		rp.setFilepath(MapUtils.getString(data,"filepath"));
		rp.setOriFields(MapUtils.getString(data,"oriFields")==null?null:MapUtils.getString(data,"oriFields").split("~"));
		rp.setNewFields(MapUtils.getString(data,"newFields")==null?null:MapUtils.getString(data,"newFields").split("~"));
		rp.setGroupFields(MapUtils.getString(data,"groupFields")==null?null:MapUtils.getString(data,"groupFields").split("~"));
		rp.setType(MapUtils.getInt(data,"type",0));
		rp.setExtMap(data);
		return rp;
	}
}
