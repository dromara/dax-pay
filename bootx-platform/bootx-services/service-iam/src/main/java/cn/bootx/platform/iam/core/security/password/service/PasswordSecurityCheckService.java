package cn.bootx.platform.iam.core.security.password.service;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.iam.core.security.password.dao.PasswordChangeHistoryManager;
import cn.bootx.platform.iam.core.security.password.entity.PasswordChangeHistory;
import cn.bootx.platform.iam.core.user.dao.UserExpandInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserExpandInfo;
import cn.bootx.platform.iam.core.user.service.UserExpandInfoService;
import cn.bootx.platform.iam.dto.security.PasswordSecurityConfigDto;
import cn.bootx.platform.iam.dto.security.passwordSecurityCheckResult;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.starter.auth.exception.NotLoginException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 密码安全校验服务
 * @author xxm
 * @since 2023/10/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordSecurityCheckService {

    private final PasswordSecurityConfigService configService;

    private final PasswordChangeHistoryManager historyManager;

    private final UserExpandInfoManager userExpandInfoManager;

    private final UserExpandInfoService userExpandInfoService;

    /**
     * 登录后检查密码相关的情况
     */
    public passwordSecurityCheckResult check(){
        passwordSecurityCheckResult result = new passwordSecurityCheckResult();
        UserDetail userDetail = SecurityUtil.getCurrentUser()
                .orElseThrow(NotLoginException::new);
        if (userDetail.isAdmin()){
            return result;
        }
        Long userId = userDetail.getId();
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(userId).orElseThrow(UserInfoNotExistsException::new);

        PasswordSecurityConfigDto securityConfig = configService.getDefault();
        // 检查是否是默认密码未进行修改
        if (this.isDefaultPassword(userExpandInfo,securityConfig)){
            return result.setDefaultPwd(true);
        }
        int state = this.verifyPasswordExpire(userExpandInfo, securityConfig);
        // 检查密码是否已经过期, 如果过期更新用户过期状态
        if (state == 0){
            return result.setExpirePwd(true);
        }
        // 检查密码是否到了提示过期的时候
        if (state < 0){
            return result.setExpireRemind(true)
                    .setExpireRemindNum(Math.abs(state));
        }
        return result;
    }


    /**
     * 判断用户初始化密码是否需要修改
     */
    public boolean isDefaultPassword(UserExpandInfo userExpandInfo, PasswordSecurityConfigDto securityConfig){
        return userExpandInfo.isInitialPassword() && securityConfig.isRequireChangePwd();
    }


    /**
     * 验证密码过期和提醒状态
     * 1. 未过期也不需要提醒
     * 0. 密码已经过期
     * -N. 密码还有几天过期, 需要进行提醒
     */
    public int verifyPasswordExpire(UserExpandInfo userExpandInfo,PasswordSecurityConfigDto securityConfig){
        // 已经是密码过期状态
        if (userExpandInfo.isExpirePassword()){
            return 0;
        }

        // 判断用户密码是否需要强制进行更改
        List<PasswordChangeHistory> changeHistoryList = historyManager.findAllByUserAndCount(userExpandInfo.getId(), 1);
        if (CollUtil.isNotEmpty(changeHistoryList)){
            PasswordChangeHistory passwordChangeHistory = changeHistoryList.get(0);
            LocalDateTime createTime = passwordChangeHistory.getCreateTime();
            // 判断上次更改密码的时间距今是多少
            int keepPwdDay = (int) LocalDateTimeUtil.between(createTime, LocalDateTime.now(), ChronoUnit.DAYS);

            int dealDay = securityConfig.getUpdateFrequency() - keepPwdDay;
            // 密码过期
            if (dealDay < 0){
                // 更新过期状态
                userExpandInfoService.userExpirePwd(userExpandInfo.getId());
                return 0;
            }
            // 判断是否满足密码修改的倒计时提醒
            if (dealDay < securityConfig.getExpireRemind()){
                return -dealDay;
            }
            // 密码未过期
            return 1;

        }
        return 1;
    }


}
