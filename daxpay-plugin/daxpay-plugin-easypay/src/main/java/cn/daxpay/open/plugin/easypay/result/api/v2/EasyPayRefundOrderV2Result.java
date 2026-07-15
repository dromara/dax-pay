package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "EasyPayRefundOrderV2Result")
public class EasyPayRefundOrderV2Result {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("refund_no")
    private String refundNo;

    @JsonProperty("out_refund_no")
    private String outRefundNo;

    @JsonProperty("trade_no")
    private String tradeNo;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("money")
    private String money;

    @JsonProperty("reducemoney")
    private String reducemoney;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("sign_type")
    private String signType;

}
