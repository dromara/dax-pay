package cn.daxpay.open.platform.system.param.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台端点配置参数
///
/// 全部选填: 未配置的端对应功能不可用, 但不强制要求一次性配置完整.
/// 如未启用支付网关的环境 paymentGatewayBaseUrl 可留空.
///
@Data
@Accessors(chain = true)
@Schema(title = "平台端点配置参数")
public class PlatformUrlConfigParam {

    /// 管理端访问地址
    @Schema(description = "管理端访问地址")
    private String adminBaseUrl;

    /// 商户端访问地址
    @Schema(description = "商户端访问地址")
    private String merchantBaseUrl;

    /// 支付网关前端地址
    @Schema(description = "支付网关前端地址")
    private String paymentGatewayBaseUrl;

    /// 后端 API 地址
    @Schema(description = "后端 API 地址")
    private String backendBaseUrl;
}
