package cn.daxpay.open.plugin.easypay.result.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EasyPayOrderV1Result {
    private Integer code;
    private String msg;
    @JsonProperty("trade_no")
    private String tradeNo;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    private String type;
    private Integer status;
    private Integer pid;
    private String addtime;
    private String endtime;
    private String name;
    private String money;
    private String param;
    private String buyer;
}
