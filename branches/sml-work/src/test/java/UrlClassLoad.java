import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eastcom_sw.inas.core.service.jdbc.build.lmaps.AbstractDataBuilder;
import com.eastcom_sw.inas.core.service.tools.Maps;


public class UrlClassLoad {
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
		for(int i=0;i<10;i++){
		//F:\workspace\workspace_inas_provider\INAS_Provider\inas_core\target\inas_core-1.0.0-SNAPSHOT.jar
		URLClassLoader cl=new URLClassLoader(new URL[]{new URL("file:F:/workspace/workspace_inas_provider/INAS_Provider/inas_core/target/inas_core-1.0.0-SNAPSHOT.jar")});
		AbstractDataBuilder adb=(AbstractDataBuilder) cl.loadClass("com.eastcom_sw.inas.core.util.DB2").newInstance();
		System.out.println(adb.build(new ArrayList<Map<String,Object>>(){{
			add(new Maps<String,Object>().put("a","a").put("b","b").getMap());
			add(new Maps<String,Object>().put("a2","a2").put("b2","b2").getMap());
		}}));
		Thread.sleep(30000);
		}
		
	}
}
