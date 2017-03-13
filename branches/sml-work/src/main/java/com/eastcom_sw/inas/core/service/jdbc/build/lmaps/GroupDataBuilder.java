package com.eastcom_sw.inas.core.service.jdbc.build.lmaps;

import java.util.List;
import java.util.Map;

import com.eastcom_sw.inas.core.service.tools.MapUtils;

public class GroupDataBuilder extends AbstractDataBuilder{

	public Object build(List<Map<String, Object>> datas) {
		datas=MapUtils.rebuildMp(datas,rebuildParam.getOriFields(),rebuildParam.getNewFields());
		return MapUtils.groupMpLst(datas, rebuildParam.getGroupname(), rebuildParam.getGroupFields());
	}
}
