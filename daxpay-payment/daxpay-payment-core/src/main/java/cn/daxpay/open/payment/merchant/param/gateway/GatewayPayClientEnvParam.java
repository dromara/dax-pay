package cn.daxpay.open.payment.merchant.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 网关支付客户端环境配置参数(子表)
@Data
@Schema(title = "网关支付客户端环境配置参数")
public class GatewayPayClientEnvParam {

    /// @see cn.daxpay.open.payment.merchant.enums.ClientEnvEnum
    @Schema(description = "客户端环境编码: wechat/alipay/union_pay/douyin")
    @Size(max = 32, message = "{validation.field.clientEnv.size}")
    private String clientEnv;

    /// @see cn.daxpay.open.payment.merchant.enums.CodePayFormEnum
    @Schema(description = "支付形态: h5/mini")
    @Size(max = 16, message = "{validation.field.payForm.size}")
    private String payForm;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum
    @Schema(description = "支付方式(METHOD 模式填)")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String method;

    @Schema(description = "通道商户号(DIRECT 模式填)")
    @Size(max = 64, message = "{validation.field.channelMchNo.size}")
    private String channelMchNo;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @Schema(description = "支付能力(DIRECT 模式填)")
    @Size(max = 64, message = "{validation.field.capability.size}")
    private String capability;
}
