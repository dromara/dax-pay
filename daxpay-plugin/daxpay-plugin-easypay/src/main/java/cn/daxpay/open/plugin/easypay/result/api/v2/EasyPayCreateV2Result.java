package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "EasyPayCreateV2Result")
public class EasyPayCreateV2Result {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("trade_no")
    private String tradeNo;

    @JsonProperty("pay_type")
    private String payType;

    @JsonProperty("pay_info")
    private String payInfo;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("sign_type")
    private String signType;

}
