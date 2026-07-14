package cn.daxpay.open.platform.system.param.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台端点连通性检查参数
///
@Data
@Accessors(chain = true)
@Schema(title = "平台端点连通性检查参数")
public class PlatformUrlCheckParam {

    /// 端点类型: admin / merchant / paymentGateway / backend
    @NotBlank(message = "{validation.field.urlType.notBlank}")
    @Schema(description = "端点类型: admin / merchant / paymentGateway / backend")
    private String urlType;

    /// 待检查地址(可选; 为空则使用已保存配置)
    @Schema(description = "待检查地址, 为空则使用已保存配置")
    private String url;
}
