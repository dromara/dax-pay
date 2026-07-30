package cn.daxpay.open.payment.merchant.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 网关支付客户端环境配置结果(子表)
@Data
@Accessors(chain = true)
@Schema(title = "网关支付客户端环境配置结果")
public class GatewayPayClientEnvResult {

    /// @see cn.daxpay.open.payment.merchant.enums.ClientEnvEnum
    @Schema(description = "客户端环境编码")
    private String clientEnv;

    /// @see cn.daxpay.open.payment.merchant.enums.CodePayFormEnum
    @Schema(description = "支付形态: h5/mini")
    private String payForm;

    @Schema(description = "支付方式")
    private String method;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "支付能力")
    private String capability;
}
