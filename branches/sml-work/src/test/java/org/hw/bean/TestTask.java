package org.hw.bean;

import org.hw.sml.support.ioc.annotation.Bean;
import org.hw.sml.support.ioc.annotation.Init;
import org.hw.sml.support.ioc.annotation.Inject;
import org.hw.sml.support.ioc.annotation.Stop;
import org.hw.sml.support.ioc.annotation.Val;

@Bean
public class TestTask {
	@Inject("bus")
	public Bus busss;
	@Val("times")
	private int i;
	@Init
	public void init(){
		Thread thread=new Thread(new Runnable(){

			public void run() {
				while(true){
					i--;
					if(i<0){
						return;
					}
					System.out.println(i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		thread.start();
	}
	@Stop
	public void stop(){
		System.out.println(this.getClass()+"   stop");
	}
}
