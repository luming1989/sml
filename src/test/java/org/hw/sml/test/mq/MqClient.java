package org.hw.sml.test.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

public class MqClient {
	
	private String destinationName;
	private String mqUrl;
	public void init(){
        
	}
	public void send(String msg){
        Session session = null;
        Destination destination;
        MessageProducer producer;
        ConnectionFactory connectionFactory;
        Connection connection=null;
        try {
        	connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,mqUrl);
        	connection = connectionFactory.createConnection();
        	 connection.start();
            session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            destination = new ActiveMQQueue(destinationName) ;
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    		producer.send(session.createTextMessage(msg));
    		session.commit();
    		session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	 try {
        		 connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
        }
	}
	
}
