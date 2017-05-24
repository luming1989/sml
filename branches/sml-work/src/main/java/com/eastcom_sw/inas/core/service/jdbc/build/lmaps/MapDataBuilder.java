package com.eastcom_sw.inas.core.service.jdbc.build.lmaps;

import java.util.List;
import java.util.Map;

import com.eastcom_sw.inas.core.service.tools.MapUtils;

public class MapDataBuilder extends AbstractDataBuilder{
	public Object build(List<Map<String, Object>> datas) {
		datas=MapUtils.rebuildMp(datas,rebuildParam.getOriFields(),rebuildParam.getNewFields());
		Map<Object,Object> result=MapUtils.newLinkedHashMap();
		for(Map<String,Object> data:datas){
			result.put(data.get(rebuildParam.getExtMap().get("key")==null?"key":rebuildParam.getExtMap().get("key")),data.get(rebuildParam.getExtMap().get("value")==null?"value":rebuildParam.getExtMap().get("value")));
		}
		return result;
	}
}
