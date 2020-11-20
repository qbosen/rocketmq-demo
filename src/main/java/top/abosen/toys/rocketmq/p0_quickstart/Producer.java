package top.abosen.toys.rocketmq.p0_quickstart;

import config.GlobalConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;


public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {


        DefaultMQProducer producer = new DefaultMQProducer("quick_start_producer_group");
        producer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        producer.start();


        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message("MyTopic0", "TagA",
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        producer.shutdown();
    }
}
