package cn.bootx.platform.iam.service.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.code.UserStatusCode;
import cn.bootx.platform.iam.dao.user.UserExpandInfoManager;
import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.user.UserExpandInfo;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 超级管理员操作类
 *
 * @author xxm
 * @since 2021/9/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserInfoManager userInfoManager;

    private final UserExpandInfoManager userExpandInfoManager;

    private final UserQueryService userQueryService;


    /**
     * 分页查询
     */
    public PageResult<UserInfoResult> page(PageParam pageParam, UserInfoParam userInfoParam) {
        return MpUtil.toPageResult(userInfoManager.page(pageParam, userInfoParam));
    }

    /**
     * 封禁用户
     */
    public void ban(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusCode.BAN);
    }

    /**
     * 批量封禁用户
     */
    public void banBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusCode.BAN);
    }

    /**
     * 锁定用户
     */
    public void lock(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusCode.LOCK);
    }

    /**
     * 批量锁定用户
     */
    public void lockBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusCode.LOCK);
    }

    /**
     * 解锁用户
     */
    public void unlock(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusCode.NORMAL);
    }

    /**
     * 批量解锁用户
     */
    public void unlockBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusCode.NORMAL);
    }

    /**
     * 添加新用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(UserInfoParam userInfoParam) {
        if (userQueryService.existsAccount(userInfoParam.getAccount())) {
            throw new BizException("账号已存在");
        }
        // 注册时间
        UserInfo userInfo = UserInfo.init(userInfoParam);
        userInfo.setAdministrator(false)
                .setStatus(UserStatusCode.NORMAL)
                .setPassword(BCrypt.hashpw(userInfo.getPassword()));
        userInfoManager.save(userInfo);
        // 扩展信息
        UserExpandInfo userExpandInfo = new UserExpandInfo()
                .setRegisterTime(LocalDateTime.now());
        userExpandInfo.setId(userInfo.getId());
        userExpandInfoManager.save(userExpandInfo);
    }

    /**
     * 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPassword(Long userId, String newPassword) {

        UserInfo userInfo = userInfoManager.findById(userId).orElseThrow(UserInfoNotExistsException::new);
        // 新密码进行加密
        newPassword = BCrypt.hashpw(newPassword);
        userInfo.setPassword(newPassword);
        userInfoManager.updateById(userInfo);
    }

    /**
     * 批量重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPasswordBatch(List<Long> userIds, String newPassword){
        // 新密码进行加密
        String password = BCrypt.hashpw(newPassword);
        userInfoManager.restartPasswordBatch(userIds,password);
    }

    /**
     * 编辑用户信息
     */
    public void update(UserInfoParam userInfoParam) {
        UserInfo userInfo = userInfoManager.findById(userInfoParam.getId())
                .orElseThrow(UserInfoNotExistsException::new);
        userInfoParam.setPassword(null);
        BeanUtil.copyProperties(userInfoParam, userInfo, CopyOptions.create().ignoreNullValue());
        userInfoManager.updateById(userInfo);
    }
}
