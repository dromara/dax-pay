package cn.daxpay.open.plugin.easypay.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.plugin.easypay.convert.EasyPayRefundOrderConvert;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayRefundOrderResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 易支付协议退款订单
///
/// 独立于内核 [cn.daxpay.open.payment.trade.order.entity.RefundOrder]，记录易支付协议侧的退款信息，
/// 通过 [refundId] 关联内核退款单。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_easy_pay_refund_order")
public class EasyPayRefundOrder extends MchBaseEntity implements ToResult<EasyPayRefundOrderResult> {

    /// 关联内核退款单 ID（RefundOrder.id）
    private Long refundId;

    /// 关联易支付订单 ID（EasyPayOrder.id）
    private Long easyPayOrderId;

    /// 易支付商户号
    private Integer pid;

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 平台退款单号
    private String refundNo;

    /// 商户退款单号 out_refund_no
    private String bizRefundNo;

    /// 平台业务单号
    private String tradeNo;

    /// 商户订单号 out_trade_no
    private String outTradeNo;

    /// 退款金额（元）
    private BigDecimal money;

    /// 协议退款状态 0=失败/处理中 1=成功
    private Integer status;

    /// API 版本 v1/v2
    private String apiVersion;

    /// 退款发起时间
    private OffsetDateTime addTime;

    /// 退款完成时间
    private OffsetDateTime endTime;

    @Override
    public EasyPayRefundOrderResult toResult() {
        return EasyPayRefundOrderConvert.CONVERT.toResult(this);
    }
}
