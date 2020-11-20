package top.abosen.toys.rocketmq.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class Consumer {

    @Slf4j
    @Service
    @RocketMQMessageListener(topic = "test-topic-1", consumerGroup = "my-consumer_test-topic-1")
    public static class MyConsumer1 implements RocketMQListener<String> {
        public void onMessage(String message) {
            log.info("received message: {}", message);
        }
    }

    @Service
    @Slf4j
    @RocketMQMessageListener(topic = "test-topic-2", consumerGroup = "my-consumer_test-topic-2")
    public static class MyConsumer2 implements RocketMQListener<OrderPaidEvent> {
        public void onMessage(OrderPaidEvent orderPaidEvent) {
            log.info("received orderPaidEvent: {}", orderPaidEvent);
        }
    }

    @Service
    @Slf4j
    @RocketMQMessageListener(topic = "orderly_topic", consumerGroup = "my-consumer_test-topic-3")
    public static class MyConsumer3 implements RocketMQListener<String> {
        public void onMessage(String message) {
            log.info("received orderly message: {}", message);
        }
    }

}