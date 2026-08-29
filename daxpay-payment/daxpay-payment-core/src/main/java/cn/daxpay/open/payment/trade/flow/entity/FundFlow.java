package cn.daxpay.open.payment.trade.flow.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.flow.convert.FundFlowConvert;
import cn.daxpay.open.payment.trade.flow.enums.FundFlowTypeEnum;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 资金流水
///
/// 收款/退款成功即落流水, 只增不改, 对账与资金报表底表。
/// 幂等: 同一支付交易最多一条收款流水, 同一退款单最多一条退款流水(部分唯一索引兜底)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_fund_flow")
public class FundFlow extends MchBaseEntity implements ToResult<FundFlowResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 流水类型(pay/refund)
    /// @see FundFlowTypeEnum
    private String flowType;

    /// 原支付交易号
    private String tradeNo;

    /// 退款单号
    private String refundNo;

    /// 商户业务单号
    private String bizOrderNo;

    /// 订单标题
    private String title;

    /// 流水金额(最小货币单位)
    private Long amount;

    /// 币种
    private String currency;

    /// 支付通道
    private String channel;

    /// 支付渠道
    private String provider;

    /// 通道商户号
    private String channelMchNo;

    /// 通道交易号
    private String outOrderNo;

    /// 资金完成时间
    private OffsetDateTime finishTime;

    @Override
    public FundFlowResult toResult() {
        return FundFlowConvert.CONVERT.toResult(this);
    }
}
