package cn.daxpay.open.plugin.easypay.param.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "EasyPaySubmitV2Param")
public class EasyPaySubmitV2Param {

    @JsonProperty("pid")
    private Integer pid;

    @JsonProperty("type")
    private String type;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("notify_url")
    private String notifyUrl;

    @JsonProperty("return_url")
    private String returnUrl;

    @JsonProperty("name")
    private String name;

    @JsonProperty("money")
    private String money;

    @JsonProperty("clientip")
    private String clientip;

    @JsonProperty("param")
    private String param;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("sign")
    private String sign;

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
