package cn.daxpay.multi.service.entity.record.flow;

import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeFlowTypeEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 交易流水记录
 * @author xxm
 * @since 2024/6/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TradeFlowRecord extends MchEntity {

    /** 订单标题 */
    private String title;

    /** 金额 */
    private BigDecimal amount;

    /**
     * 业务类型
     * @see TradeFlowTypeEnum
     */
    private String type;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /** 本地交易号 */
    private String tradeNo;

    /** 商户交易号 */
    private String bizTradeNo;

    /** 通道交易号 */
    private String outTradeNo;

}
