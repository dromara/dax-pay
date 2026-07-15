package cn.daxpay.open.plugin.easypay.param.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "易支付V1统一下单参数")
public class EasyPayCreateV1Param {
    private Integer pid;
    private String type;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("notify_url")
    private String notifyUrl;
    @JsonProperty("return_url")
    private String returnUrl;
    private String name;
    private String money;
    private String clientip;
    private String device;
    private String param;
    private String sign;
    @JsonProperty("sign_type")
    private String signType;

    public void setOut_trade_no(String outTradeNo) { this.outTradeNo = outTradeNo; }
    public void setNotify_url(String notifyUrl) { this.notifyUrl = notifyUrl; }
    public void setReturn_url(String returnUrl) { this.returnUrl = returnUrl; }
    public void setSign_type(String signType) { this.signType = signType; }
}
