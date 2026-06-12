package org.dromara.daxpay.platform.iam.dao.upms;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.iam.entity.upms.RoleMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 角色权限关系
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class RoleMenuManager extends BaseManager<RoleMenuMapper, RoleMenu> {

    public void deleteByMenuId(Long menuId) {
        deleteByField(RoleMenu::getMenuId, menuId);
    }

    public void deleteByRole(Long roleId) {
        this.deleteByField(RoleMenu::getRoleId, roleId);
    }

    public List<RoleMenu> findAllByRoles(List<Long> roleIds) {
        return findAllByFields(RoleMenu::getRoleId, roleIds);
    }

    public List<RoleMenu> findAllByRole(Long roleId) {
        return findAllByField(RoleMenu::getRoleId, roleId);
    }

    /// 根据角色id、菜单ID进行删除
    public void deleteByMenuIds(Long roleId, List<Long> menuIds) {
        if (menuIds.isEmpty()) {
            return;
        }
        lambdaUpdate()
                .eq(RoleMenu::getRoleId, roleId)
                .in(RoleMenu::getMenuId, menuIds)
                .remove();
    }
}
