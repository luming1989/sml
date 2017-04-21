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
import org.hw.sml.tools.RegexUtils;


@SuppressWarnings({ "unchecked", "rawtypes" })
public class BeanHelper {
	public static final String IOC_BEAN_SCAN="ioc-bean-scan";
	private static  Map<String,Object> beanMap=MapUtils.newLinkedHashMap();
	private static  Map<String,Object> propertyInitBeanMap=MapUtils.newLinkedHashMap();
	private static Map<String,Boolean> beanErrInfo=MapUtils.newLinkedHashMap();
	static{
		try {
			beanMap.put("smlBeanHelper", new BeanHelper());
			String packageName=getValue(IOC_BEAN_SCAN);
			List<Class<?>> classes=MapUtils.newArrayList();
			boolean isAnnotationScan=packageName!=null&&packageName.trim().length()>0;
			if(isAnnotationScan){
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
					String value=getValue(key);
					List<String> ms=RegexUtils.matchGroup("\\$\\{[\\w|.|-]+\\}",value);
					if(ms.size()==0) continue;
					for(String m:ms){
						String vt=getValue(m.substring(2,m.length()-1));
						if(vt!=null)
						value=value.replace(m,vt);
					}
					FrameworkConstant.otherProperties.put(key, value);
					//
					continue;
				}
				String beanName=key.replaceFirst("bean-","");
				Map<String,String> beanKeyValue=getBeanKeyValue(key);
				String classpath=beanKeyValue.get("class");
				Assert.notNull(classpath,"bean["+beanName+"] class is null!");
				Assert.isTrue(!beanMap.containsKey(beanName),"bean["+beanName+"] name is conflict!");
				Object bean=null;
				if(classpath.startsWith("[")&&classpath.endsWith("]")){
					classpath=classpath.substring(1,classpath.length()-1);
					bean=Array.newInstance(Class.forName(classpath),beanKeyValue.size()-1);
				}else if(classpath.endsWith(")")&&classpath.contains("(")){
					//通过构造初始化bean
					String[] clps=classpath.split("\\(");
					String clp=clps[0],clpBeanElp=clps[1].substring(0, clps[1].length()-1);
					String[] clpBeans=clpBeanElp.split(",");
					Object[] consts=new Object[clpBeans.length];
					Class<?>[] constCls=new Class<?>[clpBeans.length];
					for(int i=0;i<consts.length;i++){
						String keyP=clpBeans[i];
						B b=evel(keyP);
						consts[i]=b.t;
						constCls[i]=b.c;
					}
					bean=Class.forName(clp).getConstructor(constCls).newInstance(consts);
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
								Assert.notNull(getValue(fieldType,et.getValue()),beanName+"-property["+et.getValue()+"] is not configed!");
								Assert.notNull(field, "bean["+beanName+"-"+bean.getClass()+"] has not field["+fieldName+"]");
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
					//字段注入方式
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
						filed.set(beanMap.get(beanName),v.equals("")?null:v);
					}
					//方法注入方式
					Method[] methods=ClassUtil.getMethods(clazz);
					for(Method method:methods){
						Inject inject=method.getAnnotation(Inject.class);
						if(inject==null){
							continue;
						}
						String injectName=inject.value();
						if(injectName==null||injectName.trim().length()==0){
							injectName=toLowerForStart(method.getParameterTypes()[0].getSimpleName());
						}
						method.setAccessible(true);
						Object v=beanMap.get(injectName)==null?beanMap.get(method.getParameterTypes()[0]):beanMap.get(injectName);
						Assert.notNull(v, "beanName:["+beanName+"-"+bean.getClass()+"],method inject ["+method.getName()+" params ] v is null");
						method.invoke(beanMap.get(beanName),v);
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
					//方法注入方式
					Method[] methods=ClassUtil.getMethods(clazz);
					for(Method method:methods){
						Val val=method.getAnnotation(Val.class);
						if(val==null){
							continue;
						}
						String configName=val.value();
						method.setAccessible(true);
						Assert.notNull(getValue(configName), "beanName:["+beanName+"-"+bean.getClass()+"],method param "+method.getName()+" is null");
						method.invoke(beanMap.get(beanName),ClassUtil.convertValueToRequiredType(getValue(configName),method.getParameterTypes()[0]));
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
	public static Object getValue(String type,String key) throws IllegalArgumentException, IllegalAccessException{
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
			String keyElp=key.substring(2,key.length()-1);
			 if(!beanErrInfo.containsKey(keyElp))
				return beanMap.get(keyElp);
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
	public static Map<String,String> getPropertyKeyStart(String startKey){
		Map<String,String> result=MapUtils.newLinkedHashMap();
		//--
		Enumeration<Object> keys=FrameworkConstant.otherProperties.keys();
		while(keys.hasMoreElements()){
			String key=keys.nextElement().toString();
			if(key.startsWith(startKey)){
				result.put(key,getValue(key));
			}
		}
		//--
		return result;
	}
	public static Map<String,Object> getBeanMap(){
		return beanMap;
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
	private static class B<T>{
		private T t;
		private Class<T> c;
		public B(){}
		public B(T t, Class<T> c) {
			super();
			this.t = t;
			this.c = c;
		}
		
	}
	public static Object evelV(String elp) throws IllegalArgumentException, IllegalAccessException{
		return evel(elp).t;
	}
	private static B evel(String elp) throws IllegalArgumentException, IllegalAccessException{
		Object value=null;
		if(elp.startsWith("${")&&elp.endsWith("}")){
			value= getValue(null,elp);
		}else if(elp.startsWith("#{")&&elp.endsWith("}")){
			String keyElp=elp.substring(2,elp.length()-1);
			if(elp.contains(".")){
				value=loopElp(keyElp);
			}else if(keyElp.contains("[")&&keyElp.endsWith("]")){
				String elps[]=keyElp.split("\\[");
				String bn=elps[0];int index=Integer.parseInt(elps[1].substring(0,elps[1].length()-1));
				Assert.isTrue(beanMap.containsKey(bn),"bean "+bn+" is not exists!");
				Object b=beanMap.get(bn);
				if(b.getClass().isArray()){
					value=Array.get(b, index);
				}else if(b instanceof List){
					value= ((List)b).get(index);
				}else{
					Assert.isTrue(false, "elp["+elp+"] is not a array or list!");
				}
			}else{
				Assert.isTrue(beanMap.containsKey(keyElp),"bean "+keyElp+" is not exists!");
				value=beanMap.get(keyElp);
			}
		}else{
			String keyP=elp;
			B b=new B();
			if(keyP.startsWith("''")&&keyP.endsWith("''")){
				b.t=keyP.substring(2,keyP.length()-2).charAt(0);b.c=Character.class;
			}else if(keyP.startsWith("'")&&keyP.endsWith("'")){
				b.t=keyP.substring(1,keyP.length()-1).charAt(0);b.c=char.class;
			} else if(keyP.startsWith("\"")&&keyP.endsWith("\"")){
				b.t=keyP.substring(1, keyP.length()-1);b.c=String.class;
			}else if(keyP.endsWith("l")||keyP.endsWith("L")){
				b.t=Long.parseLong(keyP.substring(0, keyP.length()-1));b.c=keyP.endsWith("L")?Long.class:long.class;
			}else if(keyP.endsWith("d")||keyP.endsWith("D")){
				b.t=Double.parseDouble(keyP.substring(0, keyP.length()-1));b.c=keyP.endsWith("D")?Double.class:double.class;
			}else if(keyP.endsWith("f")||keyP.endsWith("F")){
				b.t=Float.parseFloat(keyP.substring(0, keyP.length()-1));b.c=keyP.endsWith("F")?Float.class:float.class;
			}else if(keyP.endsWith("s")||keyP.endsWith("S")){
				b.t=Short.parseShort(keyP.substring(0, keyP.length()-1));b.c=keyP.endsWith("S")?Short.class:short.class;
			}else if(keyP.endsWith("i")||keyP.endsWith("I")){
				b.t=Integer.parseInt(keyP.substring(0, keyP.length()-1));b.c=keyP.endsWith("I")?Integer.class:int.class;
			}else if(keyP.equalsIgnoreCase("true")||keyP.equalsIgnoreCase("false")){			
				b.t=Boolean.valueOf(keyP);b.c=(keyP.equals("TRUE")||keyP.equals("FLASE"))?Boolean.class:boolean.class;
			}else{
				b.t=keyP;b.c=Object.class;
			}
			return b;
		}
		return new B(value,value==null?null:value.getClass());
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
	public static Object loopElp(String elp) throws IllegalArgumentException, IllegalAccessException{
		String elps[]=elp.split("\\.");
		Object bean=null;
		if(elps[0].contains("[")&&elps[0].endsWith("]")){
			bean=evelV("#{"+elps[0]+"}");
		}else
			bean=getBean(elps[0]);
		if(elps.length==1){
			return bean;
		}
		return loopElp(bean,elps[1],elps,1);
	}
	private static Object loopElp(Object bean,String bnelp,String[] ss,int pos) throws IllegalArgumentException, IllegalAccessException{
		Object value=null;
		if(bnelp.contains("(")&&bnelp.contains(")")){
			String[] melp=bnelp.split("\\(");
			String mn=melp[0];
			String clpP=melp[1].substring(0,melp[1].length()-1);
			String[] clpBeans=new String[0];
			if(!clpP.equals(""))
			 clpBeans=clpP.split(",");
			Object[] consts=new Object[clpBeans.length];
			Class<?>[] constCls=new Class<?>[clpBeans.length];
			for(int i=0;i<consts.length;i++){
				String keyP=clpBeans[i];
				B b=evel(keyP);
				consts[i]=b.t;
				constCls[i]=b.c;
			}
			try {
				value=ClassUtil.invokeMethod(bean, mn, constCls, consts);
			}  catch (Exception e) {
				Assert.isTrue(false,"elp-["+bnelp+"] error["+e+"]!");
			}
		}else{
			value= ClassUtil.getFieldValue(bean,bnelp);
		}
		if(value==null){
			return null;
		}
		if(ss.length==pos+1){
			return value;
		}
		return loopElp(value,ss[pos+1],ss,pos+1);
	}
	public static void main(String[] args) {
		BeanHelper.start();
	}
}