package top.abosen.toys.rocketmq.p3_batchMsg;

import config.GlobalConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量发送消息，需要满足如下几个条件
 * 1. 总大小不超过4MB
 * 2. 相同的topic
 * 3. 相同的waitStoreMsgOK
 * 4. 不能是延迟消息
 * @author qiubaisen
 * @date 2020/11/20
 */
public class BatchProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("batch_producer_group");
        producer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        producer.start();

        String topic = "MyTopic0";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes()));

        producer.send(messages);

        producer.shutdown();
    }
}
