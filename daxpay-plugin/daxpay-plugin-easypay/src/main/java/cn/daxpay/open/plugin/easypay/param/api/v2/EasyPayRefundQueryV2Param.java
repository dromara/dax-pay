package cn.daxpay.open.plugin.easypay.param.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "EasyPayRefundQueryV2Param")
public class EasyPayRefundQueryV2Param {

    @JsonProperty("pid")
    private Integer pid;

    @JsonProperty("refund_no")
    private String refundNo;

    @JsonProperty("out_refund_no")
    private String outRefundNo;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("sign")
    private String sign;

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
