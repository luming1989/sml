package org.hw.sml.test.bean;


public class Person {
	private String name;
	private Classes id;
	private int age;
	
	
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
	@Override
	public String toString() {
		return "Person [name=" + name + ", id=" + id + ", age=" + age + "]";
	}
	
}
