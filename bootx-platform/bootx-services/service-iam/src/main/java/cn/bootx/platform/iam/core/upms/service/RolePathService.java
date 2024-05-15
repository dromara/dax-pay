package cn.bootx.platform.iam.core.upms.service;

import cn.bootx.platform.common.core.annotation.CountTime;
import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.iam.core.permission.service.PermPathService;
import cn.bootx.platform.iam.core.role.dao.RoleManager;
import cn.bootx.platform.iam.core.role.entity.Role;
import cn.bootx.platform.iam.core.role.service.RoleService;
import cn.bootx.platform.iam.core.upms.dao.RolePathManager;
import cn.bootx.platform.iam.core.upms.entity.RolePath;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.dto.permission.PermPathDto;
import cn.bootx.platform.iam.dto.role.RoleDto;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.USER_PATH;

/**
 * 角色请求权限关系
 *
 * @author xxm
 * @since 2021/6/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RolePathService {

    private final RolePathManager rolePathManager;

    private final PermPathService pathService;

    private final RoleService roleService;

    private final RoleManager roleManager;

    private final UserInfoManager userInfoManager;

    private final UserRoleService userRoleService;

    /**
     * 保存角色路径授权
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_PATH }, allEntries = true)
    @CountTime
    public void addRolePath(Long roleId, List<Long> permissionIds,boolean updateAddChildren) {
        // 先删后增
        List<RolePath> rolePaths = rolePathManager.findAllByRole(roleId);
        List<Long> rolePathIds = rolePaths.stream().map(RolePath::getPermissionId).collect(Collectors.toList());
        // 需要删除的请求权限
        List<RolePath> deleteRolePaths = rolePaths.stream()
                .filter(rolePath -> !permissionIds.contains(rolePath.getPermissionId()))
                .collect(Collectors.toList());
        // 需要删除的关联ID
        List<Long> deleteIds = deleteRolePaths.stream().map(RolePath::getId).collect(Collectors.toList());
        rolePathManager.deleteByIds(deleteIds);

        // 需要新增的权限关系
        List<RolePath> addRolePath = permissionIds.stream()
                .filter(id -> !rolePathIds.contains(id))
                .map(permissionId -> new RolePath(roleId, permissionId))
                .collect(Collectors.toList());
        // 新增时验证是否超过了父级角色所拥有的权限
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        if (Objects.nonNull(role.getPid())){
            List<Long> collect = rolePathManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RolePath::getPermissionId)
                    .collect(Collectors.toList());
            addRolePath = addRolePath.stream()
                    .filter(o->collect.contains(o.getPermissionId()))
                    .collect(Collectors.toList());
        }
        rolePathManager.saveAll(addRolePath);

        // 级联更新子孙角色
        List<Long> deletePermIds = deleteRolePaths.stream().map(RolePath::getPermissionId).collect(Collectors.toList());
        if (updateAddChildren) {
            // 新增的进行追加
            List<Long> addPermIds = addRolePath.stream()
                    .map(RolePath::getPermissionId)
                    .collect(Collectors.toList());
            this.updateChildren(roleId, addPermIds, deletePermIds);
        } else {
            // 新增的不进行追加
            this.updateChildren(roleId, null, deletePermIds);
        }
    }

    /**
     * 更新子孙角色关联关系
     */
    private void updateChildren(Long roleId, List<Long> addPermIds, List<Long> deletePermIds) {
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(deletePermIds)){
            return;
        }
        // 当前角色的子孙角色
        List<RoleDto> children = roleService.findChildren(roleId);
        // 新增
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(children)){
            List<RolePath> addRolePaths = new ArrayList<>();
            for (Long addPermId : addPermIds) {
                for (RoleDto childrenRole : children) {
                    addRolePaths.add(new RolePath(childrenRole.getId(), addPermId));
                }
            }
            rolePathManager.saveAll(addRolePaths);
        }
        // 删除
        if (CollUtil.isNotEmpty(deletePermIds) && CollUtil.isNotEmpty(children)) {
            // 子孙角色
            List<Long> childrenIds = children.stream()
                    .map(BaseDto::getId)
                    .collect(Collectors.toList());
            for (Long childrenId : childrenIds) {
                rolePathManager.deleteByPermIds(childrenId,deletePermIds);
            }
        }
    }

    /**
     * 查询用户查询拥有的请求权限信息
     */
    public List<PermPathDto> findPathsByUser() {
        Long userId = SecurityUtil.getUserId();
        return this.findPathsByUser(userId);
    }

    /**
     * 根据角色id获取关联权限id
     */
    public List<Long> findIdsByRole(Long roleId) {
        List<RolePath> rolePermissions = rolePathManager.findAllByRole(roleId);
        return rolePermissions.stream().map(RolePath::getPermissionId).collect(Collectors.toList());
    }

    /**
     * 查询用户拥有的路径权限信息( 路径路由拦截使用 )
     */
    @Cacheable(value = USER_PATH, key = "#method+':'+#userId")
    public List<String> findSimplePathsByUser(String method, Long userId) {
        return SpringUtil.getBean(this.getClass())
                .findPathsByUser(userId)
                .stream()
                .filter(permPathDto -> Objects.equals(method, permPathDto.getRequestType()))
                .map(PermPathDto::getPath)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户拥有的路径权限信息
     */
    public List<PermPathDto> findPathsByUser(Long userId) {
        UserInfo userInfo = userInfoManager.findById(userId).orElseThrow(UserInfoNotExistsException::new);
        List<PermPathDto> paths;
        if (userInfo.isAdministrator()) {
            paths = pathService.findAll();
        }
        else {
            paths = this.findPermissionsByUser(userId);
        }
        return paths;
    }

    /**
     * 查询用户查询拥有的权限信息
     */
    private List<PermPathDto> findPermissionsByUser(Long userId) {
        List<PermPathDto> permissions = new ArrayList<>(0);

        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return permissions;
        }
        List<RolePath> rolePaths = rolePathManager.findAllByRoles(roleIds);
        List<Long> permissionIds = rolePaths.stream()
                .map(RolePath::getPermissionId)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(permissionIds)) {
            permissions = pathService.findByIds(permissionIds);
        }
        return permissions;
    }

    /**
     * 获取当前用户角色下可见的请求权限
     * 如果是顶级角色, 查询到的是当前角色拥有的权限
     * 如果是子角色, 查询到父级角色分配的权限，范围不会超过父级角色拥有的权限
     */
    public List<PermPathDto> findPathsByRole(Long roleId) {
        List<PermPathDto> permPaths = pathService.findAll();
        Role role = roleManager.findById(roleId)
                .orElseThrow(RoleNotExistedException::new);
        // 如果有有父级角色, 进行过滤筛选, 防止越权
        if (Objects.nonNull(role.getPid())){
            List<Long> permissionIds = rolePathManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RolePath::getPermissionId)
                    .collect(Collectors.toList());
            permPaths = permPaths.stream()
                    .filter(o->permissionIds.contains(o.getId()))
                    .collect(Collectors.toList());
        }
        return permPaths;
    }
}
