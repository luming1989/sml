package com.eastcom_sw.inas.core.service.jdbc.build.lmaps;

import java.util.List;
import java.util.Map;

import com.eastcom_sw.inas.core.service.tools.MapUtils;

public class FieldDataBuilder extends AbstractDataBuilder {
	public Object build(List<Map<String, Object>> datas) {
		String[] oriFields=rebuildParam.getOriFields();
		String[] newFields=rebuildParam.getNewFields();
		return MapUtils.rebuildMp(datas, oriFields,newFields);
	}
}