package cn.bootx.platform.iam.core.security.password.service;

import cn.bootx.platform.iam.core.security.password.dao.PasswordLoginFailRecordManager;
import cn.bootx.platform.iam.core.security.password.entity.PasswordLoginFailRecord;
import cn.bootx.platform.iam.core.user.service.UserAdminService;
import cn.bootx.platform.iam.dto.security.PasswordSecurityConfigDto;
import cn.bootx.platform.iam.event.user.UserRestartPasswordEvent;
import cn.bootx.platform.iam.event.user.UserUnlockEvent;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 密码登录失败记录
 * @author xxm
 * @since 2023/9/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordLoginFailRecordService {
    private final PasswordLoginFailRecordManager passwordLoginFailRecordManager;

    private final PasswordSecurityConfigService passwordSecurityConfigService;

    private final UserAdminService userAdminService;

    /**
     * 获取 密码登录失败记录
     */
    public void get(){

    }

    /**
     * 登录失败的错误处理
     */
    public String passwordError(Long userId){
        // 密码安全配置
        PasswordSecurityConfigDto securityConfig = passwordSecurityConfigService.getDefault();

        // 更新错误次数
        PasswordLoginFailRecord loginFailRecord = passwordLoginFailRecordManager.findByUserId(userId)
                .orElse(new PasswordLoginFailRecord()
                        .setFailCount(0)
                        .setUserId(userId));
        loginFailRecord.setFailCount(loginFailRecord.getFailCount()+1)
                .setFailTime(LocalDateTime.now());

        // 判断是否超过最大错误次数, 进行锁定用户
        int errCount = securityConfig.getMaxPwdErrorCount() - loginFailRecord.getFailCount();
        if (errCount == 0){
            userAdminService.lock(userId);
            // 需要将错误次数更新到数据库
            passwordLoginFailRecordManager.saveOrUpdate(loginFailRecord);
            return StrUtil.format("账号已被锁定, 请{}分钟后再试", securityConfig.getMaxPwdErrorCount());
        } else if (errCount < 0) {
            // 继续尝试密码, 不再更新错误次数, 直接返回
            return StrUtil.format("账号已被锁定, 请{}分钟后再试", securityConfig.getMaxPwdErrorCount());
        }
        passwordLoginFailRecordManager.saveOrUpdate(loginFailRecord);
        return StrUtil.format("密码不正确, 还有{}次机会", errCount);
    }

    /**
     * 清除登录失败的次数
     */
    @Async("taskExecutor")
    public void clearFailCount(Long userId){
        passwordLoginFailRecordManager.findById(userId)
                .ifPresent(r -> {
                    if (r.getFailCount() > 0){
                        r.setFailCount(0);
                        passwordLoginFailRecordManager.updateById(r);
                    }
                });
    }

    /**
     * 解锁用户,清除登录失败的次数
     */
    @EventListener(UserUnlockEvent.class)
    @Async("taskExecutor")
    public void clearBatchFailCount(UserUnlockEvent event){
        this.clearBatchFailCount(event.getUserIds());
    }

    /**
     * 重置用户密码, 清除登录失败的次数
     */
    @EventListener(UserRestartPasswordEvent.class)
    @Async("taskExecutor")
    public void clearBatchFailCount(UserRestartPasswordEvent event){
        this.clearBatchFailCount(event.getUserIds());
    }


    /**
     * 批量清除登录失败的次数
     */
    public void clearBatchFailCount(List<Long> userIds){
        passwordLoginFailRecordManager.clearBatchFailCount(userIds);
    }
}
