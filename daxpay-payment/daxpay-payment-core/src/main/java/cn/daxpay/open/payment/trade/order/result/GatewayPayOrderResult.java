package cn.daxpay.open.payment.trade.order.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 网关支付业务单(管理)
///
/// 列表场景仅填充容器(GatewayPayOrder)字段;
/// 详情场景额外填充资金凭证(PayTrade)字段: tradeNo / outOrderNo / fundStatus 等。
/// 字段契约与 [NormalPayOrderResult] 对齐, 另含网关独有 gatewayType / clientEnv / device。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "网关支付业务单")
public class GatewayPayOrderResult extends BaseResult {

    // ===== 容器(业务单)字段 =====

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "平台网关单号")
    private String orderNo;

    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// @see cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum
    @Schema(description = "网关类型")
    private String gatewayType;

    /// @see cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum
    @Schema(description = "订单来源")
    private String source;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    /// @see cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum
    @Schema(description = "业务状态")
    private String status;

    @Schema(description = "异步通知地址")
    private String notifyUrl;

    @Schema(description = "同步跳转地址")
    private String returnUrl;

    @Schema(description = "商户附加参数")
    private String attach;

    @Schema(description = "过期时间")
    private OffsetDateTime expiredTime;

    @Schema(description = "金额(分)")
    private Long amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "支付方式")
    private String method;

    @Schema(description = "支付产品")
    private String product;

    @Schema(description = "限制支付类型")
    private String limitPay;

    /// @see cn.daxpay.open.payment.merchant.enums.ClientEnvEnum
    @Schema(description = "客户端环境(UA/宿主识别)")
    private String clientEnv;

    @Schema(description = "设备(mobile/pc)")
    private String device;

    @Schema(description = "支付成功时间")
    private OffsetDateTime payTime;

    @Schema(description = "关闭时间")
    private OffsetDateTime closeTime;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "支付能力编码")
    private String capability;

    @Schema(description = "通道应用AppId")
    private String channelAppId;

    @Schema(description = "客户端IP")
    private String clientIp;

    /// 门店号（线下经营归属，可空）
    @Schema(description = "门店号")
    private String storeNo;

    @Schema(description = "通道附加参数")
    private String extraParam;

    // ===== 资金凭证(PayTrade)联表字段, 仅详情时填充 =====

    @Schema(description = "资金交易号")
    private String tradeNo;

    @Schema(description = "通道订单号")
    private String outOrderNo;

    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @Schema(description = "资金状态")
    private String fundStatus;

    @Schema(description = "可退金额(分)")
    private Long refundableBalance;

    @Schema(description = "支付参数体")
    private String payBody;

    @Schema(description = "支付参数体类型")
    private String payBodyType;

    @Schema(description = "付款用户ID")
    private String buyerId;

    @Schema(description = "微信openid")
    private String openid;

    @Schema(description = "支付渠道(厂商)")
    private String provider;

    @Schema(description = "通道方记录的支付产品")
    private String tradeProduct;

    @Schema(description = "通道方记录的交易方式")
    private String tradeWay;

    @Schema(description = "银行卡类型")
    private String bankType;

    @Schema(description = "活动类型")
    private String promotionType;

    @Schema(description = "透传订单号")
    private String transOrderNo;

    @Schema(description = "实际上送通道的商户订单号")
    private String relationOrderNo;

    @Schema(description = "错误信息")
    private String errorMsg;
}
