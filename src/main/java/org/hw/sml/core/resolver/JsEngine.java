package org.hw.sml.core.resolver;

import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsEngine {
	public static Logger logger=Logger.getLogger("");
	private static ScriptEngine engine;
	static{
		 ScriptEngineManager manager = new ScriptEngineManager();
		 engine = manager.getEngineByName("javascript");
		 try {
			engine.eval(new InputStreamReader(JsEngine.class.getResourceAsStream("js_time_format.js")));
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	public static boolean evelBoolean(String msg) throws ScriptException{
			return Boolean.valueOf(String.valueOf(engine.eval(msg)));
	}
	public static Object  evelNil(String msg){
		try {
			return engine.eval(msg);
		} catch (Throwable e) {
			//logger.error("msg[{}]",msg);
		}
		return "";
	}
	public static Object  evel(String msg){
		try {
			return engine.eval(msg);
		} catch (ScriptException e) {
			e.printStackTrace();
			logger.info("msg["+msg+"]");
		}
		return null;
	}
	
}
