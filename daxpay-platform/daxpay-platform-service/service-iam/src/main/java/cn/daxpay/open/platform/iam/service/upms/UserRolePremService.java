package cn.daxpay.open.platform.iam.service.upms;

import cn.daxpay.open.platform.iam.dao.permission.PermMenuManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.permission.PermMenu;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.result.permission.resource.PermMenuResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.iam.service.permission.resource.PermCodeService;
import cn.daxpay.open.platform.iam.service.permission.resource.PermMenuService;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/// # 用户角色权限关联关系服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRolePremService {

    private final UserRoleService userRoleService;

    private final RoleMenuService roleMenuService;

    private final RoleCodeService roleCodeService;

    private final ClientCodeService clientCodeService;

    private final PermMenuService permMenuService;

    private final PermCodeService permCodeService;

    private final PermMenuManager permMenuManager;

    private final UserInfoManager userInfoManager;

    /// 根据当前登录用户查询菜单权限树
    /// 超级管理员直接返回当前终端下的全部菜单树
    public List<PermMenuResult> menuTreeByCurrentUser() {
        var user = SecurityUtil.getUser();
        String clientCode = clientCodeService.getClientCode();
        if (user.isAdmin()){
            return permMenuService.tree(clientCode);
        }
        return this.menuTreeByUser(user.getId(), clientCode);
    }

    /// 根据用户和终端查询菜单权限树
    public List<PermMenuResult> menuTreeByUser(Long userId, String clientCode) {
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        return this.menuTreeByRoles(roleIds, clientCode);
    }

    /// 根据角色列表和终端查询菜单权限树
    /// 只保存真实授权节点，但在返回展示树时自动补齐祖先节点
    public List<PermMenuResult> menuTreeByRoles(List<Long> roleIds, String clientCode){
        List<PermMenu> directMenus = roleIds.stream()
                .map(roleId -> roleMenuService.findAllByRoleAndClient(roleId, clientCode))
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        if (directMenus.isEmpty()) {
            return List.of();
        }

        // 一次性查询当前终端下全部菜单，后续在内存中补齐祖先节点，避免额外查询
        List<PermMenu> clientMenus = permMenuManager.findAllByClient(clientCode);
        Map<Long, PermMenu> menuMap = new HashMap<>(clientMenus.size());
        for (PermMenu menu : clientMenus) {
            menuMap.put(menu.getId(), menu);
        }

        // 提取直接授权菜单 ID，用于向上补齐祖先节点
        Set<Long> directMenuIds = new HashSet<>();
        for (PermMenu menu : directMenus) {
            directMenuIds.add(menu.getId());
        }

        Set<Long> allMenuIds = supplementAncestorIds(directMenuIds, menuMap);
        List<PermMenuResult> list = clientMenus.stream()
                .filter(menu -> allMenuIds.contains(menu.getId()))
                .map(PermMenu::toResult)
                .toList();
        return roleMenuService.buildTree(list);
    }

    /// 补齐祖先节点 ID
    /// 基于直接授权节点逐级向上查找父节点，直到根节点结束
    /// 使用 visited 防止异常层级数据导致死循环
    private Set<Long> supplementAncestorIds(Set<Long> directMenuIds, Map<Long, PermMenu> menuMap) {
        Set<Long> allIds = new HashSet<>(directMenuIds);
        for (Long menuId : directMenuIds) {
            Set<Long> visited = new HashSet<>();
            Long currentId = menuId;
            while (currentId != null && visited.add(currentId)) {
                PermMenu currentMenu = menuMap.get(currentId);
                if (currentMenu == null || currentMenu.getPid() == null || currentMenu.getPid() == 0L) {
                    break;
                }
                Long pid = currentMenu.getPid();
                allIds.add(pid);
                currentId = pid;
            }
        }
        return allIds;
    }

    /// 根据用户获取权限码
    /// 超级管理员直接返回全部权限码
    public List<String> findAllCodesByUser(Long userId){
        if (SecurityUtil.getUser().isAdmin()){
            return permCodeService.findAllCode();
        }
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        return this.findPermCodesByRoles(roleIds);
    }

    /// 根据角色列表汇总权限码并去重
    private List<String> findPermCodesByRoles(List<Long> roleIds) {
        return roleIds.stream()
                .map(roleCodeService::findCodesByRole)
                .flatMap(Collection::stream)
                .distinct()
                .toList();
    }
}

