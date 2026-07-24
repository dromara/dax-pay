package cn.daxpay.open.platform.system.result.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 应用内社交自动登录配置结果
///
@Data
@Accessors(chain = true)
@Schema(title = "应用内社交自动登录配置结果")
public class PlatformSocialAutoLoginConfigResult {

    @Schema(description = "运营端自动登录策略")
    private ClientAutoLoginResult admin;

    @Schema(description = "商户端自动登录策略")
    private ClientAutoLoginResult merchant;

    /// 单端自动登录结果
    @Data
    @Accessors(chain = true)
    @Schema(title = "单端自动登录结果")
    public static class ClientAutoLoginResult {

        @Schema(description = "是否启用应用内自动登录")
        private Boolean enabled;

        @Schema(description = "可自动跳转的社交平台编码列表")
        private List<String> sources;
    }
}
