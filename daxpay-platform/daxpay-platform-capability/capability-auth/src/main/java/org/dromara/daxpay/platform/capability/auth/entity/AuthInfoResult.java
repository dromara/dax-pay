package org.dromara.daxpay.platform.capability.auth.entity;

import org.dromara.daxpay.platform.core.entity.UserDetail;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 认证返回结果
///
@Data
@Accessors(chain = true)
public class AuthInfoResult {

    /// 用户id
    private Object id;

    /// 认证终端(例如管理端/商户端/代理端等)
    private String client = "";

    /// 登录方式(例如web/开放平台/app等)
    private String loginType = "";

    /// 用户对象
    private UserDetail userDetail;

}
