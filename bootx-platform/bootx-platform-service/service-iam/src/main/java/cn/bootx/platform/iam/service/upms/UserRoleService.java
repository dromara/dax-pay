package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.UserRoleManager;
import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.upms.UserRole;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.result.role.RoleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


/**
 * 用户角色关系
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final RoleManager roleManager;

    private final UserInfoManager userInfoManager;

    private final UserRoleManager userRoleManager;

    /**
     * 给用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(Long userId, List<Long> roleIds) {
        // 先删除用户拥有的角色
        userRoleManager.deleteByUser(userId);
        // 然后给用户添加角色
        List<UserRole> userRoles = this.createUserRoles(userId, roleIds);
        userRoleManager.saveAll(userRoles);
    }

    /**
     * 给用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssignBatch(List<Long> userIds, List<Long> roleIds) {
        List<UserInfo> userInfos = userInfoManager.findAllByIds(userIds);
        if (userInfos.size() != userIds.size()) {
            throw new BizException("用户数据有问题");
        }
        userRoleManager.deleteByUsers(userIds);
        List<UserRole> userRoles = userIds.stream()
            .map(userId -> this.createUserRoles(userId, roleIds))
            .flatMap(Collection::stream)
            .toList();
        userRoleManager.saveAll(userRoles);
    }

    /**
     * 根据id查询角色id, 作缓存
     */
    public List<Long> findRoleIdsByUser(Long userId) {
        return userRoleManager.findAllByUser(userId)
            .stream()
            .map(UserRole::getRoleId)
            .distinct()
            .toList();
    }

    /**
     * 查询用户所对应的角色
     */
    public List<RoleResult> findRolesByUser(Long userId) {
        return MpUtil.toListResult(roleManager.findAllByIds(this.findRoleIdsByUser(userId)));
    }

    /**
     * 创建用户角色关联
     */
    private List<UserRole> createUserRoles(Long userId, List<Long> roleIds) {
        return roleIds.stream()
            .map(roleId -> new UserRole(userId, roleId))
            .toList();
    }

}
