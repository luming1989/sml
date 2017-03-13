import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.eastcom_sw.inas.core.service.report.model.Update;


public class UpdateTest {
	public static void main(String[] args) {
		Update update=new Update();
		//update.setConditions(Arrays.asList("old.id"));
		update.setTableName("id");
		//update.setType("adu");
		Map<String,Object> data=new LinkedHashMap<String, Object>();
		data.put("id",100);
		data.put("old.id",1000);
		data.put("name", "hw");
		data.put("time_stamp@date","20151212" );
		update.setData(data);
		update.setConditions(Arrays.asList("old.id"));
		update.init();
		update.setType("update");
		System.out.println(update.getUpateSql()+"--->"+Arrays.asList(update.getObjects().get(0)));
		//System.out.println(update.isExistSql());
		/*System.out.println(update.getUpdateSqlForAdu(true)+"----->"+Arrays.asList(update.getObjectForAdu(true)));
		System.out.println(update.getUpdateSqlForAdu(false)+"----->"+Arrays.asList(update.getObjectForAdu(false)));
		update.setType("update");
		System.out.println(update.getUpateSql()+"--->"+Arrays.asList(update.getObjects().get(0)));
		update.setType("delete");
		System.out.println(update.getUpateSql()+"--->"+Arrays.asList(update.getObjects().get(0)));*/
		//System.out.println(Arrays.asList(update.getObjects().get(0)));
		
	}
}
