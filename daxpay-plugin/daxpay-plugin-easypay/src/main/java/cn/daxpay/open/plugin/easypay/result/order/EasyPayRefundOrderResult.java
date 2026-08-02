package cn.daxpay.open.plugin.easypay.result.order;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 易支付协议退款订单结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "易支付退款订单结果")
public class EasyPayRefundOrderResult extends MchBaseResult {

    /// 关联内核退款单 ID（RefundOrder.id）
    @Schema(description = "关联内核退款单ID")
    private Long refundId;

    /// 关联易支付订单 ID（EasyPayOrder.id）
    @Schema(description = "关联易支付订单ID")
    private Long easyPayOrderId;

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 平台退款单号
    @Schema(description = "平台退款单号")
    private String refundNo;

    /// 商户退款单号
    @Schema(description = "商户退款单号")
    private String bizRefundNo;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /// 退款金额（元）
    @Schema(description = "退款金额（元）")
    private BigDecimal money;

    /// 协议退款状态 0=失败/处理中 1=成功
    @Schema(description = "协议退款状态 0=失败/处理中 1=成功")
    private Integer status;

    /// API 版本 v1/v2
    @Schema(description = "API版本")
    private String apiVersion;

    /// 退款发起时间
    @Schema(description = "退款发起时间")
    private OffsetDateTime addTime;

    /// 退款完成时间
    @Schema(description = "退款完成时间")
    private OffsetDateTime endTime;
}
