package cn.daxpay.open.platform.iam.dao.user;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.user.UserPasswordSecurity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Objects;

/// # 用户密码安全信息
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserPasswordSecurityManager extends BaseManager<UserPasswordSecurityMapper, UserPasswordSecurity> {

    /// 根据用户ID查询
    public Optional<UserPasswordSecurity> findByUserId(Long userId) {
        return findById(userId);
    }

    /// 根据用户ID查询，不存在则创建默认记录
    ///
    /// 兜底补建一律不写初始密码标记: 该标记只由显式业务动作(建用户/管理员重置密码)写入,
    /// 否则历史用户(缺记录)会被误判为"使用初始密码"而被 40302 强制改密。
    public UserPasswordSecurity getOrCreateByUserId(Long userId) {
        return findById(userId).orElseGet(() -> {
            var security = new UserPasswordSecurity();
            security.setId(userId);
            security.setPasswordErrorCount(0);
            security.setInitialPassword(false);
            save(security);
            return security;
        });
    }

    /// 重置密码错误次数
    public void resetPasswordErrorCount(Long userId) {
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .set(UserPasswordSecurity::getPasswordErrorCount, 0)
                .update();
    }

    /// 增加密码错误次数
    ///
    /// 原子自增: 避免并发下读-改-写丢失计数(多个并发失败请求各读旧值后写, 计数停留在 1, 锁定永不触发)。
    public int incrementPasswordErrorCount(Long userId) {
        if (findById(userId).isEmpty()) {
            UserPasswordSecurity security = new UserPasswordSecurity();
            security.setId(userId);
            security.setPasswordErrorCount(1);
            // 兜底补建不写初始密码标记(理由见 [UserPasswordSecurityManager#getOrCreateByUserId]):
            // 历史用户登录时输错一次密码即被打上初始密码标记, 会被永久拦在改密页
            security.setInitialPassword(false);
            security.setLastFailureTime(OffsetDateTime.now(ZoneOffset.UTC));
            save(security);
            return 1;
        }
        // DB 级原子自增, 杜绝并发计数丢失
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .setSql("password_error_count = COALESCE(password_error_count, 0) + 1")
                .set(UserPasswordSecurity::getLastFailureTime, OffsetDateTime.now(ZoneOffset.UTC))
                .update();
        // setSql 不返回新值, 重新读取拿 DB 权威值(残余竞态最多偏小 1, 对锁定判断可接受)
        return findById(userId)
                .map(s -> Objects.isNull(s.getPasswordErrorCount()) ? 0 : s.getPasswordErrorCount())
                .orElse(0);
    }

    /// 锁定账号
    public void lockAccount(Long userId, OffsetDateTime lockTime) {
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .set(UserPasswordSecurity::getLockTime, lockTime)
                .update();
    }

    /// 解锁账号
    public void unlockAccount(Long userId) {
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .set(UserPasswordSecurity::getLockTime, null)
                .set(UserPasswordSecurity::getPasswordErrorCount, 0)
                .update();
    }

    /// 更新密码过期时间
    public void updatePasswordExpireTime(Long userId, OffsetDateTime expireTime) {
        // 兼容历史用户缺少密码安全记录的情况
        getOrCreateByUserId(userId);
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .set(UserPasswordSecurity::getPasswordExpireTime, expireTime)
                .set(UserPasswordSecurity::getLastChangePasswordTime, OffsetDateTime.now(ZoneOffset.UTC))
                .set(UserPasswordSecurity::getInitialPassword, false)
                .update();
    }

    /// 管理员重置密码后更新过期时间(初始密码标记保持 true, 强制用户首次登录自行改密)
    public void updatePasswordExpireTimeOnReset(Long userId, OffsetDateTime expireTime) {
        // 兼容历史用户缺少密码安全记录的情况
        getOrCreateByUserId(userId);
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .set(UserPasswordSecurity::getPasswordExpireTime, expireTime)
                .set(UserPasswordSecurity::getLastChangePasswordTime, OffsetDateTime.now(ZoneOffset.UTC))
                .set(UserPasswordSecurity::getInitialPassword, true)
                .update();
    }

    /// 重置失败计数和失败时间
    public void resetFailureCount(Long userId) {
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .set(UserPasswordSecurity::getPasswordErrorCount, 0)
                .set(UserPasswordSecurity::getLastFailureTime, null)
                .update();
    }

    /// 初始化用户密码安全信息（创建用户时调用）
    public void initPasswordSecurity(Long userId, OffsetDateTime passwordExpireTime) {
        var security = new UserPasswordSecurity();
        security.setId(userId);
        security.setPasswordErrorCount(0);
        security.setInitialPassword(true);
        security.setPasswordExpireTime(passwordExpireTime);
        security.setLastChangePasswordTime(OffsetDateTime.now(ZoneOffset.UTC));
        save(security);
    }

    /// 刷新密码过期时间(差异更新, 仅此一列)
    ///
    /// 供登录时按当前轮换策略"懒补算"使用。只写 [UserPasswordSecurity#getPasswordExpireTime] 一列,
    /// 不触碰初始密码标记与上次改密时间, 因此不能复用 [#updatePasswordExpireTime](会置初始密码标记为 false
    /// 并覆盖改密时间)或 [#updatePasswordExpireTimeOnReset](会置为 true)。
    ///
    /// 条件显式展开 NULL 分支: PG 中 `password_expire_time <> ?` 对 NULL 行返回 unknown 不会命中,
    /// 只写 `ne` 会让缺值的历史行永远补不上。
    public void refreshExpireTime(Long userId, OffsetDateTime expireTime) {
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .and(w -> {
                    if (Objects.isNull(expireTime)) {
                        // 关闭轮换: 仅清理仍残留的历史快照
                        w.isNotNull(UserPasswordSecurity::getPasswordExpireTime);
                    }
                    else {
                        w.isNull(UserPasswordSecurity::getPasswordExpireTime)
                                .or().ne(UserPasswordSecurity::getPasswordExpireTime, expireTime);
                    }
                })
                .set(UserPasswordSecurity::getPasswordExpireTime, expireTime)
                .update();
    }

    /// 更新初始密码标记
    public void updateInitialPassword(Long userId, boolean initialPassword) {
        // 兼容历史用户缺少密码安全记录的情况
        getOrCreateByUserId(userId);
        lambdaUpdate()
                .eq(UserPasswordSecurity::getId, userId)
                .set(UserPasswordSecurity::getInitialPassword, initialPassword)
                .update();
    }
}
