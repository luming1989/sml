import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hw.sml.jdbc.impl.DefaultDataSource;
import org.hw.sml.support.SmlAppContextUtils;

import com.alibaba.fastjson.JSON;
import com.eastcom_sw.inas.core.service.jdbc.JdbcFTemplate;
import com.eastcom_sw.inas.core.service.jdbc.JsonMapper;



public class JfDemo {
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
		JdbcFTemplate jf=new JdbcFTemplate();
		jf.setDss(dss);//
		jf.setJsonMapper(new JsonMapper() {
			public <T> T toObj(String json, Class<T> t) {
				return JSON.parseObject(json,t);
			}
		});
		jf.init();
		System.out.println("init success！");
		//使用核心方法-迪士尼客流量查询
			/*for(int i=0;i<10;i++){
			Object data=jf.getJfContextUtils().update("test-update",
					new Maps<String,String>().put("sp_ip","1.1.1."+i).put("sp_name","www.baidu.com"+i).getMap());
			System.out.println((System.currentTimeMillis()-begin)+"|----->"+data);
			}*/
		for(int i=0;i<3;i++){
		Object obj=SmlAppContextUtils.getSmlContextUtils().query("videoEte-homepage-cell-detail","{\"timeType\":\"day\",\"area_id\":\"21\",\"time_id\":\"201704180000\"}");
		System.out.println(JSON.toJSONString(obj));
		}
		//System.out.println(jf.getDefJt().queryForObject("select systimestamp from dual", Date.class));
		//jf.getDefJt().execute("create table hw_temp2(id int,name varchar2(128))");
		//int i=jf.getDefJt().update(Arrays.asList("insert into hw_temp(sp_ip,sp_name,f) values(?,?,?)","insert into hw_temp values(?,?)"),
		//		Arrays.asList(new Object[]{"eee4",null,null},new Object[]{"eee5","e"},null));
		//System.out.println(i);
	/*	int i=SmlAppContextUtils.getSmlContextUtils().update("test-update-links", new HashMap<String,String>(){
			{
				put("sp_ip","女儿2");
				put("sp_name","黄丽文");
				put("sp_name_update","黄楚伊");
			}
		});
		System.out.println(i);*/
		
		/*Object data=SmlAppContextUtils.getSmlContextUtils().query("test-query-links", new HashMap<String,String>(){{
		put("isRemoteParams","true");
		put("opLinksV","delete");
		put("opLinks","delete${type=4}-insert${type=1|oriFields=SP_IP~SP_NAME|newFields=a~b}-update${classpath=GroupDataBuilder|groupname=SP_IP}");
		}});*/
		//System.out.println(data);
		/*int i=SmlAppContextUtils.getSmlContextUtils().update("zy-ftp-update",new HashMap<String, String>(){{
			//put("isRemoteParams","true");
			put("creator", "creator");
			put("ftp_user", "ftp_user1");
			put("ftp_password", "ftp_password1");
		}});*/
		//System.out.println(i);
		
		
	}
}
