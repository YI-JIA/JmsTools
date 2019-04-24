package org.coding.jms.example2;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Queue（点对点）方式  生存者Producer
 * @author donald
 *
 */
public class QueueProducer {
   private static String user = ActiveMQConnection.DEFAULT_USER;
   private static String password =ActiveMQConnection.DEFAULT_PASSWORD;
   private static String url =  "tcp://localhost:61616";
   private static String qname =  "testQueue";

   public static void main(String[] args)throws Exception {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
       ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user,password,url);
       // Connection ：JMS 客户端到JMS Provider 的连接
       Connection connection = connectionFactory.createConnection();
       // Connection 启动
       connection.start();
       System.out.println("Connection is start...");
      //创建一个session
//第一个参数:是否支持事务，如果为true，则会忽略第二个参数，被jms服务器设置为SESSION_TRANSACTED
//第二个参数为false时，paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。
//Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
//Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会当作发送成功，并删除消息。
//DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。

       Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
       // Queue ：消息的目的地;消息发送给谁.
       Queue  destination = session.createQueue(qname);
       // MessageProducer：消息发送者
       MessageProducer producer = session.createProducer(destination);
       //设置生产者的模式，有两种可选
//DeliveryMode.PERSISTENT 当activemq关闭的时候，队列数据将会被保存
//DeliveryMode.NON_PERSISTENT 当activemq关闭的时候，队列里面的数据将会被清空
       producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        // 构造消息，此处写死，项目就是参数，或者方法获取
       sendMessage(session, producer);
       session.commit();

       connection.close();
       System.out.println("send text ok.");
   }

   public static void sendMessage(Session session, MessageProducer producer)
           throws Exception {
       for (int i = 1; i <= 5; i++) {//有限制,达到1000就不行
           TextMessage message = session.createTextMessage("向ActiveMq发送的Queue消息" + i);
           // 发送消息到目的地方
           System.out.println("发送消息：" + "ActiveMq 发送的Queue消息" + i);
           producer.send(message);
       }
   }
}