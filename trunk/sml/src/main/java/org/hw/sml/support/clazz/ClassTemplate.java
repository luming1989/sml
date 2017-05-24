package org.hw.sml.support.clazz;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hw.sml.tools.ClassUtil;

/**
 * 用于获取类的模板类
 * 
 */
public abstract class ClassTemplate {

	    protected final String packageName;

	    protected ClassTemplate(String packageName) {
	        this.packageName = packageName;
	    }

	    public final List<Class<?>> getClassList() {
	        List<Class<?>> classList = new ArrayList<Class<?>>();
	        try {
	            Enumeration<URL> urls = ClassUtil.getClassLoader().getResources(packageName.replace(".", "/"));
	            while (urls.hasMoreElements()) {
	                URL url = urls.nextElement();
	                if (url != null) {
	                    String protocol = url.getProtocol();
	                    if (protocol.equals("file")) {
	                        String packagePath = url.getPath().replaceAll("%20", " ");
	                        addClass(classList, packagePath, packageName);
	                    } else if (protocol.equals("jar")) {
	                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
	                        JarFile jarFile = jarURLConnection.getJarFile();
	                        Enumeration<JarEntry> jarEntries = jarFile.entries();
	                        while (jarEntries.hasMoreElements()) {
	                            JarEntry jarEntry = jarEntries.nextElement();
	                            String jarEntryName = jarEntry.getName();
	                            if (jarEntryName.endsWith(".class")) {
	                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
	                                doAddClass(classList, className);
	                            }
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
	        }
	        return classList;
	    }

	    private void addClass(List<Class<?>> classList, String packagePath, String packageName) {
	        try {
	            File[] files = new File(packagePath).listFiles(new FileFilter() {
	                @Override
	                public boolean accept(File file) {
	                    return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
	                }
	            });
	            for (File file : files) {
	                String fileName = file.getName();
	                if (file.isFile()) {
	                    String className = fileName.substring(0, fileName.lastIndexOf("."));
	                    if (isNotEmpty(packageName)) {
	                        className = packageName + "." + className;
	                    }
	                    doAddClass(classList, className);
	                } else {
	                    String subPackagePath = fileName;
	                    if (isNotEmpty(packagePath)) {
	                        subPackagePath = packagePath + "/" + subPackagePath;
	                    }
	                    String subPackageName = fileName;
	                    if (isNotEmpty(packageName)) {
	                        subPackageName = packageName + "." + subPackageName;
	                    }
	                    // 递归调用
	                    addClass(classList, subPackagePath, subPackageName);
	                }
	            }
	        } catch (Exception e) {
	        }
	    }

	   
		private void doAddClass(List<Class<?>> classList, String className) {
	        // 加载类
	        Class<?> cls = ClassUtil.loadClass(className, false);
	        // 判断是否可以添加类
	        if (checkAddClass(cls)) {
	            // 添加类
	            classList.add(cls);
	        }
	    }

	    /**
	     * 验证是否允许添加类
	     */
	    public abstract boolean checkAddClass(Class<?> cls);
	    private boolean isNotEmpty(String string) {
			return string!=null&&string.trim().length()>0;
		}

	}