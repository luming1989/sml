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
	Connection connection = null;
	public void init(){
		ConnectionFactory connectionFactory;
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,mqUrl);
        try {
        	 connection = connectionFactory.createConnection();
             connection.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
        
	}
	public void send(String msg){
        Session session = null;
        Destination destination;
        MessageProducer producer;
        try {
            session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
            destination = new ActiveMQQueue(destinationName) ;
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    		producer.send(session.createTextMessage(msg));
    		session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	 try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
        }
	}
	
}
