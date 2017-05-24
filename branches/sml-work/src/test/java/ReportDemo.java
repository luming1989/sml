import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hw.sml.jdbc.impl.DefaultDataSource;

import com.eastcom_sw.inas.core.service.report.ReportCommonService;
import com.eastcom_sw.inas.core.service.report.model.Queryer;
import com.eastcom_sw.inas.core.service.report.model.Result;



public class ReportDemo {
	public static void main(String[] args) {
		DefaultDataSource dataSource = new DefaultDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@10.221.247.46:1521/ipms");
		dataSource.setUsername("ipmsdm");
		dataSource.setPassword("SHipmsdm!23$");
		//库集
		Map<String,DataSource> dss=new HashMap<String,DataSource>();
		dss.put("defJt", dataSource);
		//对象
		ReportCommonService rcptCommonService=new ReportCommonService();
		rcptCommonService.setDss(dss);//
		rcptCommonService.init();
		//使用核心方法
		//CallableHelper.retriedTimes=1;
		for(int i=0;i<2;i++){
		Result data=rcptCommonService.getResult(
				new Queryer("10103")
				/*.addQuery("TIME_ID", ">=","201601010000")
				.addQuery("TIME_ID", "<","201609010000")
				.addQuery("LTE_CELL_NAME", "ilike","h")
				.addQuery("BTS_NAME", "like", "路")*/
				.limit(1, 10)
				.addOrder("case when DISTRICT='浦东' then 1 when DISTRICT='南区' then 2 when DISTRICT='西区' then 3 when DISTRICT='北区' then 4 when DISTRICT='闵行' then 5 when DISTRICT='松江' then 6 when DISTRICT='宝山' then 7 when DISTRICT='嘉定' then 8 when DISTRICT='青浦' then 9 when DISTRICT='奉贤' then 10 when DISTRICT='金山' then 11 when DISTRICT='崇明' then 12 end", "DESC")
				);
		System.out.println(data.toString());
		}
		//配置缓存清空
		//rcptCommonService.destroy();
		//
	}
}
