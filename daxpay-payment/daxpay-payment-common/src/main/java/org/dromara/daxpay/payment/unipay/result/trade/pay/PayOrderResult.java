package org.dromara.daxpay.payment.unipay.result.trade.pay;

import org.dromara.daxpay.platform.core.enums.pay.channel.*;
import org.dromara.daxpay.platform.core.enums.pay.pay.*;
import org.dromara.daxpay.platform.core.enums.pay.refund.*;
import org.dromara.daxpay.platform.core.enums.pay.transfer.*;
import org.dromara.daxpay.platform.core.enums.pay.trade.*;
import org.dromara.daxpay.platform.core.enums.pay.reconcile.*;
import org.dromara.daxpay.platform.core.enums.pay.notice.*;
import org.dromara.daxpay.platform.core.enums.unipay.PayLimitPayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 支付订单
///
@Data
@Accessors(chain = true)
@Schema(title = "支付订单")
public class PayOrderResult {

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    @Schema(description = "支付订单号")
    private String orderNo;

    /// 通道系统交易号
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 描述
    @Schema(description = "描述")
    private String description;

    /// 是否支持分账
    @Schema(description = "是否需要分账")
    private Boolean allocation;

    /// 是否开启自动分账, 不传输为不开启
    @Schema(description = "是否开启自动分账")
    private Boolean autoAllocation;

    /// 支付通道
    /// @see ChannelEnum
    @Schema(description = "支付通道")
    private String channel;

    /// 支付方式
    @Schema(description = "支付方式")
    private String method;

    /// 其他支付方式, 只有在 支付方式编码(method) 为 其他支付(other)时才会生效
    /// 用于处理各种通道各自定义的支付方式
    @Schema(description = "其他支付方式")
    private String otherMethod;

    /// 限制用户支付类型, 目前支持限制信用卡
    /// @see PayLimitPayEnum
    @Schema(description = "限制用户支付类型")
    private String limitPay;

    /// 金额
    @Schema(description = "金额")
    private BigDecimal amount;

    /// 实收金额
    @Schema(description = "实收金额")
    private BigDecimal realAmount;

    /// 可退款余额
    @Schema(description = "可退款余额")
    private BigDecimal refundableBalance;

    /// 支付状态
    /// @see PayStatusEnum
    @Schema(description = "支付状态")
    private String status;

    /// 退款状态
    /// @see PayRefundStatusEnum
    @Schema(description = "退款状态")
    private String refundStatus;

    /// 分账状态
    /// @see PayAllocStatusEnum
    @Schema(description = "分账状态")
    private String allocStatus;

    /// 结算状态
    /// @see SettleStatusEnum
    @Schema(description = "结算状态")
    private String settleStatus;

    /// 支付渠道 微信/支付宝/银联
    /// @see PayProviderEnum
    @Schema(description = "支付渠道")
    private String provider;

    /// 支付时间
    @Schema(description = "支付时间(UTC)")
    private OffsetDateTime payTime;

    /// 关闭时间
    @Schema(description = "关闭时间(UTC)")
    private OffsetDateTime closeTime;

    /// 过期时间
    @Schema(description = "过期时间(UTC)")
    private OffsetDateTime expiredTime;

    /// 终端设备编码
    @Schema(description = "终端设备编码")
    private String terminalNo;

    /// 付款用户ID
    @Schema(description = "付款用户ID")
    private String buyerId;

    /// 商户扩展参数,回调时会原样返回
    @Schema(description = "商户扩展参数")
    private String attach;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;
}

