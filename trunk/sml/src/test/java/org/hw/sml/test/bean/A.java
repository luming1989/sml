package org.hw.sml.test.bean;

import java.util.Arrays;

import org.hw.sml.support.ioc.BeanHelper;

public class A {
	private String str;
	private  Integer i;
	private Character c;
	private Double d;
	private Long l;
	private Short s;
	private Float f;
	private Boolean flag;
	public A(int i){
		this.i=i;
	}
	public A(String str, Integer i, Character c, Double d, Long l, Short s, Float f, Boolean flag) {
		super();
		this.str = str;
		this.i = i;
		this.c = c;
		this.d = d;
		this.l = l;
		this.s = s;
		this.f = f;
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "A [str=" + str + ", i=" + i + ", c=" + c + ", d=" + d + ", l=" + l + ", s=" + s + ", f=" + f + ", flag="
				+ flag + "]";
	}
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		BeanHelper.start();
		System.out.println(Arrays.asList(A.class.getConstructors()[1].getParameterTypes()));
		System.out.println(BeanHelper.getBean(A.class));
		System.out.println(BeanHelper.evelV("12.0d"));
	}


}
