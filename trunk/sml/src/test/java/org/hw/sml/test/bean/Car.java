package org.hw.sml.test.bean;

import org.hw.sml.support.ioc.annotation.Bean;
import org.hw.sml.support.ioc.annotation.Init;
import org.hw.sml.support.ioc.annotation.Stop;

@Bean
public class Car implements ICar{
	@Init(isDelay=true,sleep=10)
	public void run() {
		System.out.println("car is run...");
	}
	@Stop
	public void stop() {
		System.out.println("car is stopd");
	}
}
