package cn.bootx.platform.iam.dao.upms;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.upms.RolePath;
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

    public List<RolePath> findAllByRoleAndClient(Long roleId, String clientCode) {
        return lambdaQuery()
                .eq(RolePath::getRoleId, roleId)
                .eq(RolePath::getClientCode, clientCode)
                .list();
    }

    public List<RolePath> findAllByRoles(List<Long> roleIds) {
        return findAllByFields(RolePath::getRoleId, roleIds);
    }

    public void deleteByRole(Long roleId) {
        deleteByField(RolePath::getRoleId, roleId);
    }

    public void deleteByPathIds(List<Long> pathIds) {
        deleteByFields(RolePath::getPathId, pathIds);
    }

//    /**
//     * 替换为for方式
//     */
//    @Override
//    public List<RolePath> saveAll(List<RolePath> rolePaths) {
//        rolePaths.forEach(rolePath -> rolePath.setId(IdUtil.getSnowflakeNextId()));
//        MpUtil.executeBatch(rolePaths, baseMapper::saveAll, this.DEFAULT_BATCH_SIZE);
//        return rolePaths;
//    }

    /**
     * 根据角色id 菜单ids 删除关联关系
     */
    public void deleteByPathIds(Long roleId, String clientCode, List<Long> pathIds) {
        if (pathIds.isEmpty()){
            return;
        }
        lambdaUpdate()
                .eq(RolePath::getRoleId, roleId)
                .eq(RolePath::getClientCode, clientCode)
                .in(RolePath::getPathId,pathIds)
                .remove();
    }
}
