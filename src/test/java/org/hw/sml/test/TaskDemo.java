package org.hw.sml.test;
import org.hw.sml.support.queue.ManagedQuene;
import org.hw.sml.support.queue.Task;


public class TaskDemo {
	public static void main(String[] args) {
		ManagedQuene mq=new ManagedQuene();
		mq.setConsumerThreadSize(3);//处理线程任务数
		mq.setTimeout(10);//超时s
		mq.init();
		for(int i=0;i<10;i++){
			mq.add(new Task1());
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
		}
	}
	 static int count=0;
	 static class Task1 implements Task{
		 public String toString(){
			 return count+"";
		 }
		public synchronized void execute() throws Exception {
			Thread.sleep(count*1000);
			System.out.println(count++);
		}
		
	}
}
