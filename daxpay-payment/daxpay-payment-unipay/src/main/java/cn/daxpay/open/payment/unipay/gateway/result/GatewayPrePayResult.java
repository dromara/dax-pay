package cn.daxpay.open.payment.unipay.gateway.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 网关预下单结果
@Data
@Accessors(chain = true)
@Schema(title = "网关预下单结果")
public class GatewayPrePayResult {

    @Schema(description = "平台网关单号")
    private String orderNo;

    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    @Schema(description = "业务状态")
    private String status;

    @Schema(description = "网关落地页 URL")
    private String url;

    @Schema(description = "过期时间")
    private OffsetDateTime expiredTime;
}
