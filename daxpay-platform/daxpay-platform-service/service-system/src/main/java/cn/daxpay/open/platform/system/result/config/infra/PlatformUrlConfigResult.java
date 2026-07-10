package cn.daxpay.open.platform.system.result.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台端点配置返回结果
///
@Data
@Accessors(chain = true)
@Schema(title = "平台端点配置")
public class PlatformUrlConfigResult {

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
