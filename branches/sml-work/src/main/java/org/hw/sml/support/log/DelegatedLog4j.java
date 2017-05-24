package org.hw.sml.support.log;

import org.apache.log4j.Logger;

public class DelegatedLog4j implements Loggers{

	public void debug(Class<?> clazz, String msg) {
		Logger.getLogger(clazz).debug(msg);
	}

	public void info(Class<?> clazz, String msg) {
		Logger.getLogger(clazz).info(msg);
	}

	public void warn(Class<?> clazz, String msg) {
		Logger.getLogger(clazz).warn(msg);
	}

	public void error(Class<?> clazz, String msg) {
		Logger.getLogger(clazz).error(msg);
	}
}
