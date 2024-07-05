package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.iam.entity.permission.PermMenu;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
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
     * 根据传入的户ID、请求类型、终端查询请求路径
     */
    public List<String> findPathByRoles(Long userId, String method, String clientCode){
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



}
