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
		for(int i=0;i<1;i++){
			String result=Https.newGetHttps("http://www.baidu.com").keepAlive(true).execute();
			//
			System.out.println(new Integer(result.length()).toString().concat(""));
		}
	}
}
