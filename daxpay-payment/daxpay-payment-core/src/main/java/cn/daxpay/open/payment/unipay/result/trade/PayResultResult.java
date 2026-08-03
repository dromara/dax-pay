package cn.daxpay.open.payment.unipay.result.trade;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付结果查询结果
///
/// 平台 H5 结果页(/pay-result/{tradeNo})调用的无签名查询接口返回值。
/// 凭 tradeNo 跨租户反查资金交易 + 容器, 返回订单状态/摘要 + (支付成功时)带签名的跳转地址。
///
/// ## 字段说明
/// - [PayResultResult#finalState]: 是否终态(paid/failed/closed/expired), 前端据此决定轮询或展示结果
/// - [PayResultResult#redirectUrl]: 仅支付成功(paid) + 有商户 returnUrl 时生成, 含平台签名,
///   前端直接 `location.href` 跳转, 无需自行拼接参数或签名
@Data
@Accessors(chain = true)
@Schema(title = "支付结果查询")
public class PayResultResult {

    /// 资金交易号
    @Schema(description = "资金交易号")
    private String tradeNo;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    private String orderNo;

    /// 商户业务单号
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// 订单状态(沿用容器状态: wait_pay/paying/paid/failed/closed/expired)
    @Schema(description = "订单状态")
    private String status;

    /// 订单标题
    @Schema(description = "订单标题")
    private String title;

    /// 订单金额(最小货币单位, 分)
    @Schema(description = "订单金额(分)")
    private Long amount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 商户同步跳转地址(容器 returnUrl)
    @Schema(description = "商户跳转地址")
    private String returnUrl;

    /// 支付渠道(供前端品牌色, 见 PayProviderEnum)
    @Schema(description = "支付渠道")
    private String provider;

    /// 是否终态(paid/failed/closed/expired)
    @Schema(description = "是否终态")
    private boolean finalState;

    /// 带签名的跳转URL(仅支付成功 paid + 有 returnUrl 时生成, 含平台签名)
    @Schema(description = "带签名的跳转地址")
    private String redirectUrl;
}
