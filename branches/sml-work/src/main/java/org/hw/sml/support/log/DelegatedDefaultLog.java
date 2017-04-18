package org.hw.sml.support.log;

import java.util.logging.Logger;

public class DelegatedDefaultLog implements Loggers{

	public void debug(Class<?> clazz, String msg) {
		Logger.getLogger(clazz.getSimpleName()).info(msg);
	}

	public void info(Class<?> clazz, String msg) {
		Logger.getLogger(clazz.getSimpleName()).info(msg);
	}

	public void warn(Class<?> clazz, String msg) {
		Logger.getLogger(clazz.getSimpleName()).warning(msg);
	}

	public void error(Class<?> clazz, String msg) {
		Logger.getLogger(clazz.getSimpleName()).warning(msg);
	}

}
