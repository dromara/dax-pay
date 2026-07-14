package cn.daxpay.open.payment.trade.order.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 普通支付业务单(管理)
///
/// 列表场景仅填充容器(NormalPayOrder)字段;
/// 详情场景额外填充资金凭证(PayTrade)字段: tradeNo / outOrderNo / fundStatus 等
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "普通支付业务单")
public class NormalPayOrderResult extends BaseResult {

    // ===== 容器(业务单)字段 =====

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    private String orderNo;

    /// 商户业务单号
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 描述
    @Schema(description = "描述")
    private String description;

    /// 业务状态
    /// @see cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum
    @Schema(description = "业务状态")
    private String status;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /// 同步跳转地址
    @Schema(description = "同步跳转地址")
    private String returnUrl;

    /// 商户附加参数
    @Schema(description = "商户附加参数")
    private String attach;

    /// 业务单过期时间
    @Schema(description = "业务单过期时间")
    private OffsetDateTime expiredTime;

    /// 业务单金额(最小货币单位)
    @Schema(description = "业务单金额(分)")
    private Long amount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 支付通道
    @Schema(description = "支付通道")
    private String channel;

    /// 支付方式
    @Schema(description = "支付方式")
    private String method;

    /// 支付产品
    @Schema(description = "支付产品")
    private String product;

    /// 支付成功时间
    @Schema(description = "支付成功时间")
    private OffsetDateTime payTime;

    /// 关闭时间
    @Schema(description = "关闭时间")
    private OffsetDateTime closeTime;

    /// 通道商户号(路由回填)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 支付能力编码(路由回填)
    @Schema(description = "支付能力编码")
    private String capability;

    /// 通道应用 AppId（实际支付所用）
    @Schema(description = "通道应用AppId")
    private String channelAppId;

    /// 客户端IP
    @Schema(description = "客户端IP")
    private String clientIp;

    /// 终端设备编码
    @Schema(description = "终端设备编码")
    private String terminalNo;

    // ===== 资金凭证(PayTrade)联表字段, 仅详情时填充 =====

    /// 资金交易号
    @Schema(description = "资金交易号")
    private String tradeNo;

    /// 通道订单号
    @Schema(description = "通道订单号")
    private String outOrderNo;

    /// 资金状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @Schema(description = "资金状态")
    private String fundStatus;

    /// 可退金额(最小货币单位)
    @Schema(description = "可退金额(分)")
    private Long refundableBalance;

    /// 支付参数体
    @Schema(description = "支付参数体")
    private String payBody;

    /// 支付参数体类型
    @Schema(description = "支付参数体类型")
    private String payBodyType;

    /// 付款用户ID
    @Schema(description = "付款用户ID")
    private String buyerId;

    /// 微信openid
    @Schema(description = "微信openid")
    private String openid;

    /// 通道方记录的支付产品
    @Schema(description = "通道方记录的支付产品")
    private String tradeProduct;

    /// 通道方记录的交易方式
    @Schema(description = "通道方记录的交易方式")
    private String tradeWay;

    /// 银行卡类型
    @Schema(description = "银行卡类型")
    private String bankType;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;
}
