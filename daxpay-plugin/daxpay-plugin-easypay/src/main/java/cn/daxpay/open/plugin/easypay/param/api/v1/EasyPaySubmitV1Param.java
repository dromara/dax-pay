package cn.daxpay.open.plugin.easypay.param.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 易支付 V1 页面跳转支付参数（submit.php）
///
@Data
@Schema(title = "易支付V1页面跳转参数")
public class EasyPaySubmitV1Param {

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 协议支付方式 alipay/wxpay
    @Schema(description = "支付方式 alipay/wxpay")
    private String type;

    /// 商户订单号
    @Schema(description = "商户订单号")
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    @JsonProperty("notify_url")
    private String notifyUrl;

    /// 同步跳转地址
    @Schema(description = "同步跳转地址")
    @JsonProperty("return_url")
    private String returnUrl;

    /// 商品名称
    @Schema(description = "商品名称")
    private String name;

    /// 金额（元，字符串）
    @Schema(description = "金额（元）")
    private String money;

    /// 客户端 IP
    @Schema(description = "客户端IP")
    private String clientip;

    /// 业务扩展参数
    @Schema(description = "业务扩展参数")
    private String param;

    /// 签名
    @Schema(description = "签名")
    private String sign;

    /// 签名类型
    @Schema(description = "签名类型")
    @JsonProperty("sign_type")
    private String signType;

    public void setOut_trade_no(String outTradeNo) { this.outTradeNo = outTradeNo; }
    public void setNotify_url(String notifyUrl) { this.notifyUrl = notifyUrl; }
    public void setReturn_url(String returnUrl) { this.returnUrl = returnUrl; }
    public void setSign_type(String signType) { this.signType = signType; }
}
