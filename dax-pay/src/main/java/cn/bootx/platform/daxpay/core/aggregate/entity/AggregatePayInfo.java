package cn.bootx.platform.daxpay.core.aggregate.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 聚合支付发起信息
 *
 * @author xxm
 * @since 2022/3/5
 */
@Data
@Accessors(chain = true)
public class AggregatePayInfo {

    /** 标题 */
    private String title;

    /** 订单ID */
    private String businessId;

    /** 支付金额 */
    private BigDecimal amount;

}
