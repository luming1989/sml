package org.hw.sml.test;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.hw.sml.context.SmlContextUtils;
import org.hw.sml.core.resolver.JsEngine;
import org.hw.sml.support.el.ElException;
import org.hw.sml.support.ioc.BeanHelper;

public class Test {
	 static class DefaultThreadFactory implements ThreadFactory {
	        static final AtomicInteger poolNumber = new AtomicInteger(1);
	        final ThreadGroup group;
	        final AtomicInteger threadNumber = new AtomicInteger(1);
	        final String namePrefix;

	        DefaultThreadFactory() {
	            SecurityManager s = System.getSecurityManager();
	            group = (s != null)? s.getThreadGroup() :
	                                 Thread.currentThread().getThreadGroup();
	            namePrefix = "pool-" +
	                          poolNumber.getAndIncrement() +
	                         "-thread-";
	        }

	        public Thread newThread(Runnable r) {
	            Thread t = new Thread(group, r,
	                                  namePrefix + threadNumber.getAndIncrement(),
	                                  0);
	            if (t.isDaemon())
	                t.setDaemon(false);
	            if (t.getPriority() != Thread.NORM_PRIORITY)
	                t.setPriority(Thread.NORM_PRIORITY);
	            return t;
	        }
	    }
	public static void main(String[] args) throws IOException, ElException {
		BeanHelper.start();
		JsEngine.evel("");
		long start=System.currentTimeMillis();
		for(int i=0;i<100;i++){
			Object result=BeanHelper.evelV("#{lo.plus(1.0,1)}");
			if(i==1)
				System.out.println(result);
		}
		System.out.println(System.currentTimeMillis()-start);
		long s=System.currentTimeMillis();
		for(int i=0;i<100;i++){
			Object result=JsEngine.evel("1+1");
			if(i==1)
				System.out.println(result);
		}
		System.out.println(System.currentTimeMillis()-s);
		
	}
}
