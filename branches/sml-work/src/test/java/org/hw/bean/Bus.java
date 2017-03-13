package org.hw.bean;

import org.hw.sml.support.ioc.annotation.Bean;
import org.hw.sml.support.ioc.annotation.Init;
import org.hw.sml.support.ioc.annotation.Stop;
import org.hw.sml.support.ioc.annotation.Val;

@Bean
public class Bus implements ICar {
	@Val("fload-test")
	private float f;

	@Init(isDelay=true,sleep=15,igErr=true)
	public void run() {
		System.out.println("bus run");
	}
	@Init
	@Stop
	public void stop() {
		System.out.println("bus stop");
	}
	@Init
	public void init(){
		System.out.println(f);
	}
	
}
