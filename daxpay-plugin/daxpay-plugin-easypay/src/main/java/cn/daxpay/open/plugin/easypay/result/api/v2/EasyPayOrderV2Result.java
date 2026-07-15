package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "EasyPayOrderV2Result")
public class EasyPayOrderV2Result {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("trade_no")
    private String tradeNo;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("api_trade_no")
    private String apiTradeNo;

    @JsonProperty("type")
    private String type;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("pid")
    private Integer pid;

    @JsonProperty("addtime")
    private String addtime;

    @JsonProperty("endtime")
    private String endtime;

    @JsonProperty("name")
    private String name;

    @JsonProperty("money")
    private String money;

    @JsonProperty("refundmoney")
    private String refundmoney;

    @JsonProperty("param")
    private String param;

    @JsonProperty("buyer")
    private String buyer;

    @JsonProperty("clientip")
    private String clientip;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("sign_type")
    private String signType;

}
