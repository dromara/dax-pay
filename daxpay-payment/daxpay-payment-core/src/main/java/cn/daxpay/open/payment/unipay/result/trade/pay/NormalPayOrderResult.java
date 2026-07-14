package cn.daxpay.open.payment.unipay.result.trade.pay;

import cn.daxpay.open.platform.core.enums.pay.channel.*;
import cn.daxpay.open.platform.core.enums.pay.pay.*;
import cn.daxpay.open.platform.core.enums.pay.trade.*;
import cn.daxpay.open.platform.core.enums.pay.notice.*;
import cn.daxpay.open.platform.core.enums.unipay.PayLimitPayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付订单
///
@Data
@Accessors(chain = true)
@Schema(title = "支付订单")
public class NormalPayOrderResult {

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /// 平台业务单号(容器 orderNo)
    @Schema(description = "平台业务单号")
    private String orderNo;

    /// 资金交易号(tradeNo)
    @Schema(description = "资金交易号")
    private String tradeNo;

    /// 通道系统交易号
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 描述
    @Schema(description = "描述")
    private String description;

    /// 支付通道
    /// @see ChannelEnum
    @Schema(description = "支付通道")
    private String channel;

    /// 支付方式
    @Schema(description = "支付方式")
    private String method;

    /// 限制用户支付类型, 目前支持限制信用卡
    /// @see PayLimitPayEnum
    @Schema(description = "限制用户支付类型")
    private String limitPay;

    /// 金额（分，最小货币单位）
    @Schema(description = "金额(分)")
    private Long amount;

    /// 实收金额（分，最小货币单位）
    @Schema(description = "实收金额(分)")
    private Long realAmount;

    /// 可退款余额（分，最小货币单位）
    @Schema(description = "可退款余额(分)")
    private Long refundableBalance;

    /// 支付状态
    /// @see PayStatusEnum
    @Schema(description = "支付状态")
    private String status;

    /// 退款状态
    /// @see PayRefundStatusEnum
    @Schema(description = "退款状态")
    private String refundStatus;

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

