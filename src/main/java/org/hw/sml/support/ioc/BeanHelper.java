package org.hw.sml.support.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.support.ClassHelper;
import org.hw.sml.support.ioc.annotation.Bean;
import org.hw.sml.support.ioc.annotation.Val;
import org.hw.sml.support.ioc.annotation.Init;
import org.hw.sml.support.ioc.annotation.Inject;
import org.hw.sml.tools.Assert;
import org.hw.sml.tools.MapUtils;

public class BeanHelper {
	private static  Map<String,Object> beanMap=MapUtils.newHashMap();
	static{
		try {
			String packageName=FrameworkConstant.properties.getProperty("ioc-class-scan");
			List<Class<?>> classes=MapUtils.newArrayList();
			for(String pn:packageName.split(",| ")){
				List<Class<?>> cls=ClassHelper.getClassListByAnnotation(pn, Bean.class);
				classes.addAll(cls);
			}
			if(packageName!=null&&packageName.trim().length()>0){
				
				//查找所有Bean注解并生成对象
				for(Class<?> clazz:classes){
					Bean bean=clazz.getAnnotation(Bean.class);
					String beanName=bean.value();
					if(beanName==null||beanName.trim().length()==0){
						beanName=toLowerForStart(clazz.getSimpleName());
					}
					Object beanVal=clazz.newInstance();
					beanMap.put(beanName,beanVal);
				}
				
				//查询所有字段inject进行赋值
				for(Class<?> clazz:classes){
					Bean bean=clazz.getAnnotation(Bean.class);
					String beanName=bean.value();
					if(beanName==null||beanName.trim().length()==0){
						beanName=toLowerForStart(clazz.getSimpleName());
					}
					//
					Field[] fields=clazz.getDeclaredFields();
					for(Field filed:fields){
						Inject inject=filed.getAnnotation(Inject.class);
						if(inject==null){
							continue;
						}
						String injectName=inject.value();
						if(injectName==null||injectName.trim().length()==0){
							injectName=toLowerForStart(filed.getType().getSimpleName());
						}
						filed.setAccessible(true);
						Object v= beanMap.get(injectName)==null?beanMap.get(filed.getName()):beanMap.get(injectName);
						Assert.notNull(v, "beanName:"+beanName+",field inject"+filed.getName()+" v is null");
						filed.set(beanMap.get(beanName),v);
					}
				}
				//@Config进行赋值
				for(Class<?> clazz:classes){
					Bean bean=clazz.getAnnotation(Bean.class);
					String beanName=bean.value();
					if(beanName==null||beanName.trim().length()==0){
						beanName=toLowerForStart(clazz.getSimpleName());
					}
					//
					Field[] fields=clazz.getDeclaredFields();
					for(Field filed:fields){
						Val config=filed.getAnnotation(Val.class);
						if(config==null){
							continue;
						}
						String configName=config.value();
						Assert.notNull(configName, "beanName:"+beanName+",field config"+filed.getName()+" is null");
						filed.setAccessible(true);
						filed.set(beanMap.get(beanName),FrameworkConstant.properties.get(configName));
					}
				}
				//init方法
				for(Class<?> clazz:classes){
					Bean bean=clazz.getAnnotation(Bean.class);
					String beanName=bean.value();
					if(beanName==null||beanName.trim().length()==0){
						beanName=toLowerForStart(clazz.getSimpleName());
					}
					for(Method method:clazz.getDeclaredMethods()){
						Init init=method.getAnnotation(Init.class);
						if(init!=null){
							method.setAccessible(true);
							method.invoke(beanMap.get(beanName), null);
						}
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public static int start(){
		return 1;
	}
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name,Class<T> t){
		return (T)beanMap.get(name);
	}
	private static String toLowerForStart(String name){
		return name.substring(0,1).toLowerCase()+name.substring(1);
	}
	public static void main(String[] args) {
		String iocClassScan=FrameworkConstant.properties.getProperty("ioc-class-scan");
		System.out.println(iocClassScan);
	}
}
