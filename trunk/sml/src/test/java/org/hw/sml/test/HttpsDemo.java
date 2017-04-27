package org.hw.sml.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hw.sml.tools.Https;

public class HttpsDemo {
	public static void main(String[] args) throws IOException {
		//下载
		//String result=Https.newGetHttps("http://10.221.235.17:8080/INAS/sml/invoke/mdmMngService/template/System").bos(new FileOutputStream("d:/temp/t.xlsx")).execute();
		//上传
		Https https=Https.newPostHttps("http://localhost:10086/esb/helloworld/import").upFile().body(Https.newUpFile("t.xlsx",new FileInputStream("D:/temp/t.xlsx")));
		https.getParamer().add("a","黄文");
		https.getParamer().add("b","黄武");
		String result=https.execute();
		//
		System.out.println(result);
	}
}
