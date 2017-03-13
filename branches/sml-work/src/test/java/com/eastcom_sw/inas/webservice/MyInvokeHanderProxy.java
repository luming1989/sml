package com.eastcom_sw.inas.webservice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface A{
	String say(String a);
}
class AI implements A{
	public String say(String a) {
		return a;
	}
	
}
public class MyInvokeHanderProxy implements InvocationHandler {
    public Object bind(Object delegate){    
        return Proxy.newProxyInstance(delegate.getClass().getClassLoader(),     
                delegate.getClass().getInterfaces(), this);    
    }    
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return method.invoke(proxy, args);
	}
	public static void main(String[] args) {
		MyInvokeHanderProxy proxy=new MyInvokeHanderProxy();
		AI ai=(AI) proxy.bind(new AI());

		System.out.println(ai.say("aaa"));
	}
}
