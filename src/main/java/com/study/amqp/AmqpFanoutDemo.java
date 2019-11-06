package com.study.amqp;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @className: AmqpFanoutDemo
 * @description: TODO rabbitmq发布订阅模式
 * @author: zyd
 * @date: 2019/10/15 17:18
 * @version: 1.0
 */
public class AmqpFanoutDemo {

    public static void main(String[] args) throws InterruptedException {
        Send send = new Send();
        for (int i = 0; i < 10 ; i++) {
            send.sendMsg("hellow!");
            Thread.sleep(1000);
        }
    }

    static class Send{
        private final String QUEUE_NAME = "";
        private final String EXCHANGE_NAME = "logs";

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
                    String message = "Hello World!";
                    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                    channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + message + "'");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Receiver{
        public static void main(String[] argv) throws Exception {
            String EXCHANGE_NAME = "logs";
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("122.51.44.233");
            factory.setUsername("admin");
            factory.setPassword("admin");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue,EXCHANGE_NAME,"");
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });

        }
    }
}
