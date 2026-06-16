package org.dromara.daxpay.payment.old.pay.bo.reconcile;

import org.dromara.daxpay.platform.core.enums.pay.trade.TradeStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 平台通用交易对象对象，用于与网关进行对账
///
@Data
@Accessors(chain = true)
public class PlatformReconcileTradeBo {

    /// 交易类型
    /// @see TradeTypeEnum
    private String tradeType;

    /// 金额
    private BigDecimal amount;

    /// 交易状态
    /// @see TradeStatusEnum
    private String tradeStatus;

    /// 平台交易号
    private String tradeNo;

    /// 商户交易号
    private String bizTradeNo;

    /// 通道交易号
    private String outTradeNo;

    /// 完成时间
    private OffsetDateTime tradeTime;
}

