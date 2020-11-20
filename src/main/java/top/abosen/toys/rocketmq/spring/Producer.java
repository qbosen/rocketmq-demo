package top.abosen.toys.rocketmq.spring;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author qiubaisen
 * @date 2020/11/20
 */

@Component
public class Producer implements ApplicationRunner {
    @Autowired
    @Qualifier("myTemplate")
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (int i = 0; i < 20; i++) {
            //send message synchronously
            rocketMQTemplate.convertAndSend("test-topic-1", "Hello, World!");
            //send spring message
            rocketMQTemplate.send("test-topic-1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
            //send messgae asynchronously
            rocketMQTemplate.asyncSend("test-topic-2", new OrderPaidEvent("T_001", new BigDecimal("88.00")), new SendCallback() {
                @Override
                public void onSuccess(SendResult var1) {
                    System.out.printf("async onSucess SendResult=%s %n", var1);
                }

                @Override
                public void onException(Throwable var1) {
                    System.out.printf("async onException Throwable=%s %n", var1);
                }

            });
            //Send messages orderly
            rocketMQTemplate.syncSendOrderly("orderly_topic", MessageBuilder.withPayload("Hello, World").build(), "hashkey");
            //rocketMQTemplate.destroy(); // notes:  once rocketMQTemplate be destroyed, you can not send any message again with this rocketMQTemplate
            Thread.sleep(1000);
        }

    }
}
