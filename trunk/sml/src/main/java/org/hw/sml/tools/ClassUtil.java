package org.hw.sml.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
 * 类操作工具类
 *
 */
public class ClassUtil {

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    public static String getClassPath() {
        String classpath = "";
        URL resource = getClassLoader().getResource("");
        if (resource != null) {
            classpath = resource.getPath();
        }
        return classpath;
    }

    /**
     * 加载类（将自动初始化）
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    /**
     * 加载类
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;
    }
    @SuppressWarnings("unchecked")
   	public static <T> T newInstance(String classpath,Class<T> clazz){
       	try {
   			return (T)loadClass(classpath).newInstance();
   		} catch (InstantiationException e) {
   			e.printStackTrace();
   		} catch (IllegalAccessException e) {
   			e.printStackTrace();
   		}
       	return null;
       }
    @SuppressWarnings("unchecked")
	public static <T> T newInstance(String classpath){
    	try {
			return (T) loadClass(classpath).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	return null;
    }
    public static boolean isInt(Class<?> type) {
        return type.equals(int.class) || type.equals(Integer.class);
    }
    public static boolean isLong(Class<?> type) {
        return type.equals(long.class) || type.equals(Long.class);
    }
    public static boolean isDouble(Class<?> type) {
        return type.equals(double.class) || type.equals(Double.class);
    }
    public static boolean isFloat(Class<?> type) {
        return type.equals(float.class) || type.equals(Float.class);
    }
    public static boolean isString(Class<?> type) {
        return type.equals(String.class);
    }
    @SuppressWarnings("rawtypes")
	public static Object convertValueToRequiredType(Object value, Class requiredType) {
    	if(isInt(requiredType)){
    		requiredType=Integer.class;
    	}else if(isLong(requiredType)){
    		requiredType=Long.class;
    	}else if(isDouble(requiredType)){
    		requiredType=Double.class;
    	}else if(isFloat(requiredType)){
    		requiredType=Float.class;
    	}
    	if(requiredType.equals(boolean.class)||requiredType.equals(Boolean.class)){
    		return Boolean.valueOf(String.valueOf(value));
    	}
		if (String.class.equals(requiredType)) {
			return value.toString();
		}
		else if (Number.class.isAssignableFrom(requiredType)) {
			if (value instanceof Number) {
				return NumberUtils.convertNumberToTargetClass(((Number) value), requiredType);
			}
			else {
				return NumberUtils.parseNumber(value.toString(), requiredType);
			}
		}else{
			return value;
		}
		
	}
    public static Field[] getFields(Class<?> clazz){
    	Field[] fields=clazz.getFields();
		Field[] dfs=clazz.getDeclaredFields();
		Field[] result=getFieldts(fields,dfs);
    	while(clazz.getSuperclass()!=null){
    		clazz=clazz.getSuperclass();
    		Field[] fs=clazz.getDeclaredFields();
    		Field[] ds=clazz.getDeclaredFields();
    		Field[] f=getFieldts(fs,ds);
    		result=getFieldts(result,f);
    	}
    	return result;
    }
    private static Field[]  getFieldts(Field[] fields,Field[] dfs){
    	Field[] result=new Field[fields.length+dfs.length];
    	for(int i=0;i<fields.length;i++){
    		result[i]=fields[i];
    	}
    	for(int i=fields.length;i<fields.length+dfs.length;i++){
    		result[i]=dfs[i-fields.length];
    	}
    	return result;
    }
    public static Method[] getMethods(Class<?> clazz){
    	List<Method> methods=MapUtils.newArrayList();
    	Method[] fields=clazz.getMethods();
    	Method[] dfs=clazz.getDeclaredMethods();
    	Method[] result=getMethodts(fields,dfs);
    	while(clazz.getSuperclass()!=null&&!clazz.getSuperclass().equals(Object.class)){
    		Method[] fs=clazz.getSuperclass().getDeclaredMethods();
    		Method[] ds=clazz.getSuperclass().getDeclaredMethods();
    		Method[] f=getMethodts(fs,ds);
    		result=getMethodts(result,f);
    		clazz=clazz.getSuperclass();
    	}
    	for(Method method:result){
    		if(!methods.contains(method))
    		methods.add(method);
    	}
    	return methods.toArray(new Method[]{});
    }
    private static Method[] getMethodts(Method[] fields,Method[] dfs){
    	Method[] result=new Method[fields.length+dfs.length];
    	for(int i=0;i<fields.length;i++){
    		result[i]=fields[i];
    	}
    	for(int i=fields.length;i<fields.length+dfs.length;i++){
    		result[i]=dfs[i-fields.length];
    	}
    	return result;
    }
    public static Method getMethod(Class<?> clazz,String name){
    	if(clazz==null){
    		return null;
    	}
    	Method[] ms=clazz.getDeclaredMethods();
    	for(Method m:ms){
    		if(m.getName().equals(name)){
    			return m;
    		}
    	}
    	ms=clazz.getMethods();
    	for(Method m:ms){
    		if(m.getName().equals(name)){
    			return m;
    		}
    	}
    	return getMethod(clazz.getSuperclass(),name);
    }
    public static Field getField(Class<?> clazz,String name){
    	if(clazz==null){
    		return null;
    	}
    	Field[] fs=clazz.getDeclaredFields();
    	for(Field f:fs){
    		if(f.getName().equals(name)){
    			return f;
    		}
    	}
    	fs=clazz.getFields();
    	for(Field f:fs){
    		if(f.getName().equals(name)){
    			return f;
    		}
    	}
    	return getField(clazz.getSuperclass(), name);
    }
    public  static boolean hasClass(String classPath){
    	try{
    		Class.forName(classPath);
    	}catch(Exception e){
    		return false;
    	}
    	return true;
    }
}
