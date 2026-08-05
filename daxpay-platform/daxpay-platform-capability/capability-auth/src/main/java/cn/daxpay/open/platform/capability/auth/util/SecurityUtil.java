package cn.daxpay.open.platform.capability.auth.util;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.capability.auth.cache.SessionCacheLocal;
import cn.daxpay.open.platform.capability.auth.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.DesensitizedUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

/// # 安全工具类
///
@Slf4j
@UtilityClass
public class SecurityUtil {

    private final String LOGIN_TYPE_PARAMETER = "loginType";

    private final String CLIENT_PARAMETER = "client";

    /// 获取当前用户,无异常
    /// @return 当前登录用户
    public Optional<UserDetail> getCurrentUser() {
        return getCurrentUser0();
    }

    /// 获取当前用户,无异常
    /// @return 当前登录用户的id,如未登录
    public Long getUserIdOrDefaultId() {
        return getCurrentUser().map(UserDetail::getId).orElse(DesensitizedUtil.userId());
    }

    /// 获取用户id
    public Long getUserId() {
        return getCurrentUser().map(UserDetail::getId).orElseThrow(NotLoginException::new);
    }

    /// 获取用户
    public UserDetail getUser() {
        return getCurrentUser().orElseThrow(NotLoginException::new);
    }

    /// 判断当前是否已登录
    /// @return true=已登录，false=未登录
    public boolean isLogin() {
        return getCurrentUser().isPresent();
    }

    /// 判断当前是否未登录
    /// @return true=未登录，false=已登录
    public boolean notLogin() {
        return !isLogin();
    }

    /// 获取登录方式 异步环境中获取会有问题
    @Nullable
    public String getLoginType(HttpServletRequest request) {
        return request.getParameter(LOGIN_TYPE_PARAMETER);
    }

    /// 获取终端Code 异步环境中获取会有问题
    @Nullable
    public String getClient(HttpServletRequest request) {
        return request.getParameter(CLIENT_PARAMETER);
    }

    /// 获取当前用户,无异常, 使用线程缓存来减少redis的访问频率
    private Optional<UserDetail> getCurrentUser0() {
        Optional<UserDetail> userDetail = Optional.ofNullable(SessionCacheLocal.getUserInfo());
        if (userDetail.isEmpty()) {
            try {
                // 会话不为空. 获取用户信息不为空. 放入缓存
                SaSession saSession = StpUtil.getSession();
                if (Objects.nonNull(saSession)){
                    UserDetail user = saSession.getModel(CommonCode.USER, UserDetail.class);
                    SessionCacheLocal.putUserInfo(user);
                    userDetail = Optional.of(user);
                }
            }
            catch (SaTokenException e) {
                userDetail = Optional.empty();
                log.debug("获取当前用户失败: {}", e.getMessage());
            }
        }
        return userDetail;
    }

}

