package cn.bootx.platform.iam.core.user.service;

import cn.bootx.platform.baseapi.core.captcha.service.CaptchaService;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.code.UserStatusCode;
import cn.bootx.platform.iam.core.client.dao.ClientManager;
import cn.bootx.platform.iam.core.security.password.service.PasswordChangeHistoryService;
import cn.bootx.platform.iam.core.upms.service.UserRoleService;
import cn.bootx.platform.iam.core.user.dao.UserExpandInfoManager;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserExpandInfo;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.dto.dept.DeptDto;
import cn.bootx.platform.iam.dto.role.RoleDto;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.bootx.platform.iam.dto.user.UserInfoWhole;
import cn.bootx.platform.iam.event.user.*;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.param.user.UserRegisterParam;
import cn.bootx.platform.starter.auth.util.PasswordEncoder;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private final UserRoleService userRoleService;

    private final UserDeptService userDeptService;

    private final PasswordEncoder passwordEncoder;

    private final UserQueryService userQueryService;

    private final CaptchaService captchaService;

    private final ClientManager clientManager;

    private final PasswordChangeHistoryService passwordChangeHistoryService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 分页查询
     */
    public PageResult<UserInfoDto> page(PageParam pageParam, UserInfoParam userInfoParam) {
        return MpUtil.convert2DtoPageResult(userInfoManager.page(pageParam, userInfoParam));
    }

    /**
     * 封禁用户
     */
    public void ban(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusCode.BAN);
        eventPublisher.publishEvent(new UserDeactivateEvent(this, UserStatusCode.BAN, userId));
    }

    /**
     * 批量封禁用户
     */
    public void banBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusCode.BAN);
        eventPublisher.publishEvent(new UserDeactivateEvent(this, UserStatusCode.BAN, userIds));
    }

    /**
     * 锁定用户
     */
    public void lock(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusCode.LOCK);
        eventPublisher.publishEvent(new UserDeactivateEvent(this, UserStatusCode.LOCK, userId));
    }

    /**
     * 批量锁定用户
     */
    public void lockBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusCode.LOCK);
        eventPublisher.publishEvent(new UserDeactivateEvent(this, UserStatusCode.LOCK, userIds));
    }

    /**
     * 解锁用户
     */
    public void unlock(Long userId) {
        userInfoManager.setUpStatus(userId, UserStatusCode.NORMAL);
        eventPublisher.publishEvent(new UserUnlockEvent(this, userId));
    }

    /**
     * 批量解锁用户
     */
    public void unlockBatch(List<Long> userIds) {
        userInfoManager.setUpStatusBatch(userIds, UserStatusCode.NORMAL);
        eventPublisher.publishEvent(new UserUnlockEvent(this, userIds));
    }

    /**
     * 注册新用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterParam param) {
        // 验证
        if (!captchaService.validateImgCaptcha(param.getCaptchaKey(), param.getCaptcha())) {
            throw new BizException("验证码错误");
        }
        UserInfoParam userInfoParam = new UserInfoParam();
        BeanUtil.copyProperties(param, userInfoParam);
        userInfoParam.setName(param.getUsername());
        // 添加默认注册就有权限的终端
        List<Long> clientIds = clientManager.findAllByDefaultEndow(true)
                .stream()
                .map(MpIdEntity::getId)
                .collect(Collectors.toList());
        userInfoParam.setClientIds(clientIds);
        this.add(userInfoParam);
    }

    /**
     * 添加新用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(UserInfoParam userInfoParam) {
        if (userQueryService.existsUsername(userInfoParam.getUsername())) {
            throw new BizException("账号已存在");
        }
        if (userQueryService.existsEmail(userInfoParam.getEmail())) {
            throw new BizException("邮箱已存在");
        }
        if (userQueryService.existsPhone(userInfoParam.getPhone())) {
            throw new BizException("手机号已存在");
        }
        // 注册时间
        UserInfo userInfo = UserInfo.init(userInfoParam);
        userInfo.setAdministrator(false)
                .setStatus(UserStatusCode.NORMAL)
                .setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoManager.save(userInfo);
        // 扩展信息
        UserExpandInfo userExpandInfo = new UserExpandInfo()
                .setRegisterTime(LocalDateTime.now());
        userExpandInfo.setId(userInfo.getId());
        passwordChangeHistoryService.saveChangeHistory(userInfo.getId(), userInfo.getPassword());
        userExpandInfoManager.save(userExpandInfo);
        eventPublisher.publishEvent(new UserCreateEvent(this, userInfo.toDto()));
    }

    /**
     * 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPassword(Long userId, String newPassword) {

        UserInfo userInfo = userInfoManager.findById(userId).orElseThrow(UserInfoNotExistsException::new);
        // 新密码进行加密
        newPassword = passwordEncoder.encode(newPassword);
        userInfo.setPassword(newPassword);
        passwordChangeHistoryService.saveChangeHistory(userInfo.getId(), userInfo.getPassword());
        userInfoManager.updateById(userInfo);
        eventPublisher.publishEvent(new UserRestartPasswordEvent(this, userInfo.getId()));
    }

    /**
     * 批量重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPasswordBatch(List<Long> userIds, String newPassword){
        // 新密码进行加密
        String password = passwordEncoder.encode(newPassword);
        userInfoManager.restartPasswordBatch(userIds,password);
        passwordChangeHistoryService.saveBatchChangeHistory(userIds, password);
        eventPublisher.publishEvent(new UserRestartPasswordEvent(this, userIds));
    }

    /**
     * 编辑用户信息
     */
    public void update(UserInfoParam userInfoParam) {
        UserInfo userInfo = userInfoManager.findById(userInfoParam.getId())
                .orElseThrow(UserInfoNotExistsException::new);
        userInfoParam.setPassword(null);
        BeanUtil.copyProperties(userInfoParam, userInfo, CopyOptions.create().ignoreNullValue());
        userInfo.setClientIds(userInfoParam.getClientIds());
        UserInfoDto userInfoDto = userInfoManager.updateById(userInfo).toDto();
        eventPublisher.publishEvent(new UserUpdateEvent(this, userInfoDto));
    }

    /**
     * 获取用户详情
     */
    public UserInfoWhole getUserInfoWhole(Long userId) {
        // 用户信息
        UserInfo userInfo = userInfoManager.findById(userId).orElseThrow(UserInfoNotExistsException::new);
        UserExpandInfo userExpandInfo = userExpandInfoManager.findById(userId)
                .orElseThrow(UserInfoNotExistsException::new);
        // 角色信息
        List<RoleDto> rolesByUser = userRoleService.findRolesByUser(userId);
        // 部门组织
        List<DeptDto> deptListByUser = userDeptService.findDeptListByUser(userId);
        return new UserInfoWhole().setUserInfo(userInfo.toDto())
                .setUserExpandInfo(userExpandInfo.toDto())
                .setRoles(rolesByUser)
                .setDeptList(deptListByUser);
    }

}
