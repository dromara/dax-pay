package cn.daxpay.open.platform.iam.dao.upms;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.upms.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 用户角色关系
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRoleManager extends BaseManager<UserRoleMapper, UserRole> {

    public boolean existsByRoleId(Long roleId) {
        return existedByField(UserRole::getRoleId, roleId);
    }

    public boolean existsByUserRole(Long userId, Long roleId) {
        return lambdaQuery()
                .eq(UserRole::getUserId,userId)
                .eq(UserRole::getRoleId, roleId)
                .exists();
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

    public UserRole findOneByUser(Long userId) {
        return firstOpt(q -> q.eq(UserRole::getUserId, userId)).orElse(null);
    }

    public List<UserRole> findAllByRole(Long roleId) {
        return findAllByField(UserRole::getRoleId, roleId);
    }

    public List<UserRole> findAllByRoles(List<Long> roleIds) {
        return findAllByFields(UserRole::getRoleId, roleIds);
    }

}
