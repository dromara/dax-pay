package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.upms.entity.RolePath;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限
 *
 * @author xxm
 * @since 2020/5/27 16:02
 */
@Repository
@RequiredArgsConstructor
public class RolePathManager extends BaseManager<RolePathMapper, RolePath> {

    private final RolePathMapper rolePathMapper;

    public List<RolePath> findAllByRole(Long roleId) {
        return findAllByField(RolePath::getRoleId, roleId);
    }

    public List<RolePath> findAllByRoles(List<Long> roleIds) {
        return findAllByFields(RolePath::getRoleId, roleIds);
    }

    public void deleteByRole(Long roleId) {
        deleteByField(RolePath::getRoleId, roleId);
    }

    public void deleteByPermission(Long permissionId) {
        deleteByField(RolePath::getPermissionId, permissionId);
    }

    public void deleteByPermissions(List<Long> permissionIds) {
        deleteByFields(RolePath::getPermissionId, permissionIds);
    }

    /**
     * 替换为for方式
     */
    @Override
    public List<RolePath> saveAll(List<RolePath> rolePaths) {
        rolePaths.forEach(rolePath -> rolePath.setId(IdUtil.getSnowflakeNextId()));
        MpUtil.executeBatch(rolePaths, baseMapper::saveAll, this.DEFAULT_BATCH_SIZE);
        return rolePaths;
    }

    /**
     * 根据角色id 权限ids 删除关联关系
     */
    public void deleteByPermIds(Long roleId, List<Long> permissionIds) {
        lambdaUpdate()
                .eq(RolePath::getRoleId, roleId)
                .in(RolePath::getPermissionId,permissionIds)
                .remove();
    }
}
