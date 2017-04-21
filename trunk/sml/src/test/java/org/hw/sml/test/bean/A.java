package org.hw.sml.test.bean;

import java.util.Arrays;

import org.hw.sml.support.ioc.BeanHelper;

import com.alibaba.fastjson.JSON;

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
	public String get(String a,String b,String c){
		return a+b+c;
	}
	public String get(String[] strs){
		return Arrays.asList(strs).toString();
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
		System.out.println(new Object().getClass().isAssignableFrom(String.class));
		BeanHelper.start();
		System.out.println(Arrays.asList(A.class.getConstructors()[1].getParameterTypes()));
		System.out.println(BeanHelper.getBean(A.class));
		System.out.println(BeanHelper.evelV("12.0d"));
		System.out.println(BeanHelper.evelV("#{hi.length}"));
		System.out.println(BeanHelper.evelV("#{mk.get(2i)}"));
		System.out.println(BeanHelper.evelV("#{aBean.get(\"1\",\"2\",\"3\")}"));
		System.out.println(JSON.toJSONString(BeanHelper.evelV("#{hi[1]}")));
		System.out.println(BeanHelper.evelV("#{aBean.get(#{mark})}"));
		BeanHelper.evelV("#{sml.cacheManager.set(\"hlw\",123,1i)}");
		//System.out.println(sml.getCacheManager().getKeyStart(""));
		System.out.println(BeanHelper.loopElp("sml.cacheManager.getKeyStart(\"\")"));
		//System.out.println(BeanHelper.evelV("#{sml.smlContextUtils.query(\"area-pm\",\"\")}"));
		System.out.println(BeanHelper.evelV("#{sml.cacheManager.clear()}"));
		System.out.println(BeanHelper.evelV("${username}"));
		System.out.println(BeanHelper.loopElp("sml.cacheManager.getKeyStart(\"\")"));
		System.out.println(BeanHelper.loopElp("sml.getJdbc(\"defJt\").dataSource.toString().equals(#{sml})"));
		//ArrayList c;
		//"".equals(anObject)
	}


}
