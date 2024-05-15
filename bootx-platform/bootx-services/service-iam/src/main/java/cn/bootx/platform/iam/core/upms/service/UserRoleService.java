package cn.bootx.platform.iam.core.upms.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.iam.core.role.dao.RoleManager;
import cn.bootx.platform.iam.core.upms.dao.UserRoleManager;
import cn.bootx.platform.iam.core.upms.entity.UserRole;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.dto.role.RoleDto;
import cn.bootx.platform.iam.event.user.UserAssignRoleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.USER_PATH;
import static cn.bootx.platform.iam.code.CachingCode.USER_PERM_CODE;

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

    private final ApplicationEventPublisher eventPublisher;


    /**
     * 给用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_PATH, USER_PERM_CODE }, allEntries = true)
    public void saveAssign(Long userId, List<Long> roleIds) {
        // 先删除用户拥有的角色
        userRoleManager.deleteByUser(userId);
        // 然后给用户添加角色
        List<UserRole> userRoles = this.createUserRoles(userId, roleIds);
        userRoleManager.saveAll(userRoles);
        eventPublisher.publishEvent(new UserAssignRoleEvent(this, Collections.singletonList(userId),roleIds));
    }

    /**
     * 给用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_PATH, USER_PERM_CODE }, allEntries = true)
    public void saveAssignBatch(List<Long> userIds, List<Long> roleIds) {
        List<UserInfo> userInfos = userInfoManager.findAllByIds(userIds);
        if (userInfos.size() != userIds.size()) {
            throw new BizException("用户数据有问题");
        }
        userRoleManager.deleteByUsers(userIds);
        List<UserRole> userRoles = userIds.stream()
            .map(userId -> this.createUserRoles(userId, roleIds))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        userRoleManager.saveAll(userRoles);
        eventPublisher.publishEvent(new UserAssignRoleEvent(this, userIds,roleIds));
    }

    /**
     * 根据id查询角色id
     */
    public List<Long> findRoleIdsByUser(Long userId) {
        return userRoleManager.findAllByUser(userId)
            .stream()
            .map(UserRole::getRoleId)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * 查询用户所对应的角色
     */
    public List<RoleDto> findRolesByUser(Long userId) {
        return ResultConvertUtil.dtoListConvert(roleManager.findAllByIds(this.findRoleIdsByUser(userId)));
    }

    /**
     * 创建用户角色关联
     */
    private List<UserRole> createUserRoles(Long userId, List<Long> roleIds) {
        return roleIds.stream()
            .map(roleId -> new UserRole().setRoleId(roleId).setUserId(userId))
            .collect(Collectors.toList());
    }

}
