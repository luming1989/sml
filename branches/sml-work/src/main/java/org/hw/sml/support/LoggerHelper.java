package org.hw.sml.support;


public class LoggerHelper {
	public static  void debug(Class<?> c,String msg){
			org.slf4j.LoggerFactory.getLogger(c).debug(msg);
	}
	
	public static  void info(Class<?> c,String msg){
			org.slf4j.LoggerFactory.getLogger(c).info(msg);
	}
	
	public static  void warn(Class<?> c,String msg){
			org.slf4j.LoggerFactory.getLogger(c).warn(msg);
	}
	
	public static  void error(Class<?> c,String msg){
			org.slf4j.LoggerFactory.getLogger(c).error(msg);
	}
	
	/*
	public static  void debug(Class<?> c,String msg){
		java.util.logging.Logger.getLogger(c.getSimpleName()).info(msg);
	}
	public static  void info(Class<?> c,String msg){
		java.util.logging.Logger.getLogger(c.getSimpleName()).info(msg);
	}
	public static  void warn(Class<?> c,String msg){
		java.util.logging.Logger.getLogger(c.getSimpleName()).warning(msg);
	}
	public static  void error(Class<?> c,String msg){
		java.util.logging.Logger.getLogger(c.getSimpleName()).warning(msg);
	}*/
	
}
