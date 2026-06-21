package cn.daxpay.open.platform.iam.service.social.cache;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 社交登录授权上下文
///
/// 在 render 阶段生成并以 state 为键缓存, 在 exchange 阶段取出, 携带授权场景与用户信息
///
@Data
@Accessors(chain = true)
public class SocialAuthContext {

    /// 授权场景
    private SocialAuthMode mode;

    /// 终端编码
    private String clientCode;

    /// 本地用户ID(BIND 场景下为已登录用户)
    private Long userId;

    /// 回调成功后前端跳转的相对路径(可选)
    private String redirect;

    /// 平台来源(从 state 上下文恢复, 用于 exchange 时构建正确的 AuthRequest)
    private String source;
}
