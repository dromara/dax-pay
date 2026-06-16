package org.dromara.daxpay.payment.old.pay.bo.reconcile;

import org.dromara.daxpay.platform.core.enums.pay.trade.TradeStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 支付通道交易对账单
///
@Data
@Accessors(chain = true)
public class ChannelReconcileTradeBo {

    /// 交易类型
    /// @see TradeTypeEnum
    private String tradeType;

    /// 交易状态
    /// @see TradeStatusEnum
    private String tradeStatus;

    /// 本地平台交易号
    private String platformTradeNo;

    /// 通道交易号
    private String channelTradeNo;

    /// 交易金额
    private BigDecimal amount;

    /// 交易时间
    private OffsetDateTime tradeTime;
}

