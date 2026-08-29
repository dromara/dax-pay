package cn.daxpay.open.platform.iam.service.session;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.code.LoginLockStatusEnum;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.entity.user.UserPasswordSecurity;
import cn.daxpay.open.platform.iam.param.session.LoginLockQuery;
import cn.daxpay.open.platform.iam.result.session.LoginLockPageResult;
import cn.daxpay.open.platform.iam.result.session.LoginLockResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 登录锁定监控服务
///
/// 系统监控"锁定用户"页面数据源: 查询登录重试锁定状态(登录失败与敏感操作验证失败共用计数器),
/// 展示锁定中/已到期/计数中的用户并提供管理员手动解锁。
/// 真攻击场景的正确动作是封禁(ban)而非解锁, 解锁仅用于协助被误锁的合法用户提前恢复。
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLockService {

    private final UserPasswordSecurityManager passwordSecurityManager;

    private final LoginRetryService loginRetryService;

    private final IamSecurityConfigService iamSecurityConfigService;

    /// 分页查询锁定与计数中的用户
    public LoginLockPageResult page(PageParam pageParam, LoginLockQuery query) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Page<LoginLockResult> mpPage = MpUtil.getMpPage(pageParam);
        MPJLambdaWrapper<UserPasswordSecurity> wrapper = new MPJLambdaWrapper<UserPasswordSecurity>()
                .selectAll(UserPasswordSecurity.class)
                .selectAs(UserInfo::getName, LoginLockResult::getUsername)
                .selectAs(UserInfo::getAccount, LoginLockResult::getAccount)
                .selectAs(UserInfo::getClientCode, LoginLockResult::getClientCode)
                // 用户主体信息仅作展示, 逻辑删除的历史用户仍保留安全记录
                .leftJoin(UserInfo.class, UserInfo::getId, UserPasswordSecurity::getId)
                // 仅展示有安全事件的记录: 锁定中/已到期未清理/失败计数中
                .and(w -> w.isNotNull(UserPasswordSecurity::getLockTime)
                        .or().gt(UserPasswordSecurity::getPasswordErrorCount, 0))
                // 按名称模糊查询
                .like(StrUtil.isNotBlank(query.getUsername()), UserInfo::getName, query.getUsername())
                // 按账号模糊查询
                .like(StrUtil.isNotBlank(query.getAccount()), UserInfo::getAccount, query.getAccount())
                // 按终端过滤
                .eq(StrUtil.isNotBlank(query.getClientCode()), UserInfo::getClientCode, query.getClientCode())
                // 最近失败的排前面, 监控视角优先看到活跃事件
                .orderByDesc(UserPasswordSecurity::getLastFailureTime);
        // 按锁定状态筛选(条件之间 AND, 直接拼主链)
        this.applyStatusFilter(wrapper, query.getStatus(), now);
        Page<LoginLockResult> page = passwordSecurityManager.selectJoinListPage(mpPage, LoginLockResult.class, wrapper);

        // 状态与剩余分钟为衍生字段, 统一在 Java 层计算(与 LoginRetryService#checkLockedState 同口径)
        page.getRecords().forEach(result -> this.fillComputedFields(result, now));

        boolean lockoutEnabled = Boolean.TRUE.equals(iamSecurityConfigService.getLoginSecurity().getLockoutEnabled());
        return new LoginLockPageResult()
                .setLockoutEnabled(lockoutEnabled)
                .setPage(new PageResult<LoginLockResult>()
                        .setRecords(page.getRecords())
                        .setCurrent(page.getCurrent())
                        .setSize(page.getSize())
                        .setTotal(page.getTotal()));
    }

    /// 管理员手动解锁: 清除锁定结束时间与失败计数, 复用 [LoginRetryService#unlockAccount]
    public void unlock(Long userId) {
        loginRetryService.unlockAccount(userId);
    }

    /// 按锁定状态拼接筛选条件(与 [LoginLockStatusEnum] 计算口径一致), 非法状态值忽略不抛异常
    private void applyStatusFilter(MPJLambdaWrapper<UserPasswordSecurity> wrapper, String status, OffsetDateTime now) {
        LoginLockStatusEnum statusEnum = this.parseStatus(status);
        if (statusEnum == null) {
            return;
        }
        switch (statusEnum) {
            case LOCKED -> wrapper.isNotNull(UserPasswordSecurity::getLockTime)
                    .gt(UserPasswordSecurity::getLockTime, now);
            case EXPIRED -> wrapper.isNotNull(UserPasswordSecurity::getLockTime)
                    .le(UserPasswordSecurity::getLockTime, now);
            case COUNTING -> wrapper.isNull(UserPasswordSecurity::getLockTime)
                    .gt(UserPasswordSecurity::getPasswordErrorCount, 0);
        }
    }

    /// 解析状态编码, 不匹配返回 null
    private LoginLockStatusEnum parseStatus(String status) {
        if (StrUtil.isBlank(status)) {
            return null;
        }
        for (LoginLockStatusEnum statusEnum : LoginLockStatusEnum.values()) {
            if (statusEnum.getCode().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }

    /// 填充状态与剩余分钟
    private void fillComputedFields(LoginLockResult result, OffsetDateTime now) {
        if (result.getLockTime() != null) {
            if (result.getLockTime().isAfter(now)) {
                result.setStatus(LoginLockStatusEnum.LOCKED.getCode());
                // 剩余分钟向上取整, 与 LoginRetryService#checkLockedState 的提示口径一致
                result.setRemainingMinutes(Math.max(1, Duration.between(now, result.getLockTime()).toMinutes() + 1));
            } else {
                // 已到期: 下次登录/验证时自动清除, 解锁按钮可立即清掉残留
                result.setStatus(LoginLockStatusEnum.EXPIRED.getCode());
            }
        } else {
            result.setStatus(LoginLockStatusEnum.COUNTING.getCode());
        }
    }
}
