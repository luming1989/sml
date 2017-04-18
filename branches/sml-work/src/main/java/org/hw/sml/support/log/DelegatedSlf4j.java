package org.hw.sml.support.log;

import org.slf4j.LoggerFactory;

public class DelegatedSlf4j implements Loggers{
	public void debug(Class<?> clazz, String msg) {
		LoggerFactory.getLogger(clazz).debug(msg);
	}
	public void info(Class<?> clazz, String msg) {
		LoggerFactory.getLogger(clazz).info(msg);
	}
	public void warn(Class<?> clazz, String msg) {
		LoggerFactory.getLogger(clazz).warn(msg);
	}
	public void error(Class<?> clazz, String msg) {
		LoggerFactory.getLogger(clazz).error(msg);
	}
}
