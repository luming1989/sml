import java.util.List;
import java.util.concurrent.Callable;

import com.eastcom_sw.inas.core.service.support.CallableHelper;
import com.eastcom_sw.inas.core.service.tools.MapUtils;


public class CallableTest {
	public static void main(String[] args) {
		List<Callable<String>> e=MapUtils.newArrayList();
		for(int i=0;i<15;i++){
			final int temp=i;
			Callable<String> callable=new Callable<String>() {
				public String call() throws Exception {
					Thread.sleep(900*temp);
					System.out.println(temp);
					return temp+"";
				}
			};
			e.add(callable);
		}
		
		CallableHelper.callresults(1,5,e.toArray(new Callable[e.size()]));
	}
}
