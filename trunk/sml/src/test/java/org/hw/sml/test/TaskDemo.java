package org.hw.sml.test;
import org.hw.sml.support.queue.ManagedQuene;
import org.hw.sml.support.queue.Task;


public class TaskDemo {
	public static void main(String[] args) {
		ManagedQuene mq=new ManagedQuene();
		mq.setConsumerThreadSize(3);//处理线程任务数
		mq.setTimeout(10);//超时s
		mq.init();
		for(int i=1;i<15;i++){
			mq.add(new Task1(i));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
		}
	}
	 static class Task1 implements Task{
		 private int c;
		 public Task1(int c){
			 this.c=c;
		 }
		 public String toString(){
			 return "任务-#("+c+")";
		 }
		public synchronized void execute() throws Exception {
			Thread.sleep(c*1000);
			System.out.println(this.toString()+" 执行完成！");
		}
		
	}
}
