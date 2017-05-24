package org.hw.sml.test;
import org.hw.sml.support.queue.ManagedQuene;
import org.hw.sml.support.queue.Task;
import org.hw.sml.support.time.StopWatch;


public class TaskDemo {
	static ManagedQuene<Task> mq=new ManagedQuene<Task>();
	static StopWatch sw=new StopWatch("test");
	public static void main(String[] args) throws InterruptedException {
		
		mq.setConsumerThreadSize(1);//处理线程任务数
		mq.setTimeout(2);//超时s
		//mq.setTimeoutRunning(true);
		//mq.setSkipQueueCaseInExecute(true);
		mq.init();
		for(int i=1;i<15;i++){
			mq.add(new Task1(i));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println(sw.prettyPrint());
			}
		}));
		
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
			sw.start(toString());
			try{
			Thread.sleep(c*1000);
			System.out.println(mq.getExecutingMap());
			System.out.println(this.toString()+" 执行完成！");
			}catch(Exception e){}
			finally{
				sw.stop();
			}
		}
		
	}
}
