package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.UserRoleManager;
import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.upms.UserRole;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.role.RoleQueryService;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * 用户角色关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final RoleManager roleManager;

    private final RoleQueryService roleQueryService;

    private final UserInfoManager userInfoManager;

    private final UserRoleManager userRoleManager;

    /**
     * 给用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(Long userId, List<Long> roleIds) {
        // 判断是否越权
        List<RoleResult> roleTree = roleQueryService.tree();
        List<Long> roleIdsByUser = TreeBuildUtil.unfold(roleTree, RoleResult::getChildren)
                .stream()
                .distinct()
                .map(RoleResult::getId)
                .toList();
        if (!CollUtil.containsAll(roleIdsByUser, roleIds)){
            throw new ValidationFailedException("角色分配超出了可分配的范围");
        }

        // 先删除用户拥有的角色
        userRoleManager.deleteByUser(userId);
        // 然后给用户添加角色
        List<UserRole> userRoles = this.createUserRoles(userId, roleIds);
        userRoleManager.saveAll(userRoles);
    }

    /**
     * 给用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssignBatch(List<Long> userIds, List<Long> roleIds) {
        // 判断是否越权
        List<RoleResult> roleTree = roleQueryService.tree();
        List<Long> roleIdsByUser = TreeBuildUtil.unfold(roleTree, RoleResult::getChildren)
                .stream()
                .distinct()
                .map(RoleResult::getId)
                .toList();
        if (!CollUtil.containsAll(roleIdsByUser, roleIds)){
            throw new ValidationFailedException("角色分配超出了可分配的范围");
        }
        List<UserInfo> userInfos = userInfoManager.findAllByIds(userIds);
        if (userInfos.size() != userIds.size()) {
            throw new BizException("用户数据有问题");
        }
        userRoleManager.deleteByUsers(userIds);
        List<UserRole> userRoles = userIds.stream()
            .map(userId -> this.createUserRoles(userId, roleIds))
            .flatMap(Collection::stream)
            .toList();
        userRoleManager.saveAll(userRoles);
    }

    /**
     * 根据id查询角色id, 作缓存
     */
    public List<Long> findRoleIdsByUser(Long userId) {
        return userRoleManager.findAllByUser(userId)
            .stream()
            .map(UserRole::getRoleId)
            .distinct()
            .toList();
    }

    /**
     * 查询用户所对应的角色
     */
    public List<RoleResult> findRolesByUser(Long userId) {
        return MpUtil.toListResult(roleManager.findAllByIds(this.findRoleIdsByUser(userId)));
    }

    /**
     * 创建用户角色关联
     */
    private List<UserRole> createUserRoles(Long userId, List<Long> roleIds) {
        return roleIds.stream()
            .map(roleId -> new UserRole(userId, roleId))
            .toList();
    }

    /**
     * 判断当前登录用户和指定角色是否为符合下列条件
     * 1. 为超级管理员
     * 2. 拥有当前角色
     * 3. id为空, 说明是顶级角色, 只有超级管理员可以操作
     */
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
