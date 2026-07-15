package cn.daxpay.open.payment.trade.order.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 资金交易凭证(管理)
///
/// 列表场景仅填充凭证(PayTrade)字段;
/// 详情场景额外填充容器(NormalPayOrder)字段: bizOrderNo / title / containerStatus
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "资金交易凭证")
public class PayTradeResult extends BaseResult {

    // ===== 凭证(PayTrade)字段 =====

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 支付交易号
    @Schema(description = "支付交易号")
    private String tradeNo;

    /// 交易形态
    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    @Schema(description = "交易形态")
    private String tradeType;

    /// 关联容器ID
    @Schema(description = "关联容器ID")
    private Long containerId;

    /// 支付产品
    @Schema(description = "支付产品")
    private String product;

    /// 支付通道
    @Schema(description = "支付通道")
    private String channel;

    /// 支付方式
    @Schema(description = "支付方式")
    private String method;

    /// 通道应用 AppId（实际支付所用）
    @Schema(description = "通道应用AppId")
    private String channelAppId;

    /// 限制支付类型
    @Schema(description = "限制支付类型")
    private String limitPay;

    /// 支付渠道(微信/支付宝/银联等)
    @Schema(description = "支付渠道")
    private String provider;

    /// 本次交易金额(最小货币单位)
    @Schema(description = "本次交易金额(分)")
    private Long amount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 入账金额(最小货币单位)
    /// 结算类 SUCCESS = amount；预授权冻结(authorize)等非结算动作 = 0
    @Schema(description = "入账金额(分)")
    private Long postedAmount;

    /// 可退金额(最小货币单位)
    @Schema(description = "可退金额(分)")
    private Long refundableBalance;

    /// 资金状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @Schema(description = "资金状态")
    private String status;

    /// 过期时间
    @Schema(description = "过期时间")
    private OffsetDateTime expiredTime;

    /// 支付成功时间
    @Schema(description = "支付成功时间")
    private OffsetDateTime payTime;

    /// 关闭时间
    @Schema(description = "关闭时间")
    private OffsetDateTime closeTime;

    /// 订单来源
    @Schema(description = "订单来源")
    private String source;

    /// 通道商户号(冗余自业务容器)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 门店号(冗余自业务容器, 可空)
    @Schema(description = "门店号")
    private String storeNo;

    /// 通道订单号
    @Schema(description = "通道订单号")
    private String outOrderNo;

    /// 透传订单号
    @Schema(description = "透传订单号")
    private String transOrderNo;

    /// 特殊通道关联订单号
    @Schema(description = "特殊通道关联订单号")
    private String relationOrderNo;

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

    /// 付款码(被扫支付)
    @Schema(description = "付款码")
    private String authCode;

    /// 活动类型
    @Schema(description = "活动类型")
    private String promotionType;

    /// 支付参数体
    @Schema(description = "支付参数体")
    private String payBody;

    /// 支付参数体类型
    @Schema(description = "支付参数体类型")
    private String payBodyType;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;

    // ===== 容器(NormalPayOrder)联表字段, 仅详情时填充 =====

    /// 商户业务单号
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// 订单标题
    @Schema(description = "订单标题")
    private String title;

    /// 容器业务状态
    /// @see cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum
    @Schema(description = "容器业务状态")
    private String containerStatus;
}
