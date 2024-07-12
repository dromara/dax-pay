package cn.bootx.platform.iam.service.service;

import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.bootx.platform.starter.auth.exception.UserNotFoundException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户信息查询服务
 *
 * @author xxm
 * @since 2022/6/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserInfoManager userInfoManager;

    /**
     * 账号是否存在
     */
    public boolean existsAccount(String account) {
        return userInfoManager.existsByAccount(account.trim());
    }

    /**
     * 账号是否存在
     */
    public boolean existsAccount(String account, Long id) {
        return userInfoManager.existsByAccount(account.trim(), id);
    }
    /**
     * 邮箱是否存在
     */
    public boolean existsEmail(String email) {
        if (StrUtil.isBlank(email)){
            return false;
        }
        return userInfoManager.existsByEmail(email.trim());
    }

    /**
     * 邮箱是否存在
     */
    public boolean existsEmail(String email, Long id) {
        if (StrUtil.isBlank(email)){
            return false;
        }
        return userInfoManager.existsByEmail(email.trim(), id);
    }

    /**
     * 手机是否存在
     */
    public boolean existsPhone(String phone) {
        if (StrUtil.isBlank(phone)){
            return false;
        }
        return userInfoManager.existsByPhone(phone);
    }

    /**
     * 手机是否存在
     */
    public boolean existsPhone(String phone, Long id) {
        if (StrUtil.isBlank(phone)){
            return false;
        }
        return userInfoManager.existsByPhone(phone.trim(), id);
    }


    /**
     * 根据用户id 获取 UserInfo
     */
    public UserInfoResult findById(Long id) {
        return userInfoManager.findById(id).map(UserInfo::toResult).orElseThrow(UserNotFoundException::new);
    }


    /**
     * 根据账号查询用户
     */
    public UserInfoResult findByAccount(String account) {
        return userInfoManager.findByAccount(account).map(UserInfo::toResult).orElseThrow(UserNotFoundException::new);
    }


}
