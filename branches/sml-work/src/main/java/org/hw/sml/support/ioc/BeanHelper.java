package org.hw.sml.support.ioc;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
	private static Map<String,Boolean> beanErrInfo=MapUtils.newHashMap();
	static{
		try {
			String packageName=getValue("ioc-bean-scan");
			List<Class<?>> classes=MapUtils.newArrayList();
			boolean isAnnotationScan=packageName!=null&&packageName.trim().length()>0;
			if(isAnnotationScan){
				LoggerHelper.info(BeanHelper.class,"bean-scan package|---->"+packageName);
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
				Map<String,String> beanKeyValue=getBeanKeyValue(key);
				String classpath=beanKeyValue.get("class");
				Assert.notNull(classpath,"bean["+beanName+"] class is null!");
				Assert.isTrue(!beanMap.containsKey(beanName),"bean["+beanName+"] name is conflict!");
				Object bean=null;
				if(classpath.startsWith("[")&&classpath.endsWith("]")){
					classpath=classpath.substring(1,classpath.length()-1);
					bean=Array.newInstance(Class.forName(classpath),beanKeyValue.size()-1);
				}else{
					if(!Boolean.valueOf(beanKeyValue.get("passErr"))){
						bean=ClassUtil.newInstance(classpath);
					}else{
						try{
							bean=ClassUtil.newInstance(classpath);
						}catch(Exception e){
							e.printStackTrace();
							beanErrInfo.put(beanName,false);
						}
					}
				}
				beanMap.put(beanName,bean);
				propertyInitBeanMap.put(beanName,bean);
			}
			//查找所有Bean注解并生成对象
			if(isAnnotationScan){
				for(Class<?> clazz:classes){
					Bean bean=clazz.getAnnotation(Bean.class);
					String beanName=bean.value();
					if(beanName==null||beanName.trim().length()==0){
						beanName=toLowerForStart(clazz.getSimpleName());
					}
					beanMap.put(beanName,clazz.newInstance());
				}
			}
			//初始化属性值
			for(Map.Entry<String,Object> entry:propertyInitBeanMap.entrySet()){
				String beanName=entry.getKey();
				Object bean=entry.getValue();
				if(beanErrInfo.containsKey(beanName)){
					continue;
				}
				//如果bean属于map类
				Map<String,String> pvs=getBeanKeyValue(beanName);
				int i=0;
				for(Map.Entry<String,String> et:pvs.entrySet()){
					String k=et.getKey();
					if(k.startsWith("p-")){
						String[] ktoken=getPorM(k);
						String fieldName=ktoken[1];
						String fieldType=ktoken[2];
						if(bean instanceof Map){
								((Map) bean).put(fieldName,getValue(fieldType,et.getValue()));
						}else if(bean instanceof List){
								((List) bean).add(getValue(fieldType,et.getValue()));
						}else if(bean.getClass().isArray()){
								Array.set(bean, i++,ClassUtil.convertValueToRequiredType(getValue(fieldType,et.getValue()),bean.getClass().getComponentType()));
						}else{
							Field field=ClassUtil.getField(bean.getClass(),fieldName);
							if(fieldType!=null&&!fieldType.startsWith("m"))
							Assert.notNull(field, "bean["+beanName+"-"+bean.getClass()+"] has not field["+fieldName+"]");
							if(field!=null)
							field.setAccessible(true);
							if(fieldType==null||fieldType.equals("v")||fieldType.equals("b")){
								Object value=ClassUtil.convertValueToRequiredType(getValue(fieldType,et.getValue()), field.getType());
								Assert.notNull(value, "bean["+beanName+"-"+bean.getClass()+"] has not field "+fieldType+"["+et.getValue()+"]");
								field.set(bean,value.equals("")?null:value);
							}else if(fieldType.equals("m")||fieldType.equals("mv")||fieldType.equals("mb")){
								String methodName="set"+toUpperForStart(fieldName);
								Method method=ClassUtil.getMethod(bean.getClass(),methodName);
								method.setAccessible(true);
								Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"] for field["+fieldName+"]!");
								Object value=ClassUtil.convertValueToRequiredType(getValue(fieldType.replace("m",""),et.getValue()),method.getGenericParameterTypes()[0].getClass());
								Assert.notNull(value, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"] for field "+fieldType+" params["+et.getValue()+"]!");
								method.invoke(bean,value.equals("")?null:value);
							}
							else
								field.set(bean,ClassUtil.convertValueToRequiredType(et.getValue(), field.getType()));
						}
					}else if(k.startsWith("m-")){
						String[] ktoken=getPorM(k);
						String methodName=ktoken[1];
						String methodType=ktoken[2];
						Method method=ClassUtil.getMethod(bean.getClass(),methodName);
						Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not method["+methodName+"]!");
						Object value=ClassUtil.convertValueToRequiredType(getValue(methodType,et.getValue()),method.getGenericParameterTypes()[0].getClass());
						Assert.notNull(value,"bean["+beanName+"-"+bean.getClass()+"] method ["+methodName+"] for params "+methodType+"["+et.getValue()+"]");
						method.invoke(bean, value.equals("")?null:value);
					}
				}
			}
			//注解类字段进行注入或赋值
			if(isAnnotationScan){
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
						Assert.notNull(v, "beanName:["+beanName+"-"+bean.getClass()+"],field inject ["+filed.getName()+"|"+injectName+"] v is null");
						filed.set(beanMap.get(beanName),v.equals("")?null:v);
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
						Assert.notNull(getValue(configName), "beanName:["+beanName+"-"+bean.getClass()+"],field value "+filed.getName()+" is null");
						filed.set(beanMap.get(beanName),ClassUtil.convertValueToRequiredType(getValue(configName),filed.getType()));
					}
				}
			}
			//初始化属性文件配置中方法或注入关闭勾子
			for(Map.Entry<String,Object> entry:propertyInitBeanMap.entrySet()){
				String beanName=entry.getKey();
				if(beanErrInfo.containsKey(beanName)){
					continue;
				}
				final Object bean=entry.getValue();
				Map<String,String> pvs=getBeanKeyValue(beanName);
				for(final Map.Entry<String,String> et:pvs.entrySet()){
					String k=et.getKey();
					String methodName=et.getValue();
					if(k.equals("init-method")){
						final Method method=ClassUtil.getMethod(bean.getClass(),methodName);
						Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not init-method["+methodName+"]");
						method.setAccessible(true);
						boolean isDelay=Boolean.valueOf(pvs.get("isDelay"));
						LoggerHelper.info(BeanHelper.class,"beanName["+beanName+"] init-method["+methodName+"] isDelay["+(isDelay?MapUtils.getString(pvs,"sleep","0")+"s":"false")+"]...");
						methodInvoke(bean, method, Boolean.valueOf(pvs.get("igErr")), isDelay,Long.parseLong(MapUtils.getString(pvs,"sleep","0")));
					}else if(k.equals("stop-method")||k.equals("destroy-method")){
						final Method method=ClassUtil.getMethod(bean.getClass(),methodName);
						Assert.notNull(method, "bean["+beanName+"-"+bean.getClass()+"] has not stop-method["+methodName+"]");
						method.setAccessible(true);
						Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
							public void run() {
								try {
									method.invoke(bean,new Object[]{});
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}));
					}
				}
			}
			//初始化注解方法
			if(packageName!=null&&packageName.trim().length()>0){
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
								public void run() {
									try {
										method.invoke(beanMap.get(tempBean), new Object[]{});
									} catch (Exception e) {
										e.printStackTrace();
									} 
								}
							}));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		} 
		LoggerHelper.info(BeanHelper.class,"bean initd--->"+beanMap.keySet());
	}
	public static void initAnnotationInvoke(List<Class<?>> classes) throws Exception{
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
					initdMethod.add(method.getName());
					methodInvoke(beanMap.get(beanName), method, init.igErr(), init.isDelay(), init.sleep());
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
	public static <T> T getBean(Class<T> t){
		for(Map.Entry<String,Object> entry:beanMap.entrySet()){
			Object val=entry.getValue();
			if(val.getClass().equals(t))
				return (T)val;
			if(t.isInstance(val))
				return (T)val;
		}
		return null;
	}
	public static String getValue(String key){
		return FrameworkConstant.getProperty(key);
	}
	public static Object getValue(String type,String key){
		if(type==null){
			//return key;
		}else if(type.equals("v")){
			return getValue(key);
		}else if(type.equals("b")){
			if(!beanErrInfo.containsKey(key))
				return beanMap.get(key);
			else
				return "";
		}
		if(key.startsWith("${")&&key.endsWith("}")){
			return getValue(key.substring(2,key.length()-1));
		}else if(key.startsWith("#{")&&key.endsWith("}")){
			if(!beanErrInfo.containsKey(key))
				return beanMap.get(key.substring(2,key.length()-1));
			else
				return "";
		}
		return key;
	}
	public static Map<String,String> getBeanKeyValue(String key){
		if(!key.startsWith("bean-")){
			key="bean-"+key;
		}
		String value=getValue(key);
		Assert.notNull(value,key+" not found!");
		return MapUtils.transMapFromStr(value);
	}
	private static String toLowerForStart(String name){
		return name.substring(0,1).toLowerCase()+name.substring(1);
	}
	private static String toUpperForStart(String name){
		return name.substring(0,1).toUpperCase()+name.substring(1);
	}
	private static String[] getPorM(String key){
		String[] pms= key.split("-");
		return new String[]{pms[0],pms[1],pms.length==3?pms[2]:null};
	}
	private static void methodInvoke(final Object bean,final Method method,boolean igErr,boolean isDelay,final long ms) throws Exception{
		if(isDelay){
			Thread thread=new Thread(new Runnable(){
				public void run() {
					try {
						Thread.sleep(ms*1000);
						method.invoke(bean,new Object[]{});
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}});
			thread.start();
			LoggerHelper.info(BeanHelper.class,"bean["+bean.getClass()+"]"+method.getName()+" lazy load sleep "+ms+" s!");
		}else{
			if(igErr){
				try {
					method.invoke(bean,new Object[]{});
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}else{
				method.invoke(bean,new Object[]{});
			}
		}
		
	}
}