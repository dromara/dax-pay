package cn.daxpay.open.plugin.easypay.param.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 易支付 V2 订单查询参数
///
@Data
@Schema(title = "易支付V2订单查询参数")
public class EasyPayQueryV2Param {

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    @JsonProperty("pid")
    private Integer pid;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    @JsonProperty("trade_no")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /// 当前时间戳（秒）
    @Schema(description = "当前时间戳")
    @JsonProperty("timestamp")
    private String timestamp;

    /// 签名
    @Schema(description = "签名")
    @JsonProperty("sign")
    private String sign;

    /// 签名类型（RSA）
    @Schema(description = "签名类型")
    @JsonProperty("sign_type")
    private String signType;

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

}
