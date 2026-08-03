package cn.daxpay.open.payment.unipay.result.trade;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付结果同步跳转参数(带平台签名)
///
/// 支付成功后, 平台 H5 结果页跳转商户 returnUrl 时, 将订单关键信息封装为本对象,
/// 拼接为 query string 附加到商户 returnUrl, 并用平台私钥签名(sign), 商户可用平台公钥验签。
///
/// ## 验签方式
/// 商户使用平台公钥验签, 规则与异步通知/支付接口一致
/// (字段 ASCII 字典序排序, 空值不参与签名, 见 [cn.daxpay.open.payment.common.util.PaySignUtil])。
@Data
@Accessors(chain = true)
@Schema(title = "支付结果同步跳转参数")
public class PayResultRedirectResult {

    /// 状态码: 0=成功(对齐 [cn.daxpay.open.platform.core.code.CommonCode#SUCCESS_CODE])
    @Schema(description = "状态码")
    private int code;

    /// 状态描述
    @Schema(description = "状态描述")
    private String msg;

    /// 资金交易号(平台生成, 反查权威)
    @Schema(description = "资金交易号")
    private String tradeNo;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    private String orderNo;

    /// 商户业务单号
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// 订单状态(paid/failed/closed/expired)
    @Schema(description = "订单状态")
    private String status;

    /// 订单金额(最小货币单位, 分)
    @Schema(description = "订单金额(分)")
    private Long amount;

    /// 平台签名(商户用平台公钥验签)
    @Schema(description = "签名")
    private String sign;
}
