package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/// # 易支付 V2 回调报文
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付V2回调报文")
public class EasyPayCallbackV2Result implements Serializable {

    @JsonProperty("pid")
    private Integer pid;

    @JsonProperty("trade_no")
    private String tradeNo;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    @JsonProperty("api_trade_no")
    private String apiTradeNo;

    @JsonProperty("type")
    private String type;

    @JsonProperty("trade_status")
    private String tradeStatus;

    @JsonProperty("addtime")
    private String addTime;

    @JsonProperty("endtime")
    private String endTime;

    @JsonProperty("name")
    private String name;

    @JsonProperty("money")
    private String money;

    @JsonProperty("param")
    private String param;

    @JsonProperty("buyer")
    private String buyer;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("sign_type")
    private String signType;
}
