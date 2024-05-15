package cn.bootx.platform.iam.core.upms.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.upms.entity.UserRole;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRoleManager extends BaseManager<UserRoleMapper, UserRole> {

    public boolean existsByRoleId(Long roleId) {
        return existedByField(UserRole::getRoleId, roleId);
    }

    public void deleteByUser(Long userId) {
        deleteByField(UserRole::getUserId, userId);
    }

    public void deleteByUsers(List<Long> userIds) {
        deleteByFields(UserRole::getUserId, userIds);
    }

    public List<UserRole> findAllByUser(Long userId) {
        return findAllByField(UserRole::getUserId, userId);
    }

    public List<UserRole> findAllByRole(Long roleId) {
        return findAllByField(UserRole::getRoleId, roleId);
    }

    public List<UserRole> findAllByRoles(List<Long> roleIds) {
        return findAllByFields(UserRole::getRoleId, roleIds);
    }

    /**
     * 批量保存
     */
    public List<UserRole> saveAll(List<UserRole> userRoles) {
        MpUtil.initEntityList(userRoles, SecurityUtil.getUserIdOrDefaultId());
        MpUtil.executeBatch(userRoles, baseMapper::saveAll, this.DEFAULT_BATCH_SIZE);
        return userRoles;
    }

}
