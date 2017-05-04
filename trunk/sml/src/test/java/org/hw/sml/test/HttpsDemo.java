package org.hw.sml.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hw.sml.tools.Https;

public class HttpsDemo {
	public static void main(String[] args) throws IOException {
		//zzzz
		//String result=Https.newGetHttps("http://10.221.235.17:8080/INAS/sml/invoke/mdmMngService/template/System").bos(new FileOutputStream("d:/temp/t.xlsx")).execute();
		//上传
		String result=Https.newPostBodyHttps("http://10.221.247.7:1202/master/server/proxy/sms/send").charset("utf-8").body("{\"content\":\"test测试\",\"fromNumber\":\"18256075451\"}").execute();
		System.out.println(result);
	}
}
