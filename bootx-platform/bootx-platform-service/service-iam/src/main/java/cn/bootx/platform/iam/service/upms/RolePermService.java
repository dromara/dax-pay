package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.RoleMenuManager;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.upms.RoleMenu;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.permission.PermMenuService;
import cn.bootx.platform.iam.service.role.RoleService;
import cn.bootx.platform.starter.auth.exception.NotLoginException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(Long roleId, String clientCode, List<Long> permissionIds, boolean updateAddChildren) {
        List<RoleMenu> RoleMenus = roleMenuManager.findAllByRoleAndClientCode(roleId, clientCode);
        List<Long> roleMenuIds = RoleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        // 需要删除的菜单
        List<RoleMenu> deleteRoleMenus = RoleMenus.stream()
                .filter(rolePath -> !permissionIds.contains(rolePath.getMenuId()))
                .toList();
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
                    .map(RoleMenu::getMenuId)
                    .collect(Collectors.toList());
            addRoleMenus = addRoleMenus.stream()
                    .filter(o->collect.contains(o.getMenuId()))
                    .collect(Collectors.toList());
        }
        roleMenuManager.saveAll(addRoleMenus);

        // 级联更新子孙角色
        List<Long> deletePermIds = deleteRoleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        if (updateAddChildren) {
            // 新增的进行追加
            List<Long> addPermIds = addRoleMenus.stream()
                    .map(RoleMenu::getMenuId)
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
        List<RoleResult> children = roleService.findChildren(roleId);
        // 新增
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(children)){
            List<RoleMenu> addRoleMenus = new ArrayList<>();
            for (Long addPermId : addPermIds) {
                for (RoleResult childrenRole : children) {
                    addRoleMenus.add(new RoleMenu(childrenRole.getId(), clientCode, addPermId));
                }
            }
            roleMenuManager.saveAll(addRoleMenus);
        }
        // 删除
        if (CollUtil.isNotEmpty(deletePermIds) && CollUtil.isNotEmpty(children)) {
            // 子孙角色
            List<Long> childrenIds = children.stream()
                    .map(RoleResult::getId)
                    .toList();
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
        return rolePermissions.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

    /**
     * 获取菜单权限树, 不包含资源权限(权限码)
     * 1. 用户为管理员, 返回所有菜单
     * 2. 如果用户为非管理员, 则返回当前用户角色下可见的菜单
     */
    public List<PermMenuResult> findMenuTree(String clientCode) {
        List<PermMenuResult> permissions = this.findPermissions(clientCode);
        List<PermMenuResult> permissionsByNotButton = permissions.stream()
                .filter(o -> !Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .collect(Collectors.toList());
        return this.recursiveBuildTree(permissionsByNotButton);
    }

    /**
     * 获取菜单和权限码树
     * 1. 用户为管理员, 返回所有菜单和资源权限(权限码)
     * 2. 如果用户为非管理员, 则返回当前用户角色下可见的菜单和资源权限(权限码)
     */
    public List<PermMenuResult> findMenuAndPermCodeTree(String clientCode){
        List<PermMenuResult> permissions = this.findPermissions(clientCode);
        return this.recursiveBuildTree(permissions);
    }

    /**
     * 获取当前用户角色下可见的菜单树, 包含菜单和资源权限(权限码)
     * 如果是顶级角色, 查询到的是当前角色拥有的权限
     * 如果是子角色, 查询到父级角色分配的权限，范围不会超过父级角色拥有的权限
     */
    public List<PermMenuResult> findTreeByRole(String clientCode, Long roleId) {
        List<PermMenuResult> permissions = this.findPermissions(clientCode);
        Role role = roleManager.findById(roleId)
                .orElseThrow(RoleNotExistedException::new);
        // 如果有有父级角色, 进行过滤筛选, 防止越权
        if (Objects.nonNull(role.getPid())){
            List<Long> permissionIds = roleMenuManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RoleMenu::getMenuId)
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
        List<PermMenuResult> permissions = this.findPermissions(clientCode);
        return permissions.stream()
                .filter(o -> !Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .map(PermMenuResult::getId)
                .collect(Collectors.toList());
    }

    /**
     * 获取菜单和资源权限(权限码) 根据用户进行筛选
     */
    public MenuAndResourceDto getPermissions(String clientCode) {
        List<PermMenuResult> permissions = this.findPermissions(clientCode);
        List<String> resourcePerms = permissions.stream()
                .filter(o -> Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .filter(PermMenuResult::isEffect)
                .map(PermMenuResult::getPermCode)
                .collect(Collectors.toList());
        List<PermMenuResult> menus = permissions.stream()
                .filter(o -> !Objects.equals(PermissionCode.MENU_TYPE_RESOURCE, o.getMenuType()))
                .collect(Collectors.toList());
        return new MenuAndResourceDto().setResourcePerms(resourcePerms).setMenus(this.recursiveBuildTree(menus));
    }

    /**
     * 获取权限信息列表
     */
    private List<PermMenuResult>  findPermissions(String clientCode) {
        UserDetail userDetail = SecurityUtil.getCurrentUser().orElseThrow(NotLoginException::new);
        List<PermMenuResult> permissions;

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
        List<PermMenuResult> permissions = this.findPermissionsByUser(userId);
        return permissions.stream()
                .filter(o -> Objects.equals(o.getMenuType(), PermissionCode.MENU_TYPE_RESOURCE))
                .filter(PermMenuResult::isEffect)
                .map(PermMenuResult::getPermCode)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户查询拥有的权限信息(直接获取所有终端的权限码),
     * 如果当前用户密码是否过期, 过期或者未修改密码, 返回权限为空
     */
    private List<PermMenuResult> findPermissionsByUser(Long userId) {
        // 判断当前用户密码是否过期, 过期或者未修改密码, 返回权限为空
        UserStatus userStatus = userStatusService.getUserStatus();
        if (userStatus.isExpirePassword() || userStatus.isInitialPassword()){
            return new ArrayList<>(0);
        }

        List<PermMenuResult> permissions = new ArrayList<>(0);
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return permissions;
        }
        List<RoleMenu> roleMenus = roleMenuManager.findAllByRoles(roleIds);
        List<Long> permissionIds = roleMenus.stream()
                .map(RoleMenu::getMenuId)
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
    private List<PermMenuResult> recursiveBuildTree(List<PermMenuResult> permissions) {
        return TreeBuildUtil.build(permissions, null, BaseDto::getId, PermMenuResult::getParentId,
                PermMenuResult::setChildren, (o1, o2) -> {
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
