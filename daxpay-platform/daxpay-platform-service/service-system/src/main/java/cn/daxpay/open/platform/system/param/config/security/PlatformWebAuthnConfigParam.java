package cn.daxpay.open.platform.system.param.config.security;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥配置参数")
public class PlatformWebAuthnConfigParam {

    @Schema(description = "是否启用通行密钥认证")
    private Boolean enabled;

    /// rpId 必须为纯域名(点分标签, 不带协议/端口/路径), 带端口(如 localhost:6999)会被浏览器
    /// WebAuthn 校验直接拒绝导致注册必败; 允许空串(未启用时占位)
    @Pattern(regexp = "^$|^[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?)*$",
            message = "{validation.field.rpId.pattern}")
    @Schema(description = "依赖方ID(纯域名不带协议端口, 变更将导致已注册凭据全部失效)")
    private String rpId;

    @Schema(description = "依赖方显示名称")
    private String rpName;

    @Schema(description = "允许的调用来源列表(完整 origin)")
    private List<String> origins;
}
