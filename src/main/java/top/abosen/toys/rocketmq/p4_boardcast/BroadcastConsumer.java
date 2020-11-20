package top.abosen.toys.rocketmq.p4_boardcast;

import config.GlobalConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 1. 使用quickstart启动两个集群consumer 对比两个广播consumer
 * 2. 广播模式的限制
 * <p>
 * ⼴播消费模式服务端不维护消费进度，不⽀持顺序消息、不⽀持重置消费位点、不支持console管理，不支持重投，重复消息概率增加
 * 业务方需要关注 消费失败的情况、消息幂等，
 * 客户端每⼀次启动都会从最新消息消费。客户端在被停⽌期间发送⾄服务端的消息将会被⾃动跳过。
 *
 * 每条消息都需要被相同订阅逻辑的多台机器处理。尽可能使⽤集群模式，可以通过多组集群方式。
 * <p>
 *
 * @author qiubaisen
 * @date 2020/11/20
 */
public class BroadcastConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("broadcast_consumer_group");
        consumer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 设置消费模式为广播，默认为集群消费模式
        consumer.setMessageModel(MessageModel.BROADCASTING);

        consumer.subscribe("MyTopic0", "TagA || TagC || TagD");

        //noinspection Convert2Lambda
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("Broadcast Consumer Started.%n");
        consumer.shutdown();
    }
}