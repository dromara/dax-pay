package cn.daxpay.open.plugin.easypay.param.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 易支付 V2 退款查询参数
///
@Data
@Schema(title = "易支付V2退款查询参数")
public class EasyPayRefundQueryV2Param {

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    @JsonProperty("pid")
    private Integer pid;

    /// 平台退款单号
    @Schema(description = "平台退款单号")
    @JsonProperty("refund_no")
    private String refundNo;

    /// 商户退款单号
    @Schema(description = "商户退款单号")
    @JsonProperty("out_refund_no")
    private String outRefundNo;

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

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

}
