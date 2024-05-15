package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.upms.entity.RoleMenu;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RoleMenuManager extends BaseManager<RoleMenuMapper, RoleMenu> {

    public void deleteByPermission(Long permissionId) {
        deleteByField(RoleMenu::getPermissionId, permissionId);
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


    public List<RoleMenu> findAllByRoleAndClientCode(Long roleId, String clientCode) {
        return lambdaQuery().eq(RoleMenu::getRoleId, roleId).eq(RoleMenu::getClientCode, clientCode).list();

    }

    /**
     * 根据角色id、客户端code、权限id进行删除
     */
    public void deleteByPermIds(Long roleId, String clientCode,List<Long> permissionIds) {
        lambdaUpdate()
                .eq(RoleMenu::getRoleId, roleId)
                .eq(RoleMenu::getClientCode,clientCode)
                .in(RoleMenu::getPermissionId,permissionIds)
                .remove();
    }


    @Override
    public List<RoleMenu> saveAll(List<RoleMenu> list) {
        list.forEach(roleMenu -> roleMenu.setId(IdUtil.getSnowflakeNextId()));
        MpUtil.executeBatch(list, baseMapper::saveAll, this.DEFAULT_BATCH_SIZE);
        return list;
    }

}
