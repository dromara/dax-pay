package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付 V2 退款查询响应
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付V2退款查询响应")
public class EasyPayRefundOrderV2Result {

    /// 返回状态码（0 成功）
    @Schema(description = "返回状态码")
    @JsonProperty("code")
    private Integer code;

    /// 错误信息
    @Schema(description = "错误信息")
    @JsonProperty("msg")
    private String msg;

    /// 平台退款单号
    @Schema(description = "平台退款单号")
    @JsonProperty("refund_no")
    private String refundNo;

    /// 商户退款单号
    @Schema(description = "商户退款单号")
    @JsonProperty("out_refund_no")
    private String outRefundNo;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    @JsonProperty("trade_no")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /// 退款金额（元，字符串）
    @Schema(description = "退款金额（元）")
    @JsonProperty("money")
    private String money;

    /// 实退金额（元，字符串）
    @Schema(description = "实退金额（元）")
    @JsonProperty("reducemoney")
    private String reducemoney;

    /// 退款状态
    @Schema(description = "退款状态")
    @JsonProperty("status")
    private Integer status;

    /// 签名
    @Schema(description = "签名")
    @JsonProperty("sign")
    private String sign;

    /// 签名类型（RSA）
    @Schema(description = "签名类型")
    @JsonProperty("sign_type")
    private String signType;

}
