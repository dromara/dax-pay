package cn.daxpay.open.payment.trade.order.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 退款订单(管理)
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款订单")
public class RefundOrderResult extends BaseResult {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称(由 mchNo 翻译, 走系统 @Trans 机制)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 退款号
    @Schema(description = "退款号")
    private String refundNo;

    /// 商户退款号
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /// 实际上送通道的商户退款关联号
    @Schema(description = "实际上送通道关联号")
    private String relationOrderNo;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 原支付资金交易号
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 原支付交易形态
    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    @Schema(description = "交易类型")
    private String tradeType;

    /// 商户业务订单号
    @Schema(description = "商户业务订单号")
    private String bizOrderNo;

    /// 通道支付订单号
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /// 通道退款流水号
    @Schema(description = "通道退款流水号")
    private String outRefundNo;

    /// 退款金额(分)
    @Schema(description = "退款金额(分)")
    private Long amount;

    /// 订单总金额(分)
    @Schema(description = "订单总金额(分)")
    private Long orderAmount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 退款原因
    @Schema(description = "退款原因")
    private String reason;

    /// 退款状态
    /// @see cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum
    @Schema(description = "退款状态")
    private String status;

    /// 退款完成时间
    @Schema(description = "退款完成时间")
    private OffsetDateTime finishTime;

    /// 支付通道
    @Schema(description = "支付通道")
    private String channel;

    /// 支付产品
    @Schema(description = "支付产品")
    private String product;

    /// 通道商户号
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 通道应用 AppId
    @Schema(description = "通道应用AppId")
    private String channelAppId;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /// 商户附加参数
    @Schema(description = "商户附加参数")
    private String attach;

    /// 客户端 IP
    @Schema(description = "客户端IP")
    private String clientIp;

    /// 门店号（继承自原支付容器，可空）
    @Schema(description = "门店号")
    private String storeNo;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;
}
