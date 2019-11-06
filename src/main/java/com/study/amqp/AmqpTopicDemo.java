package com.study.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @className: AmqpTopicDemo
 * @description: TODO
 * @author: zyd
 * @date: 2019/10/17 16:07
 * @version: 1.0
 */
public class AmqpTopicDemo {

    public static void main(String[] args) throws InterruptedException {
        Send send = new Send();
        for (int i = 0; i < 10 ; i++) {
            send.sendMsg("hellow!");
            Thread.sleep(1000);
        }
    }

    static class Send{
        private final String EXCHANGE_NAME = "topic_logs";

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
                    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
                    String[] routingKeys = new String[]{"fast.orange.dog","lazy.red.rabbit","lazy.yellow.fox"};
                    for (String routingKey : routingKeys) {
                        message += routingKey;
                        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                        System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Receiver{
        public static void main(String[] argv) throws Exception {
            String EXCHANGE_NAME = "topic_logs";
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("122.51.44.233");
            factory.setUsername("admin");
            factory.setPassword("admin");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue,EXCHANGE_NAME,"*.orange.*");
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });

//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        }
    }

    static class Receiver1{
        public static void main(String[] argv) throws Exception {
            String EXCHANGE_NAME = "topic_logs";
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("122.51.44.233");
            factory.setPort(5673);
            factory.setUsername("admin");
            factory.setPassword("admin");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue,EXCHANGE_NAME,"lazy.#");
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });

//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        }
    }
}
