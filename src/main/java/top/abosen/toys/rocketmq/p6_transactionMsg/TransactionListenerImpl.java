package top.abosen.toys.rocketmq.p6_transactionMsg;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiubaisen
 * @date 2020/11/20
 */

public class TransactionListenerImpl implements TransactionListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    /**
     * 半消息提交成功后，执行本地事务
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        int status = transactionIndex.getAndIncrement() % 3;
        System.out.printf("[%s] executeLocalTransaction: tid:[%s] state:[%d]%n", Thread.currentThread().getName(), msg.getTransactionId(), status);
        localTrans.put(msg.getTransactionId(), status);
        // 本地事务没有提交执行结果，处于pending，等待回查
        return LocalTransactionState.UNKNOW;
    }

    /**
     * 事务消息共有三种状态，提交状态、回滚状态、中间状态
     * <p>
     * 默认检查15次，之后丢弃
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Integer status = localTrans.get(msg.getTransactionId());
        System.out.printf("[%s] checkLocalTransaction: tid:[%s] state:[%d]%n", Thread.currentThread().getName(), msg.getTransactionId(), status);
        if (null != status) {
            switch (status) {
                case 0:
                    localTrans.put(msg.getTransactionId(), transactionIndex.getAndIncrement() % 3);
                    return LocalTransactionState.UNKNOW;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                default:
                    return LocalTransactionState.COMMIT_MESSAGE;
            }
        }
        // null表示是重启前消息, 不在本地状态中
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
