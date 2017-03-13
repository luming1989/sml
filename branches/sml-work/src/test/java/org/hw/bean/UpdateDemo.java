package org.hw.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hw.sml.tools.MapUtils;

import com.alibaba.fastjson.JSON;
import com.eastcom_sw.inas.core.service.report.model.Constants;
import com.eastcom_sw.inas.core.service.report.model.Update;

public class UpdateDemo {
	public static void main(String[] args) {
		//1、新增测试
		Update updater=new Update();
		Map<String,Object> data=new HashMap<String, Object>(){
			{
				put("id","1001");
				put("name2","柏君雄");
				put("age3","25");
			}
		};
		//
		//data=MapUtils.rebuildMp(data, new String[]{"id","name2","age3"}, new String[]{"id","name","age"});
		data=MapUtils.rebuildMp(data,"--id=id --name2=name --age3=age");
		updater.setDbId("wlan");
		updater.setTableName("rm_wlan_hotspot");
		updater.setData(data);
		
		//生成insert 语句
		updater.setType(Constants.TYPE_INSERT);
		System.out.println(updater.getUpateSql()+"------>"+JSON.toJSONString((updater.getObjects())));
		//生成update 语句
		updater.setType(Constants.TYPE_UPDATE);
		updater.setConditions(Arrays.asList("id"));
		System.out.println(updater.getUpateSql()+"------>"+JSON.toJSONString((updater.getObjects())));
		//delete 语句
		updater.setType(Constants.TYPE_DELETE);
		System.out.println(updater.getUpateSql()+"------>"+JSON.toJSONString((updater.getObjects())));
	}
	public int update(String sql,Object[] objs){
		return 1;
	}
}
