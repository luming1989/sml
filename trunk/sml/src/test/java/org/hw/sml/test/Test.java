package org.hw.sml.test;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.hw.sml.context.SmlContextUtils;

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
	public static void main(String[] args) throws IOException {
		ExecutorService es=Executors.newFixedThreadPool(10);
		for(int i=0;i<1000;i++){
			es.execute(new Runnable() {
				public void run() {
					try {
						String result=SmlContextUtils.queryFromUrl("http://localhost:10010/sml/cache", "");
						System.out.println(Thread.currentThread().getName()+"---->"+result.length());
						Thread.sleep(1000);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		es.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return 1;
			}
		});
		
	}
}
