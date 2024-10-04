package org.dromara.daxpay.service.entity.reconcile;

import org.dromara.daxpay.core.enums.TradeStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppRecordEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 通道对账交易明细, 通过解析通道对账文件获得,
 * @author xxm
 * @since 2024/8/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_reconcile_trade")
public class ChannelReconcileTrade extends MchAppRecordEntity {

    /** 关联对账单ID */
    private Long reconcileId;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /** 平台交易号 */
    private String outTradeNo;

    /** 通道交易号 */
    private String tradeNo;

    /** 交易金额 */
    private BigDecimal amount;

    /**
     * 交易状态
     * @see TradeStatusEnum
     */
    private String tradeStatus;

    /** 交易时间 */
    private LocalDateTime tradeTime;
}
