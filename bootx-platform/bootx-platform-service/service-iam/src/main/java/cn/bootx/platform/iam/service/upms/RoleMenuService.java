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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色菜单菜单关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleMenuService {

//    private final UserStatusService userStatusService;

    private final RoleService roleService;

    private final RoleManager roleManager;

    private final RoleMenuManager roleMenuManager;

    private final UserRoleService userRoleService;

    private final PermMenuService permMenuService;

    /**
     * 保存角色菜单授权
     * 如果删除角色关门的菜单关系, 将会级联删除子孙角色的菜单关系
     * 新增角色菜单关系, 会根据 updateAddChildren状态 来决定是否级联新增子孙角色的菜单关系
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(Long roleId, String clientCode, List<Long> menuIds, boolean updateAddChildren) {
        List<RoleMenu> RoleMenus = roleMenuManager.findAllByRoleAndClientCode(roleId, clientCode);
        List<Long> roleMenuIds = RoleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        // 需要删除的菜单
        List<RoleMenu> deleteRoleMenus = RoleMenus.stream()
                .filter(rolePath -> !menuIds.contains(rolePath.getMenuId()))
                .toList();
        // 需要删除的关联ID
        List<Long> deleteRoleMenuIds = deleteRoleMenus.stream().map(RoleMenu::getId).collect(Collectors.toList());
        roleMenuManager.deleteByIds(deleteRoleMenuIds);

        // 需要新增的角色菜单关系
        List<RoleMenu> addRoleMenus = menuIds.stream()
                .filter(id -> !roleMenuIds.contains(id))
                .map(menuId -> new RoleMenu(roleId, clientCode, menuId))
                .collect(Collectors.toList());
        // 新增时验证是否超过了父级角色所拥有的菜单
        Role role = roleManager.findById(roleId)
                .orElseThrow(RoleNotExistedException::new);
        if (Objects.nonNull(role.getPid())){
            List<Long> collect = roleMenuManager.findAllByRoleAndClientCode(role.getPid(), clientCode)
                    .stream()
                    .map(RoleMenu::getMenuId)
                    .toList();
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
            // 新增的不对子孙进行追加
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
     * 查询当前角色已经选择的菜单id
     */
    public List<Long> findMenuIdsByRole(Long roleId, String clientCode) {
        List<RoleMenu> roleMenus = roleMenuManager.findAllByRoleAndClientCode(roleId, clientCode);
        return roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

    /**
     * 获取菜单菜单树
     * 1. 用户为管理员, 返回所有菜单
     * 2. 如果用户为非管理员, 则返回当前用户角色下可见的菜单
     */
    public List<PermMenuResult> findMenuTree(String clientCode) {
        List<PermMenuResult> menus = this.findMenus(clientCode);
        return this.recursiveBuildTree(menus);
    }

    /**
     * 获取当前用户角色下可见的菜单树
     * 如果是顶级角色, 查询到的是当前角色拥有的菜单
     * 如果是子角色, 查询到父级角色分配的菜单，范围不会超过父级角色拥有的菜单
     */
    public List<PermMenuResult> findTreeByRole(String clientCode, Long roleId) {
        List<PermMenuResult> menus = this.findMenus(clientCode);
        Role role = roleManager.findById(roleId)
                .orElseThrow(RoleNotExistedException::new);
        // 如果有有父级角色, 进行过滤筛选, 防止越权
        if (Objects.nonNull(role.getPid())){
            List<Long> menuIds = roleMenuManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RoleMenu::getMenuId)
                    .toList();
            menus = menus.stream()
                    .filter(o->menuIds.contains(o.getId()))
                    .collect(Collectors.toList());
        }
        return this.recursiveBuildTree(menus);
    }

    /**
     * 获取菜单菜单id列表
     */
    public List<Long> findMenuIds(String clientCode) {
        List<PermMenuResult> menus = this.findMenus(clientCode);
        return menus.stream()
                .map(PermMenuResult::getId)
                .collect(Collectors.toList());
    }

    /**
     * 获取菜单信息列表
     */
    private List<PermMenuResult> findMenus(String clientCode) {
        UserDetail userDetail = SecurityUtil.getCurrentUser().orElseThrow(NotLoginException::new);
        List<PermMenuResult> menus;

        // 系统管理员，获取全部的菜单
        if (userDetail.isAdmin()) {
            menus = permMenuService.findAllByClientCode(clientCode);
        } else {
            // 非管理员获取自身拥有的菜单
            menus = this.findMenusByUser(userDetail.getId())
                    .stream()
                    .filter(o -> Objects.equals(clientCode, o.getClientCode()))
                    .collect(Collectors.toList());
        }
        return menus;
    }


    /**
     * 查询用户查询拥有的菜单信息
     * 如果当前用户密码是否过期, 过期或者未修改密码, 返回菜单为空
     */
    private List<PermMenuResult> findMenusByUser(Long userId) {
        // 判断当前用户密码是否过期, 过期或者未修改密码, 返回菜单为空
//        UserStatus userStatus = userStatusService.getUserStatus();
//        if (userStatus.isExpirePassword() || userStatus.isInitialPassword()){
//            return new ArrayList<>(0);
//        }

        List<PermMenuResult> menus = new ArrayList<>(0);
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return menus;
        }
        List<RoleMenu> roleMenus = roleMenuManager.findAllByRoles(roleIds);
        List<Long> menuIds = roleMenus.stream()
                .map(RoleMenu::getMenuId)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(menuIds)) {
            menus = permMenuService.findByIds(menuIds);
        }
        return menus;
    }

    /**
     * 递归建树
     * @param menus 查询出的菜单数据
     * @return 递归后的树列表
     */
    private List<PermMenuResult> recursiveBuildTree(List<PermMenuResult> menus) {
        return TreeBuildUtil.build(menus, null, PermMenuResult::getId, PermMenuResult::getParentId,
                PermMenuResult::setChildren, Comparator.comparing(PermMenuResult::getSortNo));

    }
}
