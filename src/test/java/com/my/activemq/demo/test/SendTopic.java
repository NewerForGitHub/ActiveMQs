package com.my.activemq.demo.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 通过客户端代码调用来发送一个topic的消息：
 *
 * @author Chaoqun Zheng
 */
public class SendTopic {
    private static final int SEND_NUMBER = 5;

    public static void sendMessage(Session session, MessageProducer producer)
            throws Exception {
        for (int i = 1; i <= SendTopic.SEND_NUMBER; i++) {
            TextMessage message = session
                .createTextMessage("ActiveMq发送的消息" + i);
            //发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }

    public static void main(String[] args) {
        // ConnectionFactory：连接工厂，JMS用它创建连接
        ConnectionFactory connectionFactory;
        // Connection：JMS客户端到JMS Provider的连接
        Connection connection = null;
        // Session：一个发送或接收消息的线程
        Session session;
        // Destination：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;
        // TextMessage message;
        //构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(
            ActiveMQConnection.DEFAULT_USER,
            ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
        try {
            //构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            //启动
            connection.start();
            //获取操作连接
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //获取session注意参数值FirstTopic是一个服务器的topic(与queue消息的发送相比，这里是唯一的不同)
//            destination = session.createTopic("FirstTopic");//发布主题消息
            destination = session.createQueue("firstQueue");//发布队列消息
            //得到消息生成者【发送者】
            producer = session.createProducer(destination);
            //设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //构造消息，此处写死，项目就是参数，或者方法获取
            SendTopic.sendMessage(session, producer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Throwable ignore) {
            }
        }
    }
}