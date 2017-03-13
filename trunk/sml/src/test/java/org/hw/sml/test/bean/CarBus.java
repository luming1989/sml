package org.hw.sml.test.bean;

import org.hw.sml.support.ioc.annotation.Bean;
import org.hw.sml.support.ioc.annotation.Init;
import org.hw.sml.support.ioc.annotation.Stop;

@Bean
public class CarBus implements ICar{
	
	@Init
	public void run() {
		System.out.println("carbus is run...");
	}
	@Stop
	public void stop() {
		System.out.println("carbus is stopd");
	}
	

}
