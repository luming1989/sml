package org.hw.bean;

import org.hw.sml.support.ioc.annotation.Bean;

@Bean
public class Car implements ICar {

	public void run() {
		System.out.println("car run");
	}

	public void stop() {
		System.out.println("car stop");
	}

}
