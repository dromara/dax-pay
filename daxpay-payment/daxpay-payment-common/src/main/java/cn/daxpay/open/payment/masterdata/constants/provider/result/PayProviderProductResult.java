package cn.daxpay.open.payment.masterdata.constants.provider.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 渠道支付方式支持的支付产品
@Data
@Accessors(chain = true)
@Schema(title = "渠道支付方式支持的支付产品")
public class PayProviderProductResult {

    @Schema(description = "产品名称")
    private String label;

    @Schema(description = "产品编码")
    private String value;

    @Schema(description = "所属通道编码")
    private String channel;

    @Schema(description = "通道名称")
    private String channelName;
}