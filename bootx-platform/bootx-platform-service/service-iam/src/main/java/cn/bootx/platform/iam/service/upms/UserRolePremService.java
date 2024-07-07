package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.entity.permission.PermMenu;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import cn.bootx.platform.iam.result.permission.PermPathResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色权限关联关系服务
 * @author xxm
 * @since 2024/7/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRolePremService {

    private final UserRoleService userRoleService;

    private final RoleMenuService roleMenuService;

    private final RolePathService rolePathService;

    private final RoleCodeService roleCodeService;

    /**
     * 根据传入的用户和终端查询菜单权限树
     */
    public List<PermMenuResult> menuTreeByUser(Long userId, String clientCode) {
        // 获取用户角色
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        return this.menuTreeByRoles(roleIds, clientCode);
    }

    /**
     * 根据传入的角色ID列表和终端查询菜单权限树
     */
    public List<PermMenuResult> menuTreeByRoles(List<Long> roleIds, String clientCode){
        List<PermMenuResult> list = roleIds.stream()
                .map(roleId -> roleMenuService.findAllByRoleAndClient(roleId, clientCode))
                .flatMap(Collection::stream)
                .distinct()
                .map(PermMenu::toResult)
                .toList();
        return roleMenuService.buildTree(list);
    }

    /**
     * 获取请求路径树
     */
    public List<PermPathResult> pathTreeByUser(Long userId, String clientCode){
        // 获取用户角色
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        // 获取叶子节点
        List<PermPathResult> list = roleIds.stream()
                .map(roleId -> rolePathService.treeByRoleAssign(roleId, clientCode))
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        // 获取目录节点

        // TODO 获取目录节点, 进行合并后生成树
        return TreeBuildUtil.build(list, null, PermPathResult::getCode, PermPathResult::getParentCode, PermPathResult::setChildren);
    }

    /**
     * 根据传入的户ID、请求类型、终端查询请求路径
     */
    public List<String> findPathByUser(Long userId, String method, String clientCode){
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        return this.findPathByRoles(roleIds, method, clientCode);
    }

    /**
     * 根据传入的角色ID列表、请求类型、终端查询请求路径
     */
    public List<String> findPathByRoles(List<Long> roleIds, String method, String clientCode){
        return roleIds.stream()
                .map(roleId -> rolePathService.findPathsByRoleAndMethod(roleId, method, clientCode))
                .flatMap(Collection::stream)
                .distinct()
                .toList();
    }

    /**
     * 获取权限码树
     */
    public List<PermCodeResult> codeTreeByUser(Long userId){
        // 获取用户角色
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        // 获取拥有权限的权限码
        List<PermCodeResult> list = roleIds.stream()
                .map(roleCodeService::treeByRole)
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        // TODO 获取目录节点, 进行合并后生成树
        return TreeBuildUtil.build(list, null, PermCodeResult::getId, PermCodeResult::getPid, PermCodeResult::setChildren);

    }

    /**
     * 根据用户获取权限码
     */
    public List<String> findPermCodesByUser(Long userId){
        // 获取用户角色
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userId);
        return this.findPermCodesByRoles(roleIds);
    }

    /**
     * 根据角色获取权限码
     */
    private List<String> findPermCodesByRoles(List<Long> roleIds) {
        return roleIds.stream()
                .map(roleCodeService::findCodesByRole)
                .flatMap(Collection::stream)
                .distinct()
                .toList();
    }
}
