package cn.daxpay.open.payment.merchant.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/// # 网关支付配置参数(商户端)
///
/// 商户端专用参数, 不含商户号(防越权: 由控制器/服务端按登录态与应用归属组装,
/// 对照运营端参数 [GatewayPayConfigParam])。
///
/// level 控制子表填充:
/// - AUTO: clientEnvs 可为空
/// - METHOD: 每 (clientEnv, payForm) 填 method
/// - DIRECT: 每 (clientEnv, payForm) 填 channelMchNo + capability
@Data
@Schema(title = "网关支付配置参数(商户端)")
public class MchGatewayPayConfigParam {

    @Schema(description = "应用号")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

    /// @see cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum
    @Schema(description = "配置深度: auto/method/direct")
    @NotBlank(message = "{validation.field.level.notBlank}")
    @Size(max = 32, message = "{validation.field.level.size}")
    private String level;

    @Schema(description = "是否自动拉起支付(码牌仅对固定金额生效)")
    private Boolean autoLaunch;

    @Valid
    @Schema(description = "客户端环境×形态配置列表")
    private List<GatewayPayClientEnvParam> clientEnvs;
}
