package cn.bootx.platform.iam.core.upms.service;

import cn.bootx.platform.common.core.annotation.NestedPermission;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.common.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.code.PermissionCode;
import cn.bootx.platform.iam.core.permission.service.PermMenuService;
import cn.bootx.platform.iam.core.role.dao.RoleManager;
import cn.bootx.platform.iam.core.role.entity.Role;
import cn.bootx.platform.iam.core.role.service.RoleService;
import cn.bootx.platform.iam.core.upms.dao.RoleMenuManager;
import cn.bootx.platform.iam.core.upms.entity.RoleMenu;
import cn.bootx.platform.iam.dto.permission.PermMenuDto;
import cn.bootx.platform.iam.dto.role.RoleDto;
import cn.bootx.platform.iam.dto.upms.MenuAndResourceDto;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.starter.auth.entity.UserStatus;
import cn.bootx.platform.starter.auth.exception.NotLoginException;
import cn.bootx.platform.starter.auth.service.UserStatusService;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
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

import static cn.bootx.platform.iam.code.CachingCode.USER_PERM_CODE;

/**
 * 角色权限菜单关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RolePermService {

    private final UserStatusService userStatusService;

    private final RoleService roleService;

    private final RoleManager roleManager;

    private final RoleMenuManager roleMenuManager;

    private final UserRoleService userRoleService;

    private final PermMenuService permMenuService;

    /**
     * 保存角色菜单(权限码)授权
     * 如果删除角色关门的权限关系, 将会级联删除子孙角色的权限关系
     * 新增角色权限关系, 会根据 updateAddChildren状态 来决定是否级联新增子孙角色的权限关系
     */
    @CacheEvict(value = { USER_PERM_CODE }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenu(Long roleId, String clientCode, List<Long> permissionIds, boolean updateAddChildren) {
        List<RoleMenu> RoleMenus = roleMenuManager.findAllByRoleAndClientCode(roleId, clientCode);
        List<Long> roleMenuIds = RoleMenus.stream().map(RoleMenu::getPermissionId).collect(Collectors.toList());
        // 需要删除的菜单
        List<RoleMenu> deleteRoleMenus = RoleMenus.stream()
                .filter(rolePath -> !permissionIds.contains(rolePath.getPermissionId()))
                .collect(Collectors.toList());
        // 需要删除的关联ID
        List<Long> deleteRoleMenuIds = deleteRoleMenus.stream().map(RoleMenu::getId).collect(Collectors.toList());
        roleMenuManager.deleteByIds(deleteRoleMenuIds);

        // 需要新增的角色权限关系
        List<RoleMenu> addRoleMenus = permissionIds.stream()
                .filter(id -> !roleMenuIds.contains(id))
                .map(permissionId -> new RoleMenu(roleId, clientCode, permissionId))
                .collect(Collectors.toList());
        // 新增时验证是否超过了父级角色所拥有的权限
        Role role = roleManager.findById(roleId)
                .orElseThrow(RoleNotExistedException::new);
        if (Objects.nonNull(role.getPid())){
            List<Long> collect = roleMenuManager.findAllByRoleAndClientCode(role.getPid(), clientCode)
                    .stream()
                    .map(RoleMenu::getPermissionId)
                    .collect(Collectors.toList());
            addRoleMenus = addRoleMenus.stream()
                    .filter(o->collect.contains(o.getPermissionId()))
                    .collect(Collectors.toList());
        }
        roleMenuManager.saveAll(addRoleMenus);

        // 级联更新子孙角色
        List<Long> deletePermIds = deleteRoleMenus.stream().map(RoleMenu::getPermissionId).collect(Collectors.toList());
        if (updateAddChildren) {
            // 新增的进行追加
            List<Long> addPermIds = addRoleMenus.stream()
                    .map(RoleMenu::getPermissionId)
                    .collect(Collectors.toList());
            this.updateChildren(roleId, clientCode, addPermIds, deletePermIds);
        } else {
            // 新增的不进行追加
            this.updateChildren(roleId, clientCode, null, deletePermIds);
        }
    }

    /**
     * 级联更新子孙角色关联关系
     */
    private void updateChildren(Long roleId, String clientCode, List<Long> addPermIds, List<Long> deletePermIds){
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(deletePermIds)){
            return;
        }
        // 当前角色的子孙角色
        List<RoleDto> children = roleService.findChildren(roleId);
        // 新增
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(children)){
            List<RoleMenu> addRoleMenus = new ArrayList<>();
            for (Long addPermId : addPermIds) {
                for (RoleDto childrenRole : children) {
                    addRoleMenus.add(new RoleMenu(childrenRole.getId(), clientCode, addPermId));
                }
            }
            roleMenuManager.saveAll(addRoleMenus);
        }
        // 删除
        if (CollUtil.isNotEmpty(deletePermIds) && CollUtil.isNotEmpty(children)) {
            // 子孙角色
            List<Long> childrenIds = children.stream()
                    .map(BaseDto::getId)
                    .collect(Collectors.toList());
            for (Long childrenId : childrenIds) {
                roleMenuManager.deleteByPermIds(childrenId,clientCode,deletePermIds);
            }
        }
    }

    /**
     * 查询当前角色已经选择的权限id
     */
    public List<Long> findPermissionIdsByRole(Long roleId, String clientCode) {
        List<RoleMenu> rolePermissions = roleMenuManager.findAllByRoleAndClientCode(roleId, clientCode);
        return rolePermissions.stream().map(RoleMenu::getPermissionId).collect(Collectors.toList());
    }

    /**
     * 获取菜单权限树, 不包含资源权限(权限码)
     * 1. 用户为管理员, 返回所有菜单
     * 2. 如果用户为非管理员, 则返回当前用户角色下可见的菜单
     */
    public List<PermMenuDto> findMenuTree(String clientCode) {
        List<PermMenuDto> permissions = this.findPermissions(clientCode);
        List<PermMenuDto> permissionsByNotButton = permissions.stream()
                .filter(o -> !Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .collect(Collectors.toList());
        return this.recursiveBuildTree(permissionsByNotButton);
    }

    /**
     * 获取菜单和权限码树
     * 1. 用户为管理员, 返回所有菜单和资源权限(权限码)
     * 2. 如果用户为非管理员, 则返回当前用户角色下可见的菜单和资源权限(权限码)
     */
    public List<PermMenuDto> findMenuAndPermCodeTree(String clientCode){
        List<PermMenuDto> permissions = this.findPermissions(clientCode);
        return this.recursiveBuildTree(permissions);
    }

    /**
     * 获取当前用户角色下可见的菜单树, 包含菜单和资源权限(权限码)
     * 如果是顶级角色, 查询到的是当前角色拥有的权限
     * 如果是子角色, 查询到父级角色分配的权限，范围不会超过父级角色拥有的权限
     */
    public List<PermMenuDto> findTreeByRole(String clientCode, Long roleId) {
        List<PermMenuDto> permissions = this.findPermissions(clientCode);
        Role role = roleManager.findById(roleId)
                .orElseThrow(RoleNotExistedException::new);
        // 如果有有父级角色, 进行过滤筛选, 防止越权
        if (Objects.nonNull(role.getPid())){
            List<Long> permissionIds = roleMenuManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RoleMenu::getPermissionId)
                    .collect(Collectors.toList());
            permissions = permissions.stream()
                    .filter(o->permissionIds.contains(o.getId()))
                    .collect(Collectors.toList());
        }
        return this.recursiveBuildTree(permissions);
    }

    /**
     * 获取权限菜单id列表,不包含资源权限(权限码)
     */
    public List<Long> findMenuIds(String clientCode) {
        List<PermMenuDto> permissions = this.findPermissions(clientCode);
        return permissions.stream()
                .filter(o -> !Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .map(PermMenuDto::getId)
                .collect(Collectors.toList());
    }

    /**
     * 获取菜单和资源权限(权限码) 根据用户进行筛选
     */
    public MenuAndResourceDto getPermissions(String clientCode) {
        List<PermMenuDto> permissions = this.findPermissions(clientCode);
        List<String> resourcePerms = permissions.stream()
                .filter(o -> Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .filter(PermMenuDto::isEffect)
                .map(PermMenuDto::getPermCode)
                .collect(Collectors.toList());
        List<PermMenuDto> menus = permissions.stream()
                .filter(o -> !Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .collect(Collectors.toList());
        return new MenuAndResourceDto().setResourcePerms(resourcePerms).setMenus(this.recursiveBuildTree(menus));
    }

    /**
     * 获取权限信息列表
     */
    private List<PermMenuDto>  findPermissions(String clientCode) {
        UserDetail userDetail = SecurityUtil.getCurrentUser().orElseThrow(NotLoginException::new);
        List<PermMenuDto> permissions;

        // 系统管理员，获取全部的权限
        if (userDetail.isAdmin()) {
            permissions = permMenuService.findAllByClientCode(clientCode);
        } else {
            // 非管理员获取自身拥有的权限
            permissions = this.findPermissionsByUser(userDetail.getId())
                    .stream()
                    .filter(o -> Objects.equals(clientCode, o.getClientCode()))
                    .collect(Collectors.toList());
        }
        return permissions;
    }

    /**
     * 获取有效的资源(权限码)列表(后端使用,直接获取所有终端的权限码)
     */
    @Cacheable(value = USER_PERM_CODE, key = "#userId")
    @NestedPermission
    public List<String> findEffectPermCodesByUserId(Long userId) {
        // 获取关联的的权限码
        List<PermMenuDto> permissions = this.findPermissionsByUser(userId);
        return permissions.stream()
                .filter(o -> Objects.equals(o.getMenuType(), PermissionCode.MENU_TYPE_RESOURCE))
                .filter(PermMenuDto::isEffect)
                .map(PermMenuDto::getPermCode)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户查询拥有的权限信息(直接获取所有终端的权限码),
     * 如果当前用户密码是否过期, 过期或者未修改密码, 返回权限为空
     */
    private List<PermMenuDto> findPermissionsByUser(Long userId) {
        // 判断当前用户密码是否过期, 过期或者未修改密码, 返回权限为空
        UserStatus userStatus = userStatusService.getUserStatus();
        if (userStatus.isExpirePassword() || userStatus.isInitialPassword()){
            return new ArrayList<>(0);
        }

        List<PermMenuDto> permissions = new ArrayList<>(0);
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return permissions;
        }
        List<RoleMenu> roleMenus = roleMenuManager.findAllByRoles(roleIds);
        List<Long> permissionIds = roleMenus.stream()
                .map(RoleMenu::getPermissionId)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(permissionIds)) {
            permissions = permMenuService.findByIds(permissionIds);
        }
        return permissions;
    }

    /**
     * 递归建树
     * @param permissions 查询出的菜单数据
     * @return 递归后的树列表
     */
    private List<PermMenuDto> recursiveBuildTree(List<PermMenuDto> permissions) {
        return TreeBuildUtil.build(permissions, null, BaseDto::getId, PermMenuDto::getParentId,
                PermMenuDto::setChildren, (o1, o2) -> {
                    // 先比较排序码
                    if (ObjectUtil.isAllNotEmpty(o1.getSortNo(),o2.getSortNo())) {
                        int compare = o1.getSortNo().compareTo(o2.getSortNo());
                        if (compare != 0) {
                            return compare;
                        }
                    }
                    // 后比较主键id
                    return o1.getId().compareTo(o2.getId());
                });

    }
}
