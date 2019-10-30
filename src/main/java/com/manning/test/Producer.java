package com.manning.test;

//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.MessageProperties;


/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/25 16:09
 */
public class Producer {
    private static final String EXCHANGE_NAME="exchange_demo";
    private static final String ROUTING_KEY="routingkey_demo";
    private static final String QUEUE_NAME="queue_demo";
    private static final String IP_ADDRESS="116.62.5.147";
    private static final int PORT=5672;

    public static void main(String[] args) throws Exception {
//        ConnectionFactory factory=new ConnectionFactory();
//        factory.setHost(IP_ADDRESS);
//        factory.setPort(PORT);
//        factory.setUsername("root");
//        factory.setPassword("liu.manling/1028");
//        Connection connection=factory.newConnection();
//        Channel channel=connection.createChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
//        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
//        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
//        String message="Hello World!";
//        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
//        channel.close();
//        connection.close();



    }


}
