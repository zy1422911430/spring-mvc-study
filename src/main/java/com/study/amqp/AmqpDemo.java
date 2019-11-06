package com.study.amqp;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @className: AmqpDemo
 * @description: TODO rabbitmq一对一、一对多模式
 * @author: zyd
 * @date: 2019/10/15 14:18
 * @version: 1.0
 */
public class AmqpDemo {

    public static void main(String[] args) {
        Send send = new Send();
        send.sendMsg("hellow!");
    }

    static class Send{
        private final String QUEUE_NAME = "testAck";

        private Channel channel = null;

        public Send(){
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("122.51.44.233");
            factory.setUsername("admin");
            factory.setPassword("admin");
            try {
                Connection connection = factory.newConnection();
                channel = connection.createChannel();
                System.out.println("连接到rabbitmq");
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMsg(String msg) {
            if (channel != null) {
                try {
                    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                    String message = "Hello World!";
                    //channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    //消息持久化
                    channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Receiver{
        private final static String QUEUE_NAME = "testAck";

        public static void main(String[] argv) throws Exception {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("122.51.44.233");
            factory.setUsername("admin");
            factory.setPassword("admin");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            channel.basicQos(1);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                //手动消息确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        }
    }

}
