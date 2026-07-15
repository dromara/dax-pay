package cn.daxpay.open.plugin.easypay.param.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "易支付V1查询参数")
public class EasyPayQueryV1Param {
    private String act;
    private Integer pid;
    private String key;
    @JsonProperty("trade_no")
    private String tradeNo;
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    public void setTrade_no(String tradeNo) { this.tradeNo = tradeNo; }
    public void setOut_trade_no(String outTradeNo) { this.outTradeNo = outTradeNo; }
}
