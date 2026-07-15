package cn.daxpay.open.plugin.easypay.param.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 易支付 V2 统一下单参数
///
@Data
@Schema(title = "易支付V2统一下单参数")
public class EasyPayCreateV2Param {

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    @JsonProperty("pid")
    private Integer pid;

    /// 接口类型 web/jump/jsapi
    @Schema(description = "接口类型 web/jump/jsapi")
    @JsonProperty("method")
    private String method;

    /// 设备类型 pc/mobile
    @Schema(description = "设备类型")
    @JsonProperty("device")
    private String device;

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

    /// 被扫支付授权码
    @Schema(description = "被扫支付授权码")
    @JsonProperty("auth_code")
    private String authCode;

    /// 用户 OpenId
    @Schema(description = "用户OpenId")
    @JsonProperty("sub_openid")
    private String subOpenid;

    /// 是否小程序（1=是）
    @Schema(description = "是否小程序，1:是")
    @JsonProperty("is_applet")
    private Integer isApplet;

    /// 子商户/公众号 AppId
    @Schema(description = "公众号AppId")
    @JsonProperty("sub_appid")
    private String subAppid;

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

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setSubOpenid(String subOpenid) {
        this.subOpenid = subOpenid;
    }

    public void setIsApplet(Integer isApplet) {
        this.isApplet = isApplet;
    }

    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

}
