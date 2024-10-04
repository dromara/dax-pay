package cn.bootx.platform.iam.service.service;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.iam.dao.user.UserExpandInfoManager;
import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.user.UserExpandInfo;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.iam.param.user.UserBaseInfoParam;
import cn.bootx.platform.iam.result.user.LoginAfterUserInfoResult;
import cn.bootx.platform.iam.result.user.UserBaseInfoResult;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户
 *
 * @author xxm
 * @since 2020/4/27 21:11
 */
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoManager userInfoManager;

    private final UserQueryService userQueryService;


    private final UserExpandInfoManager userExpandInfoManager;

    /**
     * 登录后获取用户信息
     */
    public LoginAfterUserInfoResult getLoginAfterUserInfo() {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        return new LoginAfterUserInfoResult().setAvatar(userExpandInfo.getAvatar())
            .setUserId(userInfo.getId())
            .setAccount(userInfo.getAccount())
            .setName(userInfo.getName());
    }

    /**
     * 获取用户安全信息
     */
    public UserInfoResult getUserSecurityInfo() {
        return userInfoManager.findById(SecurityUtil.getUserId())
            .map(UserInfo::toResult)
            .orElseThrow(UserInfoNotExistsException::new);
    }

    /**
     * 获取用户基本信息
     */
    public UserBaseInfoResult getUserBaseInfo() {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        return new UserBaseInfoResult().setId(userInfo.getId())
            .setSex(userExpandInfo.getSex())
            .setName(userInfo.getName())
            .setBirthday(userExpandInfo.getBirthday())
            .setAvatar(userExpandInfo.getAvatar());
    }

    /**
     * 修改基本信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserBaseInfo(UserBaseInfoParam param) {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        param.setId(null);
        BeanUtil.copyProperties(param, userExpandInfo, CopyOptions.create().ignoreNullValue());
        BeanUtil.copyProperties(param, userInfo, CopyOptions.create().ignoreNullValue());
        userExpandInfoManager.updateById(userExpandInfo);
        userInfoManager.updateById(userInfo);
    }

    /**
     * 修改密码
     * @param password 原密码
     * @param newPassword 新密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password, String newPassword) {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);

        // 判断原密码是否正确
        if (!BCrypt.checkpw(newPassword, userInfo.getPassword())) {
            throw new BizException("旧密码错误");
        }
        userInfo.setPassword(newPassword);
        userInfoManager.updateById(userInfo);
    }

}
