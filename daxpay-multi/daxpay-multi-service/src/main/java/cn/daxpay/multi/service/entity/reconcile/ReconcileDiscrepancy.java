package cn.daxpay.multi.service.entity.reconcile;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.service.common.entity.MchRecordEntity;
import cn.daxpay.multi.service.convert.reconcile.ReconcileConvert;
import cn.daxpay.multi.service.enums.ReconcileDiscrepancyTypeEnum;
import cn.daxpay.multi.service.result.reconcile.ReconcileDiscrepancyResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对账差异记录
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_reconcile_discrepancy")
public class ReconcileDiscrepancy extends MchRecordEntity implements ToResult<ReconcileDiscrepancyResult> {

    /** 对账单ID */
    private Long reconcileId;

    /** 对账号 */
    private String reconcileNo;

    /** 对账日期 */
    private LocalDate reconcileDate;

    /** 支付通道 */
    private String channel;

    /**
     * 差异类型
     * @see ReconcileDiscrepancyTypeEnum
      */
    private String discrepancyType;

    /* 平台侧信息 */
    /** 平台交易号 */
    private String TradeNo;

    /** 商户订单号 */
    private String bizTradeNo;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /** 交易金额 */
    private BigDecimal tradeAmount;

    /** 交易状态 */
    private String tradeStatus;

    /** 交易时间 */
    private LocalDateTime tradeTime;

    /* 通道侧信息 */

    /** 通道交易号 */
    private String channelTradeNo;

    /** 通道交易类型 */
    private String channelTradeType;

    /** 通道交易金额 */
    private BigDecimal channelTradeAmount;

    /** 通道交易状态 */
    private String channelTradeStatus;

    /** 通道交易时间 */
    private LocalDateTime channelTradeTime;

    /**
     * 转换
     */
    @Override
    public ReconcileDiscrepancyResult toResult() {
        return ReconcileConvert.CONVERT.toResult(this);
    }
}
