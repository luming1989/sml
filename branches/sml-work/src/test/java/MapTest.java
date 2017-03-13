import java.util.List;
import java.util.Map;


import com.eastcom_sw.inas.core.service.tools.DateTools;
import com.eastcom_sw.inas.core.service.tools.MapUtils;
import com.eastcom_sw.inas.core.service.tools.Maps;


public class MapTest {
	public static List<Map<String,Object>> family=MapUtils.newArrayList();
	public static List<Map<String,Object>> student=MapUtils.newArrayList();
	public static List<Map<String,Object>> classes=MapUtils.newArrayList();
	static{
		family.add(new Maps<String,Object>().put("family","黄家").put("name","黄书林").put("age","19650909").put("rank",1).put("a","").getMap());
		family.add(new Maps<String,Object>().put("family","黄家").put("name","武百秀").put("age","19650827").put("rank",2).getMap());
		family.add(new Maps<String,Object>().put("family","黄家").put("name","黄武").put("age","19871021").put("rank",3).getMap());
		family.add(new Maps<String,Object>().put("family","黄家").put("name","黄文").put("age","19891205").put("rank",4).getMap());
		family.add(new Maps<String,Object>().put("family","张家").put("name","张丽芳").put("age","19890913").put("rank",5).getMap());
		family.add(new Maps<String,Object>().put("family","黄家").put("name","黄子耀").put("age","20081212").put("rank",6).getMap());
		family.add(new Maps<String,Object>().put("family","黄家").put("name","黄子轩").put("age","20100801").put("rank",7).getMap());
		family.add(new Maps<String,Object>().put("family","黄家").put("name","黄紫嫣").put("age","20120303").put("rank",8).getMap());
		family.add(new Maps<String,Object>().put("family","黄家").put("name","黄丽文").put("age","20150219").put("rank",9).getMap());
		family.add(new Maps<String,Object>().put("family","邓家").put("name","邓雪琴").put("age","19910913").put("rank",10).getMap());
		
		classes.add(new Maps<String,Object>().put("classId","1001").put("level",1).put("className","(1)班").getMap());
		classes.add(new Maps<String,Object>().put("classId","1002").put("level",1).put("className","(2)班").getMap());
		classes.add(new Maps<String,Object>().put("classId","1003").put("level",1).put("className","(3)班").getMap());
		classes.add(new Maps<String,Object>().put("classId","2001").put("level",2).put("className","(1)班").getMap());
		classes.add(new Maps<String,Object>().put("classId","2002").put("level",2).put("className","(2)班").getMap());
		classes.add(new Maps<String,Object>().put("classId","2003").put("level",2).put("className","(3)班").getMap());
		
		student.add(new Maps<String,Object>().put("stuId","1001").put("name","小明").put("classId","1001").getMap());
		student.add(new Maps<String,Object>().put("stuId","1002").put("name","小红").put("classId","2001").getMap());
		
	}
	
	public static void main(String[] args) {
		for(int i=0;i<3;i++){
		long start=System.currentTimeMillis();
		String str="--a=3 \t\n\n\r\n --b=2=8=0=9=7 --t=4";
		System.out.println("map format test:---->"+MapUtils.transMapFromStr(str));
		System.out.println("map aggregateLstMp:---->"+MapUtils.aggregateLstMp(family,"--age=min --family$familyconcat=wm_concat --age$ageMin=max --age$ageSum=count  --size=max"));
		System.out.println("map grep:---->"+MapUtils.grep(family,"--this.type=or --name=notin 黄书林,黄武 --age=nlike 12 --size=in 9,10"));
		System.out.println("map compareMergeMp:---->"+MapUtils.compareMergeMp(family,family,new String[]{"family","name"},"--rank$sizeHb=thbu --rank$ageAvg=sum  --rank$sizeMin=min --rank$sizeMax=max --rank$sizeNew=rank"));
		System.out.println("map rebuildMp:---->"+MapUtils.rebuildMp(family));
		System.out.println("map rebuildMpDist:---->"+MapUtils.rebuildMpDist(family, new String[]{"rank"}));
		
		System.out.println("map comparemergeMpCartesianProduct:---->"+MapUtils.comparemergeMpCartesianProduct(classes, student,"--classId=classId"));
		System.out.println("map compareMergeMpIntersection:---->"+MapUtils.compareMergeMpIntersection(classes, student,new String[]{"classId"}));
		System.out.println("map compareMergeComplementary:---->"+MapUtils.compareMergeMpComplementary(classes, student,new String[]{"classId"}));
		List<Map<String,String>> datas=MapUtils.buildTimeLst("20160507","20160508","yyyy-MM-dd HH:mm:ss",5,"time");
		MapUtils.sort(datas, "time","desc");
		System.out.println("map sort:--->"+datas);
		MapUtils.rebuildMp(datas,new MapUtils.Builder<Map<String,String>>(){
			public void build(Map<String,String> data) {
				data.put("date",DateTools.parse(data.get("time"))+"");
			}
		});
		System.out.println(datas);
		System.out.println("结束调试："+(System.currentTimeMillis()-start));
		}
	}
	public static void main1(String[] args) {
		List<String> datas=(List)MapUtils.grepVsByK(family,"age",true);
		System.out.println(datas);
	}
}
