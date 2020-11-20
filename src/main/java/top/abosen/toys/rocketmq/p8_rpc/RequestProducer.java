
package top.abosen.toys.rocketmq.p8_rpc;

import config.GlobalConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class RequestProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        String producerGroup = "request_producer_group";
        String topic = "RequestTopic";
        long ttl = 6000;

        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        producer.start();

        try {
            Message msg = new Message(topic,
                    "",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));

            long begin = System.currentTimeMillis();
            Message retMsg = producer.request(msg, ttl);
            long cost = System.currentTimeMillis() - begin;
            System.out.printf("request to <%s> cost: %d replyMessage: %s %n", topic, cost, retMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        producer.shutdown();
    }
}
