package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 应用内社交自动登录配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "应用内社交自动登录配置参数")
public class PlatformSocialAutoLoginConfigParam {

    @Schema(description = "运营端自动登录策略")
    private ClientAutoLoginParam admin;

    @Schema(description = "商户端自动登录策略")
    private ClientAutoLoginParam merchant;

    /// 单端自动登录参数
    @Data
    @Accessors(chain = true)
    @Schema(title = "单端自动登录参数")
    public static class ClientAutoLoginParam {

        @Schema(description = "是否启用应用内自动登录")
        private Boolean enabled;

        @Schema(description = "可自动跳转的社交平台编码列表")
        private List<String> sources;

        /// 兼容旧客户端单字段; 服务端优先用 sources, 空时回退
        @Schema(description = "兼容旧版单平台编码", deprecated = true)
        @Deprecated
        private String source;
    }
}
