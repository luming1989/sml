package org.hw.sml.test.mq;

import java.io.IOException;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class IbmMq {
	//设备地址：10.78.220.191
	///管理器：Q_IPNET
	//端口：61616
	//队列：Q_IPNET_ALARM（东信）    Q_ALARM（亿阳）
	//通道：SYSTEM.DEF.SVRCONN（系统默认）
	//ccsid: 1381
	
	public static void main(String[] args) throws MQException, IOException, ClassNotFoundException, InterruptedException {
		for(int i=0;i<1;i++)
	     new Thread(new Runnable(){
	    	 
			public void run() {
				 String mqname="Q_IPNET_ALARM";
				// String mqname="Q_ALARM";
				 MQEnvironment.hostname = "10.78.220.191";// MQ服务器IP      
			     MQEnvironment.channel = "SYSTEM.DEF.SVRCONN";     // 队列管理器对应的服务器连接通道      
			     MQEnvironment.CCSID = 1383;            // 字符编码      
			     MQEnvironment.port = 61616;  
				 MQQueueManager qMgr;
				try {
					qMgr = new MQQueueManager("Q_IPNET");
					 int openOptions = MQC.MQOO_INPUT_AS_Q_DEF|MQC.MQOO_OUTPUT|MQC.MQOO_INQUIRE;    
					 //MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,MQC.TRANSPORT_MQSERIES_BINDINGS);
				     MQQueue queue = null;  
				     queue = qMgr.accessQueue(mqname, openOptions, null, null,null);     
					  while(true){
					    	 int depth = queue.getCurrentDepth(); 
					    	 System.out.println(depth);
					    	 if(depth==0){
					    		 //continue;
					    	 }
					    	 Thread.sleep(100);
						    /* MQMessage msg = new MQMessage();// 要读的队列的消息      
					         MQGetMessageOptions gmo = new MQGetMessageOptions();      
					         queue.get(msg, gmo);      
					         System.out.println("消息的大小为："+msg.getDataLength());     
					         byte[] bytes=new byte[msg.getDataLength()];
					         msg.readFully(bytes); 
					         System.out.println(new String(bytes));*/
					     }
				} catch (MQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
	    	 
	     }).start();
	   
		
	}
}
