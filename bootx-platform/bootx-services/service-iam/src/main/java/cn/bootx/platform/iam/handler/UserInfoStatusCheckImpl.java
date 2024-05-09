package cn.bootx.platform.iam.handler;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.iam.code.UserStatusCode;
import cn.bootx.platform.iam.core.security.password.dao.PasswordLoginFailRecordManager;
import cn.bootx.platform.iam.core.security.password.entity.PasswordLoginFailRecord;
import cn.bootx.platform.iam.core.security.password.service.PasswordLoginFailRecordService;
import cn.bootx.platform.iam.core.security.password.service.PasswordSecurityConfigService;
import cn.bootx.platform.iam.core.user.service.UserAdminService;
import cn.bootx.platform.iam.dto.security.PasswordSecurityConfigDto;
import cn.bootx.platform.starter.auth.authentication.UserInfoStatusCheck;
import cn.bootx.platform.starter.auth.configuration.AuthProperties;
import cn.bootx.platform.starter.auth.entity.AuthClient;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 用户状态检查实现类
 * @author xxm
 * @since 2023/9/21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoStatusCheckImpl implements UserInfoStatusCheck {
    private final PasswordLoginFailRecordManager loginFailRecordManager;
    private final PasswordLoginFailRecordService loginFailRecordService;
    private final PasswordSecurityConfigService securityConfigService;
    private final UserAdminService userAdminService;

    /**
     *  检查用户状态
     * @param authInfoResult 认证返回结果
     * @param context 登录认证上下文
     */
    @Override
    public void check(AuthInfoResult authInfoResult, LoginAuthContext context) {
        UserDetail userDetail = authInfoResult.getUserDetail();
        AuthClient authClient = context.getAuthClient();
        AuthProperties authProperties = context.getAuthProperties();
        // 判断是否开启了超级管理员
        if (!authProperties.isEnableAdmin() && userDetail.isAdmin()) {
            throw new LoginFailureException("未开启超级管理员权限");
        }
        // 管理员跳过各种限制
        if (userDetail.isAdmin()) {
            return;
        }
        // 判断用户是否拥有认证应用的权限
        if (!userDetail.getClientIds().contains(authClient.getId())) {
            throw new LoginFailureException("该用户不拥有该终端的权限");
        }
        //  判断用户是否被禁用
        if (Objects.equals(userDetail.getStatus(), UserStatusCode.BAN)){
            throw new LoginFailureException("该用户已被禁用");
        }

        // 密码是否因为错误被锁定
        if (Objects.equals(userDetail.getStatus(), UserStatusCode.LOCK)){
            this.checkLock(userDetail);
        }
    }

    /**
     * 密码是否因为错误被锁定, 同时处理锁定结束, 解锁用户
     */
    private void checkLock(UserDetail userDetail){
        PasswordSecurityConfigDto securityConfig = securityConfigService.getDefault();
        PasswordLoginFailRecord loginFailRecord = loginFailRecordManager.findByUserId(userDetail.getId())
                .orElse(null);
        if (Objects.isNull(loginFailRecord)){
            throw new LoginFailureException("该用户已被锁定，请联系管理员进行解锁");
        }
        // 判断锁定时间是否已经结束, 结束了就清空错误次数, 并对用户进行解锁
        LocalDateTime failOverTime = LocalDateTimeUtil.offset(loginFailRecord.getFailTime(), securityConfig.getErrorLockTime(), ChronoUnit.MINUTES);
        long seconds = LocalDateTimeUtil.between(LocalDateTime.now(), failOverTime, ChronoUnit.SECONDS);
        if (seconds>0){
            loginFailRecordService.clearFailCount(userDetail.getId());
            userAdminService.unlock(seconds);
        } else {
            throw new LoginFailureException(StrUtil.format("该用户已被锁定，请 {} 秒后再试",seconds));
        }
    }
}
