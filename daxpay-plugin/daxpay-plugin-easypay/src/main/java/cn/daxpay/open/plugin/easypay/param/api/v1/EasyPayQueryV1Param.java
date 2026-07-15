package cn.daxpay.open.plugin.easypay.param.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 易支付 V1 订单查询参数（api.php）
///
@Data
@Schema(title = "易支付V1查询参数")
public class EasyPayQueryV1Param {

    /// 操作类型（order 等）
    @Schema(description = "操作类型")
    private String act;

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// MD5 密钥（V1 查询可传 key 代替 sign）
    @Schema(description = "MD5密钥")
    private String key;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    @JsonProperty("trade_no")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    public void setTrade_no(String tradeNo) { this.tradeNo = tradeNo; }
    public void setOut_trade_no(String outTradeNo) { this.outTradeNo = outTradeNo; }
}
