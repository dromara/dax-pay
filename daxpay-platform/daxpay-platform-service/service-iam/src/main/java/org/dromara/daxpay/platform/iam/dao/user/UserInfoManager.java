package org.dromara.daxpay.platform.iam.dao.user;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpIdEntity;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpRealDelEntity;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.iam.entity.user.UserInfo;
import org.dromara.daxpay.platform.iam.param.user.UserInfoQuery;
import org.dromara.daxpay.platform.capability.auth.util.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/// # 用户信息
///
@Repository
@RequiredArgsConstructor
public class UserInfoManager extends BaseManager<UserInfoMapper, UserInfo> {

    public boolean existsByAccount(String account) {
        return existedByField(UserInfo::getAccount, account);
    }

    public boolean existsByAccount(String account, Long id) {
        return existedByField(UserInfo::getAccount, account, id);
    }

    /// 按终端+账号校验是否存在（终端维度唯一性）
    public boolean existsByClientCodeAndAccount(String clientCode, String account) {
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getAccount, account)
                .exists();
    }

    /// 按终端+账号校验是否存在，排除指定用户ID（编辑时防重）
    public boolean existsByClientCodeAndAccount(String clientCode, String account, Long excludeId) {
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getAccount, account)
                .ne(MpIdEntity::getId, excludeId)
                .exists();
    }

    public boolean existsByEmail(String email) {
        return existedByField(UserInfo::getEmail, email);
    }

    public boolean existsByEmail(String email, Long id) {
        return existedByField(UserInfo::getEmail, email, id);
    }

    /// 按终端+邮箱校验是否存在（终端维度唯一性）
    public boolean existsByClientCodeAndEmail(String clientCode, String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getEmail, email)
                .exists();
    }

    /// 按终端+邮箱校验是否存在，排除指定用户ID（编辑时防重）
    public boolean existsByClientCodeAndEmail(String clientCode, String email, Long excludeId) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getEmail, email)
                .ne(MpIdEntity::getId, excludeId)
                .exists();
    }

    public boolean existsByPhone(String phone) {
        return existedByField(UserInfo::getPhone, phone);
    }

    public boolean existsByPhone(String phone, Long id) {
        return existedByField(UserInfo::getPhone, phone, id);
    }

    /// 按终端+手机号校验是否存在（终端维度唯一性）
    public boolean existsByClientCodeAndPhone(String clientCode, String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getPhone, phone)
                .exists();
    }

    /// 按终端+手机号校验是否存在，排除指定用户ID（编辑时防重）
    public boolean existsByClientCodeAndPhone(String clientCode, String phone, Long excludeId) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getPhone, phone)
                .ne(MpIdEntity::getId, excludeId)
                .exists();
    }

    public Optional<UserInfo> findByAccount(String account) {
        return findByField(UserInfo::getAccount, account);
    }

    /// 按终端+账号查询用户（登录认证用）
    public Optional<UserInfo> findByClientCodeAndAccount(String clientCode, String account) {
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getAccount, account)
                .oneOpt();
    }

    public Optional<UserInfo> findByEmail(String email) {
        return findByField(UserInfo::getEmail, email);
    }

    public Optional<UserInfo> findByPhone(String phone) {
        return findByField(UserInfo::getPhone, phone);
    }

    /// 按终端+手机号查询用户（商户注册/找回密码用）
    public Optional<UserInfo> findByClientCodeAndPhone(String clientCode, String phone) {
        return lambdaQuery()
                .eq(UserInfo::getClientCode, clientCode)
                .eq(UserInfo::getPhone, phone)
                .oneOpt();
    }

    /// 管理员用户不显示
    public Page<UserInfo> page(PageParam pageParam, UserInfoQuery query) {
        Page<UserInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<UserInfo> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(UserInfo::isAdministrator), false);
        return this.page(mpPage, generator);
    }

    public void setUpStatus(Long userId, String status) {
        lambdaUpdate()
                .eq(MpIdEntity::getId, userId)
                .set(UserInfo::getStatus, status)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /// 批量更新用户状态
    public void setUpStatusBatch(List<Long> userIds, String status) {
        lambdaUpdate()
                .in(MpIdEntity::getId, userIds)
                .set(UserInfo::getStatus, status)
                .set(UserInfo::getLastModifiedTime, LocalDateTime.now())
                .set(UserInfo::getLastModifier, SecurityUtil.getUserIdOrDefaultId())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /// 批量重置用户密码
    public void restartPasswordBatch(List<Long> userIds,String password){
        lambdaUpdate()
                .in(MpIdEntity::getId, userIds)
                .set(UserInfo::getPassword, password)
                .set(UserInfo::getLastModifiedTime, LocalDateTime.now())
                .set(UserInfo::getLastModifier, SecurityUtil.getUserIdOrDefaultId())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

}
