package top.abosen.toys.rocketmq.p1_syncSend;

import config.GlobalConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author qiubaisen
 * @date 2020/11/20
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sync_producer_group");
        producer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        producer.setRetryTimesWhenSendFailed(2);
        producer.start();

        for (int i = 0; i < 50; i++) {
            Message message = new Message("MyTopic0", "", ("hello rocketmq " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult);
        }

        producer.shutdown();
    }
}
