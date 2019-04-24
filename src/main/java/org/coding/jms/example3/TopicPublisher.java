package org.coding.jms.example3;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Topic（发布/订阅）方式  发布者Publisher
 * @author donald
 *
 */
public class TopicPublisher {
   private static String user = ActiveMQConnection.DEFAULT_USER;
   private static String password =ActiveMQConnection.DEFAULT_PASSWORD;
   private static String url =  "tcp://localhost:61616";
   private static String tname =  "testTopic";
   public static void main(String[] args)throws Exception {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
       ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user,password,url);
       // Connection ：JMS 客户端到JMS Provider 的连接
       Connection connection = connectionFactory.createConnection();
       // Connection 启动
       connection.start();
       System.out.println("Connection is start...");
       // Session： 一个发送或接收消息的线程
       Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
       // Topicr ：消息的目的地;消息发送给谁.
       Topic  destination = session.createTopic(tname);
       // MessageProducer：消息发送者
       MessageProducer producer = session.createProducer(destination);
       // 设置持久化，此处学习，实际根据项目决定
       producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        // 构造消息，此处写死，项目就是参数，或者方法获取
       sendMessage(session, producer);
       session.commit();

       connection.close();
       System.out.println("send Order ok.");
   }
   /**
    *
    * @param session
    * @param producer
    * @throws Exception
    */
   public static void sendMessage(Session session, MessageProducer producer)
           throws Exception {
       //我们也可以将Object转换为Json String，作为TextMessage来传送，在消费再反Json String 为Obejct
       MapMessage mapMessage = session.createMapMessage();
       mapMessage.setInt("Id", 1);
       mapMessage.setDouble("Amount", 150.62);
       mapMessage.setInt("GoodsId", 15);
       mapMessage.setInt("GoodsAmount", 2);
       mapMessage.setInt("ShopId", 5656);
//       ObjectMessage orderMess = session.createObjectMessage(order);
       System.out.println("向ActiveMq:"+tname+"发送订单信息：" + "ActiveMq 发送的Topic消息");
       producer.send(mapMessage);
   }

}
