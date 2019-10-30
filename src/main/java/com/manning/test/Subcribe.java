package com.manning.test;

/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/25 16:29
 */



import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 */
public class Subcribe {
    private static final String QUEUE_NAME="queue_demo";
    private static final String IP_ADDRESS="116.62.5.147";
    private static final int PORT=5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
    //   Address []addresses=new Address[]{new Address(IP_ADDRESS,PORT)};
//        ConnectionFactory factory=new ConnectionFactory();
//        factory.setUsername("root");
//        factory.setPassword("liu.manling/1028");
//        factory.setHost(IP_ADDRESS);
//        factory.setPort(PORT);
//        Connection connection=factory.newConnection();
//        final Channel channel=connection.createChannel();
//        channel.basicQos(1); //一次发送的条数,在没有收到确认机制的返回前,将不再发送信息
//        Consumer consumer=new DefaultConsumer(channel){
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                String message="message:"+new String(body);
//                System.out.println(message);
//                System.out.println(envelope.getDeliveryTag()+"===========");
//                System.out.printf("in consumer A (delivery tag is %d): %s\n", envelope.getDeliveryTag(), message);
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//         channel.basicAck(envelope.getDeliveryTag(),false); //确认机制,
//            }
//        };
//    channel.basicConsume(QUEUE_NAME,consumer);
//        TimeUnit.SECONDS.sleep(10);
//       channel.close();
//       connection.close();
 }


}
