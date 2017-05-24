package com.eastcom_sw.inas.core.service.jdbc.build.lmaps;

import java.util.List;
import java.util.Map;

import com.eastcom_sw.inas.core.service.tools.MapUtils;

public class Group2FieldListDataBuilder extends AbstractDataBuilder{

	@Override
	public Object build(List<Map<String, Object>> datas) {
		String[] gps=rebuildParam.getGroupname().split(",");
		return MapUtils.groupMp2Lst(datas, gps[0], gps[1],rebuildParam.getOriFields(),rebuildParam.getNewFields());
	}

	
}
