package org.hw.sml.test.bean;

import org.hw.sml.support.ioc.annotation.Bean;
import org.hw.sml.support.ioc.annotation.Init;
import org.hw.sml.support.ioc.annotation.Inject;
import org.hw.sml.support.ioc.annotation.Stop;
import org.hw.sml.support.ioc.annotation.Val;

@Bean
public class Hw {
	@Inject("hwld")
	private Helloworld helloworld;
	
	@Inject("car")
	private ICar car;
	
	
	private ICar carBus;
	
	@Val("age")
	private int age;
	@Val("height")
	private double height;
	@Init
	private void init(){
		System.out.println(this.toString());
	}
	@Stop
	private void stop(){
		System.out.println(getClass()+"... stop");
	}

	public Helloworld getHelloworld() {
		return helloworld;
	}

	public void setHelloworld(Helloworld helloworld) {
		this.helloworld = helloworld;
	}
	public ICar getCar() {
		return car;
	}
	public void setCar(ICar car) {
		this.car = car;
	}
	public ICar getCarBus() {
		return carBus;
	}
	@Inject("carBus")
	public void setCarBus(ICar carBus) {
		this.carBus = carBus;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	@Override
	public String toString() {
		return "Hw [helloworld=" + helloworld + ", car=" + car + ", carBus="
				+ carBus + ", age=" + age + ", height=" + height + "]";
	}

	
	
}
