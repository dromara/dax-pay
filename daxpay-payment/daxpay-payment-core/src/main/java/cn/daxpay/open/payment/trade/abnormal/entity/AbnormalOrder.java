package cn.daxpay.open.payment.trade.abnormal.entity;

import cn.daxpay.open.payment.trade.abnormal.convert.AbnormalOrderConvert;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.trade.abnormal.enums.AbnormalHandleStatusEnum;
import cn.daxpay.open.payment.trade.abnormal.enums.AbnormalOrderTypeEnum;
import cn.daxpay.open.payment.trade.abnormal.enums.AbnormalSourceEnum;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 异常订单
///
/// 终态订单(FAIL/CLOSE/CANCEL)收到通道收款证据后的人工处置台账。
/// 终态不自动翻转(2026-08-29 决策): 由运营核实通道后人工确认成功或忽略,
/// 同一交易同时最多一条待处理记录(部分唯一索引兜底)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_abnormal_order")
public class AbnormalOrder extends MchBaseEntity implements ToResult<AbnormalOrderResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 平台交易号
    private String tradeNo;

    /// 商户业务单号
    private String bizOrderNo;

    /// 交易形态(normal/gateway)
    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    private String tradeType;

    /// 订单标题
    private String title;

    /// 交易金额(最小货币单位)
    private Long amount;

    /// 币种
    private String currency;

    /// 发现异常时的资金状态(close/fail/cancel)
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    private String tradeStatus;

    /// 异常类型
    /// @see AbnormalOrderTypeEnum
    private String abnormalType;

    /// 发现来源
    /// @see AbnormalSourceEnum
    private String source;

    /// 支付通道
    private String channel;

    /// 支付渠道
    private String provider;

    /// 通道商户号
    private String channelMchNo;

    /// 通道交易号
    private String outOrderNo;

    /// 通道侧订单状态
    private String channelStatus;

    /// 通道回调报文快照
    private String callbackNotifyInfo;

    /// 处理状态
    /// @see AbnormalHandleStatusEnum
    private String handleStatus;

    /// 处置动作(confirm_success/ignore)
    private String handleAction;

    /// 处理人账号
    private String handler;

    /// 处置时间
    private OffsetDateTime handleTime;

    /// 处置备注
    private String handleRemark;

    @Override
    public AbnormalOrderResult toResult() {
        return AbnormalOrderConvert.CONVERT.toResult(this);
    }
}
