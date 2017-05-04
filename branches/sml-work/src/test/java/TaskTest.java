import com.eastcom_sw.inas.core.service.support.queue.ManagedQuene;
import com.eastcom_sw.inas.core.service.support.queue.Task;


public class TaskTest extends ManagedQuene{
	public static void main(String[] args) throws InterruptedException {
		TaskTest mq=new TaskTest();
		mq.setConsumerThreadSize(3);
		mq.setTimeout(10);//s
		mq.init();
		for(int i=0;i<100;i++){
			mq.add(new Task1());
			Thread.sleep(10);
		}
	}
	static int count=0;
	static class Task1 implements Task{
		public void execute() throws Exception {
			System.out.println(count++);
			Thread.sleep(200000);
		}
		
	}
}
