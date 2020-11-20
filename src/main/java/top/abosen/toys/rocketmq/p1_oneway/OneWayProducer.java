package top.abosen.toys.rocketmq.p1_oneway;

import config.GlobalConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author qiubaisen
 * @date 2020/11/20
 */
public class OneWayProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("oneway_producer_group");
        producer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        producer.start();
        for (int i = 0; i < 50; i++) {
            Message message = new Message("MyTopic0", "oneway", ("hello rocketmq " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(message);
        }
        producer.shutdown();
    }
}
