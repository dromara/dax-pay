package cn.daxpay.open.platform.iam.service.upms;

import cn.daxpay.open.platform.core.util.TreeBuildUtil;
import cn.daxpay.open.platform.iam.dao.permission.PermMenuManager;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.upms.RoleMenuManager;
import cn.daxpay.open.platform.iam.entity.permission.PermMenu;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.entity.upms.RoleMenu;
import cn.daxpay.open.platform.iam.result.permission.resource.PermMenuResult;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/// # 角色菜单菜单关系
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleMenuService {

    private final RoleManager roleManager;

    private final RoleMenuManager roleMenuManager;

    private final PermMenuManager permMenuManager;

    /*------------------------------  管理端查看和配置使用  ------------------------------------*/

    /// 查询当前角色已经选择的菜单id
    public List<Long> findIdsByRoleAndClient(Long roleId, String clientCode) {
        MPJLambdaWrapper<RoleMenu> wrapper = new MPJLambdaWrapper<RoleMenu>()
                .select(RoleMenu::getMenuId)
                .innerJoin(PermMenu.class, PermMenu::getId, RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, roleId)
                .eq(PermMenu::getClientCode, clientCode);
        return roleMenuManager.selectJoinList(Long.class, wrapper);
    }

    /*------------------------------  运行时使用  ------------------------------------*/

    /// 根据角色和请求方式进行查询出请求菜单 需要进行缓存,
    /// 构造用户菜单时, 会合并多个角色的菜单, 然后再转换为菜单树
    public List<PermMenu> findAllByRoleAndClient(Long roleId, String clientCode) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAll(PermMenu.class)
                // 菜单关联
                .innerJoin(RoleMenu.class, RoleMenu::getRoleId, Role::getId)
                .innerJoin(PermMenu.class, PermMenu::getId, RoleMenu::getMenuId)
                // 角色关联
                .eq(Role::getId, roleId)
                .eq(Role::getClientCode, clientCode)
                .eq(PermMenu::getClientCode, clientCode)
                .eq(RoleMenu::getRoleId, Role::getId)
                .orderByAsc(PermMenu::getId);
        return roleManager.selectJoinList(PermMenu.class, wrapper);
    }

    /// 递归建树
    /// @param menus 查询出的菜单数据
    /// @return 递归后的树列表
    public List<PermMenuResult> buildTree(List<PermMenuResult> menus) {
        return TreeBuildUtil.build(menus, null, PermMenuResult::getId, PermMenuResult::getPid,
                PermMenuResult::setChildren, Comparator.comparing(PermMenuResult::getSortNo));

    }
}

