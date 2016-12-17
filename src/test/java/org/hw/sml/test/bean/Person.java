package org.hw.sml.test.bean;

import java.util.Arrays;
import java.util.List;


public class Person {
	private String name;
	private Classes id;
	private int age;
	
	private Double[] hi;
	
	private String[] mark;
	
	private List<String> mk;
	
	private ICar car;
	public void init(){
		System.out.println(this);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Classes getId() {
		return id;
	}
	public void setId(Classes id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public Double[] getHi() {
		return hi;
	}

	public void setHi(Double[] hi) {
		this.hi = hi;
	}

	public ICar getCar() {
		return car;
	}

	public void setCar(ICar car) {
		this.car = car;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", id=" + id + ", age=" + age + ", hi="
				+ Arrays.toString(hi) + ", mark=" + Arrays.toString(mark)
				+ ", mk=" + mk + ", car=" + car + "]";
	}


	
	
}
