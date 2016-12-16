package org.hw.sml.test.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.hw.sml.jdbc.JdbcTemplate;

public class MqListener implements MessageListener{
	
	private JdbcTemplate jdbcTemplate;
	private String destinationName;
	private String mqUrl;
	
	ConnectionFactory connectionFactory;
    Connection connection = null;
    Session session;
    Destination destination;
    private MessageConsumer consumer=null;//初始化 消息消费者  
	public void init(){
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,mqUrl);
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
            destination = new ActiveMQQueue(destinationName) ;
            consumer=session.createConsumer(destination);
            consumer.setMessageListener(this);
            System.out.println("Consumer:->Begin listening...");
            connection.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           
        }
	}
	

	@Override
	public void onMessage(Message arg0) {
		try {
			String content=((ActiveMQTextMessage)arg0).getText();
			//System.out.println(((ActiveMQTextMessage)arg0).getText());
			jdbcTemplate.update("insert into HW_TEST(timeid) values(?)",content);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
