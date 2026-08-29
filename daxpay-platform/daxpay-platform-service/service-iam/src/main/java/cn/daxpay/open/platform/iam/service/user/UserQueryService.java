package cn.daxpay.open.platform.iam.service.user;

import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.exception.auth.UserNotFoundException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 用户信息查询服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserInfoManager userInfoManager;

    /// 账号是否存在
    public boolean existsAccount(String account) {
        return userInfoManager.existsByAccount(account.trim());
    }

    /// 账号是否存在
    public boolean existsAccount(String account, Long id) {
        return userInfoManager.existsByAccount(account.trim(), id);
    }

    /// 按终端校验账号是否存在（终端维度唯一性）
    public boolean existsAccountByClientCode(String clientCode, String account) {
        return userInfoManager.existsByClientCodeAndAccount(clientCode, account.trim());
    }

    /// 按终端校验账号是否存在，排除当前用户（编辑时防重）
    public boolean existsAccountByClientCode(String clientCode, String account, Long excludeId) {
        return userInfoManager.existsByClientCodeAndAccount(clientCode, account.trim(), excludeId);
    }

    /// 邮箱是否存在
    public boolean existsEmail(String email) {
        if (StrUtil.isBlank(email)){
            return false;
        }
        return userInfoManager.existsByEmail(email.trim());
    }

    /// 邮箱是否存在
    public boolean existsEmail(String email, Long id) {
        if (StrUtil.isBlank(email)){
            return false;
        }
        return userInfoManager.existsByEmail(email.trim(), id);
    }

    /// 按终端校验邮箱是否存在（终端维度唯一性）
    public boolean existsEmailByClientCode(String clientCode, String email) {
        return userInfoManager.existsByClientCodeAndEmail(clientCode, email);
    }

    /// 按终端校验邮箱是否存在，排除当前用户（编辑时防重）
    public boolean existsEmailByClientCode(String clientCode, String email, Long excludeId) {
        return userInfoManager.existsByClientCodeAndEmail(clientCode, email, excludeId);
    }

    /// 根据用户id 获取 UserInfo
    public UserInfoResult findById(Long id) {
        return userInfoManager.findById(id).map(UserInfo::toResult).orElseThrow(UserNotFoundException::new);
    }

    /// 根据账号查询用户
    public UserInfoResult findByAccount(String account) {
        return userInfoManager.findByAccount(account).map(UserInfo::toResult).orElseThrow(UserNotFoundException::new);
    }

    /// 按终端+账号查询用户（登录认证用）
    public UserInfoResult findByClientCodeAndAccount(String clientCode, String account) {
        return userInfoManager.findByClientCodeAndAccount(clientCode, account)
                .map(UserInfo::toResult)
                .orElseThrow(UserNotFoundException::new);
    }

}
