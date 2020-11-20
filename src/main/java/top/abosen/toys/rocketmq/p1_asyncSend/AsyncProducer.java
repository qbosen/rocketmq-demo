package top.abosen.toys.rocketmq.p1_asyncSend;

import config.GlobalConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 1. 异步发送
 * 2. 消息重投
 *
 * @author qiubaisen
 * @date 2020/11/20
 */
public class AsyncProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("async_producer_group");
        producer.setNamesrvAddr(GlobalConfig.NAMESV_ADDR);
        producer.start();
        int megCount = 50;

        // 异步重试只会选择同一个broker,同步重试不会选择上次失败的broker,超过次数时进入死信队列。
        // 重试保证消息投递成功,at least once
        producer.setRetryTimesWhenSendFailed(2);
        producer.setRetryTimesWhenSendAsyncFailed(2);

        CountDownLatch countDownLatch = new CountDownLatch(megCount);
        for (int i = 0; i < megCount; i++) {
            int index = i;
            Message message = new Message("MyTopic0", "", ("hello rocketmq " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d ERROR %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }

        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }

}
