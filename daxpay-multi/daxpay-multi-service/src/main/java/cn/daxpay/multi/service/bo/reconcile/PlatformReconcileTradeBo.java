package cn.daxpay.multi.service.bo.reconcile;

import cn.daxpay.multi.core.enums.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 平台通用交易对象对象，用于与网关进行对账
 * @author xxm
 * @since 2024/3/1
 */
@Data
@Accessors(chain = true)
public class PlatformReconcileTradeBo {

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String type;

    /** 金额 */
    private BigDecimal amount;

    /** 本地交易号 */
    private String tradeNo;

    /** 通道交易号 */
    private String outTradeNo;

    /** 完成时间 */
    private LocalDateTime tradeTime;
}
