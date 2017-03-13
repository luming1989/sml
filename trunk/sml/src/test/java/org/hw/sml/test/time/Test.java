package org.hw.sml.test.time;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.hw.sml.support.time.Scheduler;
import org.hw.sml.tools.DateTools;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*ExecutorService executorService=Executors.newScheduledThreadPool(1000);
		for(int i=0;i<1200;i++){
			final String t=i+"";
			executorService.execute(new Runnable(){
			public void run() {
				try {
					Thread.sleep(100*Integer.parseInt(t));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("thread:"+Thread.currentThread().getName()+"|"+Thread.activeCount()+"--->"+t);
			}});
		}
		System.out.println("1");
		executorService.shutdown();
		System.out.println(2);*/
		for(int i=0;i<10000;i++){
			final String t=i+"";
			new Thread(new Runnable(){
				public void run() {
					try {
						Thread.sleep(10000+Integer.parseInt(t));
						System.out.println(Thread.activeCount());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}){{setName("helloworld"+t);}}.start();
		}
		
	}

}
