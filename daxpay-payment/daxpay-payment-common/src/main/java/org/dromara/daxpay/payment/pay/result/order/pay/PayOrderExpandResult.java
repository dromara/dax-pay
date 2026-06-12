package org.dromara.daxpay.payment.pay.result.order.pay;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// # 支付订单扩展信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付订单扩展信息")
public class PayOrderExpandResult extends BaseResult {

    @Schema(description = "同步跳转地址")
    private String returnUrl;

    @Schema(description = "异步通知地址")
    private String notifyUrl;

    @Schema(description = "通道附加参数")
    private String extraParam;

    @Schema(description = "商户扩展参数")
    private String attach;

    @Schema(description = "请求时间")
    private LocalDateTime reqTime;

    @Schema(description = "实收金额")
    private BigDecimal realAmount;

    @Schema(description = "终端设备编码")
    private String terminalNo;

    @Schema(description = "支付终端IP")
    private String clientIp;

    /// 付款用户ID
    @Schema(description = "付款用户ID")
    private String buyerId;

    /// 用户标识
    @Schema(description = "用户标识")
    private String userId;

    /// 支付产品
    /// 三方通道所使用的支付产品和类型
    @Schema(description = "支付产品")
    private String tradeProduct;

    /// 交易方式
    @Schema(description = "交易方式")
    private String tradeWay;

    /// 银行卡类型
    /// 借记卡/贷记卡
    @Schema(description = "银行卡类型")
    private String bankType;

    /// 透传订单号
    /// 三方通道使用微信/支付宝/银联支付时产生的订单号
    @Schema(description = "透传订单号")
    private String transOrderNo;

    /// 参加活动类型
    @Schema(description = "参加活动类型")
    private String promotionType;

    /// 付款码
    @Schema(description = "付款码")
    private String barCode;

    @Schema(description = "jsapi支付时的OpenId")
    private String jsapiOpenId;

    /// 支付通道返回支付参数
    @Schema(description = "支付通道返回支付参数")
    private String payBody;

    /// 支付参数类型
    @Schema(description = "支付参数类型")
    private String payBodyType;

    /// 扩展参数存储字段
    @Schema(description = "扩展参数存储字段")
    private String ext;
}

