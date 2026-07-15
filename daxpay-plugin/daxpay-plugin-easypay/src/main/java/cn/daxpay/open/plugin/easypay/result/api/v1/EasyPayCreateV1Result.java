package cn.daxpay.open.plugin.easypay.result.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EasyPayCreateV1Result {
    private Integer code;
    private String msg;
    @JsonProperty("trade_no")
    private String tradeNo;
    @JsonProperty("payurl")
    private String payurl;
    @JsonProperty("qrcode")
    private String qrcode;
    @JsonProperty("urlscheme")
    private String urlscheme;
}
