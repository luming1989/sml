package org.hw.sml.core.build.lmaps;

import java.util.List;
import java.util.Map;

import org.hw.sml.tools.MapUtils;

public class Group2FieldListDataBuilder extends AbstractDataBuilder{
	public Object build(List<Map<String, Object>> datas) {
		String[] gps=rebuildParam.getGroupname().split(",");
		return MapUtils.groupMp2Lst(datas, gps[0], gps[1], rebuildParam.getOriFields(),rebuildParam.getNewFields());
	}

	
}
