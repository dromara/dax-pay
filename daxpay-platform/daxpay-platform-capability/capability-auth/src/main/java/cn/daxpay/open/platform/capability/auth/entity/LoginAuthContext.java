package cn.daxpay.open.platform.capability.auth.entity;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/// # 认证上下文
///
@Getter
@Setter
@Accessors(chain = true)
public class LoginAuthContext {

    /// 请求
    @NotNull
    private HttpServletRequest request;

    /// 响应
    @NotNull
    private HttpServletResponse response;

    /// 身份域编码
    @NotNull
    private String clientCode;

    /// 登录方式
    @NotNull
    private String authLoginType;

    /// 用户对象
    private UserDetail userDetail;

    /// 认证参数配置
    @NotNull
    private PlatformStarterProperties.Auth authProperties;

}

