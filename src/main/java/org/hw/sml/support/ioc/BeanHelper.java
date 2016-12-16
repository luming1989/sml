package org.hw.sml.support.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.support.ClassHelper;
import org.hw.sml.support.LoggerHelper;
import org.hw.sml.support.ioc.annotation.Bean;
import org.hw.sml.support.ioc.annotation.Init;
import org.hw.sml.support.ioc.annotation.Inject;
import org.hw.sml.support.ioc.annotation.Stop;
import org.hw.sml.support.ioc.annotation.Val;
import org.hw.sml.tools.Assert;
import org.hw.sml.tools.ClassUtil;
import org.hw.sml.tools.MapUtils;


@SuppressWarnings({ "unchecked", "rawtypes" })
public class BeanHelper {
	private static  Map<String,Object> beanMap=MapUtils.newHashMap();
	private static  Map<String,Object> propertyInitBeanMap=MapUtils.newHashMap();
	static{
		try {
			String packageName=FrameworkConstant.getProperty("ioc-bean-scan");
			List<Class<?>> classes=MapUtils.newArrayList();
			if(packageName!=null){
				for(String pn:packageName.split(",| ")){
					List<Class<?>> cls=ClassHelper.getClassListByAnnotation(pn, Bean.class);
					classes.addAll(cls);
				}
			}
			//对属性文件bean读取解析
			Enumeration<Object> keys=FrameworkConstant.otherProperties.keys();
			while(keys.hasMoreElements()){
				String key=keys.nextElement().toString();
				if(!key.startsWith("bean-")){
					continue;
				}
				String beanName=key.replace("bean-","");
				Object bean=ClassUtil.newInstance(MapUtils.transMapFromStr(FrameworkConstant.getProperty(key)).get("class"));
				Assert.isTrue(!beanMap.containsKey(beanName),"bean["+beanName+"-"+bean.getClass()+"] name is conflict!");
				beanMap.put(beanName,bean);
				propertyInitBeanMap.put(beanName,bean);
			}
			//初始化属性值
			for(Map.Entry<String,Object> entry:propertyInitBeanMap.entrySet()){
				String beanName=entry.getKey();
				Object bean=entry.getValue();
				//如果bean属于map类
				String propertyValue=FrameworkConstant.getProperty("bean-"+beanName);
				Map<String,String> pvs=MapUtils.transMapFromStr(propertyValue);
				for(Map.Entry<String,String> et:pvs.entrySet()){
					String k=et.getKey();
					if(k.startsWith("p-")){
						String[] ktoken=k.split("-");
						String fieldName=ktoken[1];
						String fieldType=ktoken.length==2?null:ktoken[2];
						if(bean instanceof Map){
							if(fieldType==null)
								((Map) bean).put(fieldName,et.getValue());
							else if(fieldType.equals("v"))
								((Map) bean).put(fieldName,FrameworkConstant.getProperty(et.getValue()));
							else if(fieldType.equals("b"))
								((Map) bean).put(fieldName,propertyInitBeanMap.get(et.getValue()));
						}else if(bean instanceof List){
							if(fieldType==null)
								((List) bean).add(et.getValue());
							else if(fieldType.equals("v"))
								((List) bean).add(FrameworkConstant.getProperty(et.getValue()));
							else if(fieldType.equals("b"))
								((List) bean).add(propertyInitBeanMap.get(et.getValue()));
						}else if(bean.getClass().isArray()){
							
						}else{
							Field field=ClassUtil.getField(bean.getClass(),fieldName);
							if(fieldType!=null&&!fieldType.startsWith("m"))
							Assert.notNull(field, "bean["+beanName+"-"+bean.getClass()+"] has not field["+fieldName+"]");
							if(field!=null)
							field.setAccessible(true);
							if(fieldType==null)
								field.set(bean,ClassUtil.convertValueToRequiredType(et.getValue(), field.getType()));
							else if(fieldType.equals("v")){
								Assert.notNull(FrameworkConstant.getProperty(et.getValue()), "bean["+beanName+"-"+bean.getClass()+"] has not field key["+et.getValue()+"]");
								field.set(bean,ClassUtil.convertValueToRequiredType(FrameworkConstant.getProperty(et.getValue()), field.getType()));
							}
							else if(fieldType.equals("b")){
								Assert.notNull(propertyInitBeanMap.get(et.getValue()),  "bean["+beanName+"-"+bean.getClass()+"] has not field bean["+et.getValue()+"]");
								field.set(bean,propertyInitBeanMap.get(et.getValue()));
							}else if(fieldType.equals("m")){
								String methodName="set"+toUpperForStart(fieldName);
								Method method=ClassUtil.getMethod(bean.getClass(),methodName);
								Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"] for field["+fieldName+"]!");
								method.invoke(bean,ClassUtil.convertValueToRequiredType(et.getValue(),method.getGenericParameterTypes()[0].getClass()));
							}else if(fieldType.equals("mv")){
								String methodName="set"+toUpperForStart(fieldName);
								Method method=ClassUtil.getMethod(bean.getClass(),methodName);
								Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"] for field["+fieldName+"]!");
								method.invoke(bean,ClassUtil.convertValueToRequiredType(FrameworkConstant.getProperty(et.getValue()),method.getGenericParameterTypes()[0].getClass()));
							}
							else
								field.set(bean,ClassUtil.convertValueToRequiredType(et.getValue(), field.getType()));
						}
					}else if(k.startsWith("m-")){
						String[] ktoken=k.split("-");
						String methodName=ktoken[1];
						String methodType=ktoken.length==2?null:ktoken[2];
						if(methodType==null){
							Method method=ClassUtil.getMethod(bean.getClass(),methodName);
							Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"]!");
							method.invoke(bean,ClassUtil.convertValueToRequiredType(et.getValue(),method.getGenericParameterTypes()[0].getClass()));
						}else if(methodType.equals("v")){
							Method method=ClassUtil.getMethod(bean.getClass(),methodName);
							Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"]!");
							Assert.notNull(FrameworkConstant.getProperty(et.getValue()), "bean["+beanName+"-"+bean.getClass()+"] has not method params ["+methodName+"] for params bean["+et.getValue()+"]!");
							method.invoke(bean,ClassUtil.convertValueToRequiredType(FrameworkConstant.getProperty(et.getValue()),method.getGenericParameterTypes()[0].getClass()));
						}else if(methodType.equals("b")){
							Method method=ClassUtil.getMethod(bean.getClass(),methodName);
							Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"]!");
							Assert.notNull(propertyInitBeanMap.get(et.getValue()),  "bean["+beanName+"-"+bean.getClass()+"] has not method ["+methodName+"] for params bean["+et.getValue()+"]");
							method.invoke(bean,ClassUtil.convertValueToRequiredType(propertyInitBeanMap.get(et.getValue()),method.getGenericParameterTypes()[0].getClass()));
						}
					}
				}
			}
			//初始化方法
			for(Map.Entry<String,Object> entry:propertyInitBeanMap.entrySet()){
				String beanName=entry.getKey();
				final Object bean=entry.getValue();
				//
				String propertyValue=FrameworkConstant.getProperty("bean-"+beanName);
				Map<String,String> pvs=MapUtils.transMapFromStr(propertyValue);
				for(final Map.Entry<String,String> et:pvs.entrySet()){
					String k=et.getKey();
					String methodName=et.getValue();
					if(k.equals("init-method")){
						final Method method=ClassUtil.getMethod(bean.getClass(),methodName);
						Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not init-method["+methodName+"]");
						method.setAccessible(true);
						method.invoke(bean,new Object[]{});
					}else if(k.equals("stop-method")){
						final Method method=ClassUtil.getMethod(bean.getClass(),methodName);
						Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not stop-method["+methodName+"]");
						method.setAccessible(true);
						Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
							public void run() {
								try {
									method.invoke(bean,new Object[]{});
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								} 
							}
						}));
					}
				}
			}
			//
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
					Field[] fields=ClassUtil.getFields(clazz);
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
						Assert.notNull(v, "beanName:["+beanName+"-"+bean.getClass()+"],field inject ["+filed.getName()+"] v is null");
						filed.set(beanMap.get(beanName),v);
					}
				}
				//@Val进行赋值
				for(Class<?> clazz:classes){
					Bean bean=clazz.getAnnotation(Bean.class);
					String beanName=bean.value();
					if(beanName==null||beanName.trim().length()==0){
						beanName=toLowerForStart(clazz.getSimpleName());
					}
					//
					Field[] fields=ClassUtil.getFields(clazz);
					for(Field filed:fields){
						Val config=filed.getAnnotation(Val.class);
						if(config==null){
							continue;
						}
						String configName=config.value();
						Assert.notNull(configName, "beanName:"+beanName+"-"+bean.getClass()+",field config "+filed.getName()+" is null");
						filed.setAccessible(true);
						Assert.notNull(FrameworkConstant.getProperty(configName), "beanName:["+beanName+"-"+bean.getClass()+"],field value "+filed.getName()+" is null");
						filed.set(beanMap.get(beanName),ClassUtil.convertValueToRequiredType(FrameworkConstant.getProperty(configName),filed.getType()));
					}
				}
				//@Init方法
				initAnnotationInvoke(classes);
				//@Stop方法销毁
				for(Class<?> clazz:classes){
					Bean bean=clazz.getAnnotation(Bean.class);
					 String beanName=bean.value();
					if(beanName==null||beanName.trim().length()==0){
						beanName=toLowerForStart(clazz.getSimpleName());
					}
					final String tempBean=beanName;
					for(final Method method:ClassUtil.getMethods(clazz)){
						Stop stop=method.getAnnotation(Stop.class);
						if(stop!=null){
							method.setAccessible(true);
							Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										method.invoke(beanMap.get(tempBean), new Object[]{});
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									} catch (IllegalAccessException e) {
										e.printStackTrace();
									} catch (InvocationTargetException e) {
										e.printStackTrace();
									}
								}
							}));
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
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		LoggerHelper.info(BeanHelper.class,"bean initd--->"+beanMap.keySet());
	}
	public static void initAnnotationInvoke(List<Class<?>> classes) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		//@Init方法
		for(Class<?> clazz:classes){
			Bean bean=clazz.getAnnotation(Bean.class);
			String beanName=bean.value();
			if(beanName==null||beanName.trim().length()==0){
				beanName=toLowerForStart(clazz.getSimpleName());
			}
			Method[] ms=ClassUtil.getMethods(clazz);
			List<String> initdMethod=MapUtils.newArrayList();
			for(Method method:ms){
				Init init=method.getAnnotation(Init.class);
				if(init!=null){
					method.setAccessible(true);
					if(initdMethod.contains(method.getName())){
						continue;
					}
					method.invoke(beanMap.get(beanName), new Object[]{});
					initdMethod.add(method.getName());
				}
			}
		}
	}
	public static int start(){
		return 1;
	}
	public static <T> T getBean(String name){
		return (T)beanMap.get(name);
	}
	private static String toLowerForStart(String name){
		return name.substring(0,1).toLowerCase()+name.substring(1);
	}
	private static String toUpperForStart(String name){
		return name.substring(0,1).toUpperCase()+name.substring(1);
	}
}
