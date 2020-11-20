package top.abosen.toys.rocketmq.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaidEvent implements Serializable {
    private String orderId;
    private BigDecimal paidMoney;
}