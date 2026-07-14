package cn.daxpay.open.platform.iam.service.upms;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.upms.UserRoleManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.entity.upms.UserRole;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.result.role.RoleResult;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;

/// # 用户角色关系
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final RoleManager roleManager;

    private final UserInfoManager userInfoManager;

    private final UserRoleManager userRoleManager;

    /// 给用户分配角色（单角色模式）
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(Long userId, Long roleId, boolean ignoreScopes) {
        // 校验用户与角色的终端一致性
        this.validateUserRoleTerminalConsistency(userId, roleId);

        // 判断是否越权
        if (!ignoreScopes){
            List<Long> roleIdsByUser = this.findRoleIdsByUser();
            if (!roleIdsByUser.contains(roleId)){
                // 权限: 角色分配超出了可分配的范围
                throw new ValidationFailedException("error.iam.role.assignOutOfScope");
            }
        }

        // 先删除用户拥有的角色
        userRoleManager.deleteByUser(userId);
        // 然后给用户添加角色
        UserRole userRole = new UserRole(userId, roleId);
        userRoleManager.save(userRole);
    }

    /// 批量给用户分配角色（单角色模式）
    @Transactional(rollbackFor = Exception.class)
    public void saveAssignBatch(List<Long> userIds, Long roleId) {
        List<Long> roleIdsByUser = this.findRoleIdsByUser();
        if (!roleIdsByUser.contains(roleId)){
            // 权限: 角色分配超出了可分配的范围
            throw new ValidationFailedException("error.iam.role.assignOutOfScope");
        }
        List<UserInfo> userInfos = userInfoManager.findAllByIds(userIds);
        if (userInfos.size() != userIds.size()) {
            // 权限: 用户数据有问题
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.dataError");
        }
        // 校验用户与角色的终端一致性
        for (UserInfo userInfo : userInfos) {
            this.validateUserRoleTerminalConsistency(userInfo.getId(), roleId);
        }
        userRoleManager.deleteByUsers(userIds);
        List<UserRole> userRoles = userIds.stream()
            .map(userId -> new UserRole(userId, roleId))
            .toList();
        userRoleManager.saveAll(userRoles);
    }

    /// 查询用户可分配的角色列表
    public List<RoleResult> findAssignableRolesByUser(Long userId) {
        UserInfo userInfo = userInfoManager.findById(userId)
                // 权限: 用户不存在
                .orElseThrow(() -> new DataNotExistException("error.iam.user.notExist"));
        return MpUtil.toListResult(roleManager.findAllByClientCode(userInfo.getClientCode()));
    }

    /// 校验用户与角色的终端一致性
    private void validateUserRoleTerminalConsistency(Long userId, Long roleId) {
        UserInfo userInfo = userInfoManager.findById(userId)
                // 权限: 用户不存在
                .orElseThrow(() -> new DataNotExistException("error.iam.user.notExist"));
        String userClientCode = userInfo.getClientCode();
        Role role = roleManager.findById(roleId)
                // 权限: 角色不存在
                .orElseThrow(() -> new DataNotExistException("error.iam.role.notExist"));
        if (!Objects.equals(userClientCode, role.getClientCode())) {
            // 权限: 禁止跨终端分配角色
            throw new ValidationFailedException(
                    "error.iam.role.assignCrossClient", userClientCode, role.getClientCode());
        }
    }

    /// 根据id查询角色id, 作缓存
    public List<Long> findRoleIdsByUser(Long userId) {
        UserRole userRole = userRoleManager.findOneByUser(userId);
        if (Objects.isNull(userRole) || Objects.isNull(userRole.getRoleId())) {
            return List.of();
        }
        return List.of(userRole.getRoleId());
    }

    /// 查询用户所对应的角色（单角色模式）
    public RoleResult findRolesByUser(Long userId) {
        UserRole userRole = userRoleManager.findOneByUser(userId);
        if (Objects.isNull(userRole) || Objects.isNull(userRole.getRoleId())) {
            return null;
        }
        return roleManager.findById(userRole.getRoleId())
                .map(Role::toResult)
                .orElse(null);
    }

    /// 查询用户关联的角色, 超级管理员返回全部
    private List<Long> findRoleIdsByUser() {
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()){
            return roleManager.findAll().stream().map(Role::getId).toList();
        } else {
            return findRoleIdsByUser(user.getId());
        }
    }

    /// 判断当前登录用户和指定角色是否为符合下列条件
    /// 1. 为超级管理员
    /// 2. 拥有当前角色
    /// 3. id为空, 说明是顶级角色, 只有超级管理员可以操作
    public boolean checkUserRole(Long roleId){
        // 为超级管理员
        UserDetail user = SecurityUtil.getUser();
        if (user.isAdmin()){
            return true;
        }
        if (Objects.isNull(roleId)){
            return false;
        }
        // 是否分配了该角色
        return userRoleManager.existsByUserRole(user.getId(), roleId);
    }
}

