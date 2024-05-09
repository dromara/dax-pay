package cn.bootx.platform.iam.core.user.service;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.function.UserDetailService;
import cn.bootx.platform.starter.auth.exception.UserNotFoundException;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户信息查询服务
 *
 * @author xxm
 * @since 2022/6/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService implements UserDetailService {

    private final UserInfoManager userInfoManager;

    private final UserAssistService userAssistService;

    /**
     * 账号是否存在
     */
    public boolean existsUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return false;
        }
        return userInfoManager.existsByUsername(username.trim());
    }

    /**
     * 账号是否存在
     */
    public boolean existsUsername(String username, Long id) {
        return userInfoManager.existsByUsername(username.trim(), id);
    }

    /**
     * 邮箱是否存在
     */
    public boolean existsEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return false;
        }
        return userInfoManager.existsByEmail(email.trim());
    }

    /**
     * 邮箱是否存在
     */
    public boolean existsEmail(String email, Long id) {
        return userInfoManager.existsByEmail(email.trim(), id);
    }

    /**
     * 手机是否存在
     */
    public boolean existsPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        return userInfoManager.existsByPhone(phone);
    }

    /**
     * 手机是否存在
     */
    public boolean existsPhone(String phone, Long id) {
        return userInfoManager.existsByPhone(phone.trim(), id);
    }

    /**
     * 根据用户id 获取 UserInfo
     */
    public UserInfoDto findById(Long id) {
        return userInfoManager.findById(id).map(UserInfo::toDto).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 根据账号查询用户
     */
    public UserInfoDto findByAccount(String account) {
        return userInfoManager.findByUsername(account).map(UserInfo::toDto).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 根据邮箱查询用户
     */
    public UserInfoDto findByEmail(String email) {
        return userInfoManager.findByEmail(email).map(UserInfo::toDto).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 根据手机号查询用户
     */
    public UserInfoDto findByPhone(String phone) {
        return userInfoManager.findByPhone(phone).map(UserInfo::toDto).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 根据手机验证码查询账号
     */
    public String findUsernameByPhoneCaptcha(String phone, String captcha) {
        if (!userAssistService.validatePhoneForgetCaptcha(phone, captcha)) {
            throw new BizException("验证码错误");
        }
        return userInfoManager.findByPhone(phone).map(UserInfo::getUsername).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<UserDetail> findByUserId(Long userId) {
        return userInfoManager.findById(userId).map(UserInfo::toUserDetail);
    }

}
