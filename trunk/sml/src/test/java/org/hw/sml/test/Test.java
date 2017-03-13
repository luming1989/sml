package org.hw.sml.test;

import java.io.IOException;

import org.hw.sml.context.SmlContextUtils;

public class Test {
	public static void main(String[] args) throws IOException {
		//System.setProperty("sun.jnu.encoding","utf-8");
		System.setProperty("LANG","utf-8");
		System.out.println(System.getProperties());
		String result=SmlContextUtils.queryFromUrl("http://10.221.247.7:19080/ipmsDS/ipms?accessToken=YWRtaW5obHdhc2QkMTIz&IfId=IF-3RD-SMS-XN-SHARE-001",
				"{\"msisdns\":\"1572141320111！7\",\"content\":\"测试123\",\"fnId\":\"1001\"}");
		String t="！";
		System.out.println(result);
	}
}
