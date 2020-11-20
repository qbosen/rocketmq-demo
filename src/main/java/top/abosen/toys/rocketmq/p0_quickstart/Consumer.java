package top.abosen.toys.rocketmq.p0_quickstart;

import config.GlobalConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 1. group的作用
 * 2. 消息回溯
 * 3. 批量push
 */
public class Consumer {

    public static void main(String[] args) throws InterruptedException, MQClientException {


        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("quick_start_consumer_group");
        consumer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);

        /*如果是一个新的消费者组，那么从以下设置位置开始消费*/
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("MyTopic0", "*");


        /*push模式消费，注册回调*/
        consumer.setConsumeMessageBatchMaxSize(10);
        //noinspection Convert2Lambda
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages [%d]: %s %n", Thread.currentThread().getName(), msgs.size(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
