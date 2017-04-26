package org.hw.sml.test.time;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		final ExecutorService executorService=Executors.newScheduledThreadPool(1);
		executorService.awaitTermination(1,TimeUnit.SECONDS);
		for(int i=0;i<4;i++){
			final String t=i+"";
			executorService.execute(new Runnable(){
			public void run() {
				try {
					Thread.sleep(1000*Integer.parseInt(t));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("thread:"+Thread.currentThread().getName()+"|"+Thread.activeCount()+"--->"+t);
			}});
		}
		System.out.println("1");
		Thread.sleep(1000);
		new Thread(new Runnable() {
			public void run() {
				//executorService.awaitTermination(timeout, unit)
			}
		}).start();
		//executorService.shutdown();
		System.out.println(2);
		
	}

}
