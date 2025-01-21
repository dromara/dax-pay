package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.permission.PermMenuManager;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.RoleMenuManager;
import cn.bootx.platform.iam.entity.permission.PermMenu;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.upms.RoleMenu;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.param.permission.PermMenuAssignParam;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.role.RoleQueryService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    private final RoleManager roleManager;

    private final UserRoleService userRoleService;

    private final RoleMenuManager roleMenuManager;

    private final RoleQueryService roleQueryService;

    private final PermMenuManager permMenuManager;

    /**
     * 保存角色菜单授权
     * 如果删除角色关门的菜单关系, 将会级联删除子孙角色的菜单关系
     * 新增角色菜单关系, 会根据 updateAddChildren状态 来决定是否级联新增子孙角色的菜单关系
     * 如果当前用户没有拥有该角色父级的分配关系, 不允许进行编辑
     */
    @CacheEvict(value = "cache:permMenu", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(PermMenuAssignParam param) {
        Long roleId = param.getRoleId();
        Role role = roleManager.findById(roleId)
                .orElseThrow(RoleNotExistedException::new);
        // 判断是否有上级角色的权限
        if (!userRoleService.checkUserRole(role.getPid())){
            throw new ValidationFailedException("你没有权限操作该角色");
        }

        String clientCode = param.getClientCode();
        List<Long> menuIds = param.getMenuIds();
        // 已经保存的角色菜单关系
        List<RoleMenu> RoleMenus = roleMenuManager.findAllByRoleAndClient(roleId, clientCode);
        List<Long> roleMenuIds = RoleMenus.stream().map(RoleMenu::getMenuId).toList();
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
        if (Objects.nonNull(role.getPid())){
            List<Long> collect = roleMenuManager.findAllByRoleAndClient(role.getPid(), clientCode)
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
        if (param.isUpdateChildren()) {
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
        List<RoleResult> childrenRoles = roleQueryService.findChildren(roleId);
        // 新增
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(childrenRoles)){
            List<RoleMenu> addRoleMenus = new ArrayList<>();
            for (Long addPermId : addPermIds) {
                for (RoleResult childrenRole : childrenRoles) {
                    addRoleMenus.add(new RoleMenu(childrenRole.getId(), clientCode, addPermId));
                }
            }
            roleMenuManager.saveAll(addRoleMenus);
        }
        // 删除
        if (CollUtil.isNotEmpty(deletePermIds) && CollUtil.isNotEmpty(childrenRoles)) {
            // 子孙角色
            List<Long> childrenRoleIds = childrenRoles.stream()
                    .map(RoleResult::getId)
                    .toList();
            for (Long childrenId : childrenRoleIds) {
                roleMenuManager.deleteByMenuIds(childrenId,clientCode,deletePermIds);
            }
        }
    }


    /*------------------------------  管理端查看和配置使用  ------------------------------------*/

    /**
     * 查询当前角色已经选择的菜单id
     */
    public List<Long> findIdsByRoleAndClient(Long roleId, String clientCode) {
        List<RoleMenu> rolePermissions = roleMenuManager.findAllByRoleAndClient(roleId,clientCode);
        return rolePermissions.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

    /**
     * 获取当前用户角色下可见的菜单信息(即上级菜单被分配的权限), 并转换成树返回, 分配时使用
     * 如果是顶级角色且为超级管理员, 可以查看所有的菜单
     * 如果是子角色, 查询父角色关联的菜单
     * 如果是子角色且用户不拥有子角色的上级角色, 返回空
     */
    public List<PermMenuResult> treeByRoleAssign(Long roleId, String clientCode) {
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        // 判断是否有上级角色的权限, 没有权限返回空
        if (!userRoleService.checkUserRole(role.getPid())){
            return new ArrayList<>(0);
        }
        // 如果有有上级级角色, 查询可以被分配的菜单(上级角色已经分配的菜单), 没有返回当前终端所有的菜单
        List<PermMenu> menus;
        if (Objects.nonNull(role.getPid())){
                // 查询当前角色父角色被分配的菜单
                menus = SpringUtil.getBean(this.getClass()).findAllByRoleAndClient(role.getPid(), clientCode);
        } else {
            menus = permMenuManager.findAllByClient(clientCode);

        }
        // 转换为树并返回
        List<PermMenuResult> list = menus.stream()
                .map(PermMenu::toResult)
                .toList();
        return this.buildTree(list);

    }

    /*------------------------------  运行时使用  ------------------------------------*/

    /**
     * 根据角色和请求方式进行查询出请求菜单 需要进行缓存,
     * 构造用户菜单时, 会合并多个角色的菜单, 然后再转换为菜单树
     */
    @Cacheable(value = "cache:permMenu", key = "#clientCode+':'+#roleId")
    public List<PermMenu> findAllByRoleAndClient(Long roleId, String clientCode) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAll(PermMenu.class)
                // 菜单关联
                .innerJoin(RoleMenu.class,RoleMenu::getRoleId, Role::getId)
                .innerJoin(PermMenu.class, PermMenu::getId, RoleMenu::getMenuId)
                // 角色关联
                .eq(Role::getId, roleId)
                .eq(RoleMenu::getClientCode, clientCode)
                .eq(RoleMenu::getRoleId, Role::getId)
                .orderByAsc(PermMenu::getId);
        return roleManager.selectJoinList(PermMenu.class, wrapper);
    }

    /**
     * 递归建树
     * @param menus 查询出的菜单数据
     * @return 递归后的树列表
     */
    public List<PermMenuResult> buildTree(List<PermMenuResult> menus) {
        return TreeBuildUtil.build(menus, null, PermMenuResult::getId, PermMenuResult::getPid,
                PermMenuResult::setChildren, Comparator.comparing(PermMenuResult::getSortNo));

    }
}
