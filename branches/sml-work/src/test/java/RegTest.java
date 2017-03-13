import java.util.Arrays;

import com.eastcom_sw.inas.core.service.tools.RegexUtils;


public class RegTest {
	public static void main(String[] args) {
		String phone="15721413208";
		String[] at=RegexUtils.matchSubString("(\\d{3})(\\d{4})(\\d{4})",phone);
		System.out.println(Arrays.asList(at));
		System.out.println(phone.replaceAll("(\\d{3})(\\d{4})(\\d{4})","$1****$2"));
	}
}
