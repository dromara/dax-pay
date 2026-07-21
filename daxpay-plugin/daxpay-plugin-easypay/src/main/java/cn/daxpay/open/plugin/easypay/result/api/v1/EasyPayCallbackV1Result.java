package cn.daxpay.open.plugin.easypay.result.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/// # 易支付 V1 回调报文
///
@Data
@Accessors(chain = true)
public class EasyPayCallbackV1Result implements Serializable {

    @JsonProperty("pid")
    private Integer pid;

    @JsonProperty("trade_no")
    private String tradeNo;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("money")
    private String money;

    @JsonProperty("trade_status")
    private String tradeStatus;

    @JsonProperty("param")
    private String param;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("sign_type")
    private String signType;
}
