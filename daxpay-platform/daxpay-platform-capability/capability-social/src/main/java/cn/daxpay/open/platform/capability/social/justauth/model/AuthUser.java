package cn.daxpay.open.platform.capability.social.justauth.model;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 授权用户信息
///
/// 各平台授权登录后归一化的用户信息, 仅保留系统所需字段
///
@Data
@Accessors(chain = true)
public class AuthUser {

    /// 平台用户唯一标识(openid/uuid/id)
    private String uuid;

    /// 用户名
    private String username;

    /// 昵称
    private String nickname;

    /// 头像
    private String avatar;

    /// 邮箱
    private String email;

    /// 用户来源(平台枚举名)
    private String source;

    /// 授权令牌
    private AuthToken token;
}
