package org.dromara.daxpay.payment.pay.entity.reconcile;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppRecordEntity;
import org.dromara.daxpay.payment.pay.convert.reconcile.ReconcileConvert;
import org.dromara.daxpay.platform.core.enums.pay.reconcile.ReconcileDiscrepancyTypeEnum;
import org.dromara.daxpay.payment.pay.result.reconcile.ReconcileDiscrepancyResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/// # 对账差异记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_reconcile_discrepancy")
public class ReconcileDiscrepancy extends MchAppRecordEntity implements ToResult<ReconcileDiscrepancyResult> {

    /// 对账单ID
    private Long reconcileId;

    /// 对账号
    private String reconcileNo;

    /// 对账日期
    private LocalDate reconcileDate;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 支付通道
    private String channel;

    /// 差异类型
    /// @see ReconcileDiscrepancyTypeEnum
    private String discrepancyType;

    /* 平台侧信息 */
    /// 平台交易号
    private String tradeNo;

    /// 商户交易号
    private String bizTradeNo;

    /// 平台关联通道交易号
    private String outTradeNo;

    /// 交易类型
    /// @see TradeTypeEnum
    private String tradeType;

    /// 交易金额
    private BigDecimal tradeAmount;

    /// 交易状态
    private String tradeStatus;

    /// 交易时间
    private OffsetDateTime tradeTime;

    /* 通道侧信息 */

    /// 通道交易号
    private String channelTradeNo;

    /// 通道关联平台交易号
    private String channelOutTradeNo;

    /// 通道交易类型
    private String channelTradeType;

    /// 通道交易金额
    private BigDecimal channelTradeAmount;

    /// 通道交易状态
    private String channelTradeStatus;

    /// 通道交易时间
    private OffsetDateTime channelTradeTime;

    /// 转换
    @Override
    public ReconcileDiscrepancyResult toResult() {
        return ReconcileConvert.CONVERT.toResult(this);
    }
}

