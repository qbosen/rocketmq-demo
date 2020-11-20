package top.abosen.toys.rocketmq.p5_delayMsg;

import config.GlobalConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * 延时等级由具体的broker配置决定 messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
 * 定时消息会暂存在名为SCHEDULE_TOPIC_XXXX的topic中
 * 有多少个等级，就有多少个对应的队列
 *
 * @author qiubaisen
 * @date 2020/11/20
 */
public class DelayProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("delay_producer_group");
        producer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        producer.start();
        int totalMessagesToSend = 10;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("MyTopic2", ("Hello scheduled message " + i).getBytes());
            // 设置延时等级2,这个消息将在5s之后发送,
            message.setDelayTimeLevel(2);
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult);

        }
        // 关闭生产者
        producer.shutdown();
    }
}
