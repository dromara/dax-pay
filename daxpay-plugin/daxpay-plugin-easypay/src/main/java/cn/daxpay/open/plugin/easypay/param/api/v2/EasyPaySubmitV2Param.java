package cn.daxpay.open.plugin.easypay.param.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 易支付 V2 页面跳转支付参数
///
@Data
@Schema(title = "易支付V2页面跳转参数")
public class EasyPaySubmitV2Param {

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    @JsonProperty("pid")
    private Integer pid;

    /// 协议支付方式 alipay/wxpay
    @Schema(description = "支付方式 alipay/wxpay")
    @JsonProperty("type")
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
    @JsonProperty("name")
    private String name;

    /// 金额（元，字符串）
    @Schema(description = "金额（元）")
    @JsonProperty("money")
    private String money;

    /// 客户端 IP
    @Schema(description = "客户端IP")
    @JsonProperty("clientip")
    private String clientip;

    /// 业务扩展参数
    @Schema(description = "业务扩展参数")
    @JsonProperty("param")
    private String param;

    /// 当前时间戳（秒）
    @Schema(description = "当前时间戳")
    @JsonProperty("timestamp")
    private String timestamp;

    /// 签名
    @Schema(description = "签名")
    @JsonProperty("sign")
    private String sign;

    /// 签名类型（RSA）
    @Schema(description = "签名类型")
    @JsonProperty("sign_type")
    private String signType;

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

}
