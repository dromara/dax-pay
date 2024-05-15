package cn.bootx.platform.iam.core.upms.service;

import cn.bootx.platform.common.core.annotation.CountTime;
import cn.bootx.platform.common.core.annotation.NestedPermission;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.iam.core.scope.dao.DataRoleDeptManager;
import cn.bootx.platform.iam.core.scope.dao.DataRoleManager;
import cn.bootx.platform.iam.core.scope.dao.DataRoleUserManager;
import cn.bootx.platform.iam.core.scope.entity.DataRole;
import cn.bootx.platform.iam.core.scope.entity.DataRoleDept;
import cn.bootx.platform.iam.core.scope.entity.DataRoleUser;
import cn.bootx.platform.iam.core.upms.entity.UserDataRole;
import cn.bootx.platform.iam.core.user.dao.UserDeptManager;
import cn.bootx.platform.iam.core.user.entity.UserDept;
import cn.bootx.platform.iam.dto.scope.DataRoleDto;
import cn.bootx.platform.starter.data.perm.code.DataScopeEnum;
import cn.bootx.platform.starter.data.perm.scope.DataPermScope;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.iam.core.dept.dao.DeptManager;
import cn.bootx.platform.iam.core.dept.entity.Dept;
import cn.bootx.platform.iam.core.upms.dao.UserDataRoleManager;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.USER_DATA_SCOPE;

/**
 * 用户数据权限关联关系
 *
 * @author xxm
 * @since 2021/12/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDataRoleService {

    private final UserDataRoleManager userDataRoleManager;

    private final UserDeptManager userDeptManager;

    private final DataRoleManager dataRoleManager;

    private final DataRoleUserManager dataRoleUserManager;

    private final DataRoleDeptManager dataRoleDeptManager;

    private final DeptManager deptManager;

    /**
     * 给用户数据权限关联关系
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_DATA_SCOPE }, key = "#userId")
    public void saveAssign(Long userId, Long roleId) {
        // 先删除用户拥有的数据权限
        userDataRoleManager.deleteByUser(userId);
        if (Objects.nonNull(roleId)) {
            userDataRoleManager.save(new UserDataRole(userId, roleId));
        }
    }

    /**
     * 给用户数据角色关联关系
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_DATA_SCOPE }, key = "#userIds")
    public void saveAssignBatch(List<Long> userIds, Long roleId) {
        // 先删除用户拥有的数据权限
        userDataRoleManager.deleteByUsers(userIds);
        List<UserDataRole> userDataRoles = userIds.stream()
            .map(userId -> new UserDataRole(userId, roleId))
            .collect(Collectors.toList());
        userDataRoleManager.saveAll(userDataRoles);
    }

    /**
     * 查询用户所对应的数据角色信息
     */
    public DataRoleDto findDataRoleByUser(Long userId) {
        if (Objects.isNull(this.findDataRoleIdByUser(userId))) {
            return new DataRoleDto();
        }
        return dataRoleManager.findById(this.findDataRoleIdByUser(userId))
            .map(DataRole::toDto)
            .orElseThrow(DataNotExistException::new);
    }

    /**
     * 查询用户所对应的数据角色id
     */
    public Long findDataRoleIdByUser(Long userId) {
        return userDataRoleManager.findByUserId(userId).map(UserDataRole::getRoleId).orElse(null);

    }

    /**
     * 根据用户id获取对应的数据权限范围
     */
    @CountTime
    @NestedPermission
    @Cacheable(value = USER_DATA_SCOPE, key = "#userId")
    public DataPermScope getDataPermScopeByUser(Long userId) {
        DataPermScope dataPermScope = new DataPermScope();
        List<UserDataRole> userDataRoles = userDataRoleManager.findAllByUserId(userId);

        if (CollUtil.isEmpty(userDataRoles)) {
            return dataPermScope.setScopeType(DataScopeEnum.SELF);
        }
        UserDataRole userDataRole = userDataRoles.get(0);
        DataRole dataRole = dataRoleManager.findById(userDataRole.getRoleId())
            .orElseThrow(() -> new BizException("数据角色不存在"));

        dataPermScope.setScopeType(DataScopeEnum.findByCode(dataRole.getType()));
        // 用户
        if (Objects.equals(dataRole.getType(), DataScopeEnum.USER_SCOPE.getCode())) {
            Set<Long> userIds = dataRoleUserManager.findByDateRoleId(dataRole.getId())
                .stream()
                .map(DataRoleUser::getUserId)
                .collect(Collectors.toSet());
            return dataPermScope.setUserScopeIds(userIds);
        }
        // 部门
        else if (Objects.equals(dataRole.getType(), DataScopeEnum.DEPT_SCOPE.getCode())) {
            Set<Long> deptIds = dataRoleDeptManager.findByDateRoleId(dataRole.getId())
                .stream()
                .map(DataRoleDept::getDeptId)
                .collect(Collectors.toSet());
            return dataPermScope.setDeptScopeIds(deptIds);
        }
        // 部门+下属部门
        else if (Objects.equals(dataRole.getType(), DataScopeEnum.SELF_DEPT_AND_SUB.getCode())) {
            Set<Long> deptIds = dataRoleDeptManager.findByDateRoleId(dataRole.getId())
                    .stream()
                    .map(DataRoleDept::getDeptId)
                    .collect(Collectors.toSet());
            return dataPermScope.setDeptScopeIds(this.findDeptAndSub(deptIds));
        }
        // 用户和部门
        else if (Objects.equals(dataRole.getType(), DataScopeEnum.DEPT_AND_USER_SCOPE.getCode())) {
            Set<Long> userIds = dataRoleUserManager.findByDateRoleId(dataRole.getId())
                .stream()
                .map(DataRoleUser::getUserId)
                .collect(Collectors.toSet());
            Set<Long> deptIds = dataRoleDeptManager.findByDateRoleId(dataRole.getId())
                .stream()
                .map(DataRoleDept::getDeptId)
                .collect(Collectors.toSet());
            return dataPermScope.setDeptScopeIds(deptIds).setUserScopeIds(userIds);
        }
        // 自己所属的部门
        else if (Objects.equals(dataRole.getType(), DataScopeEnum.SELF_DEPT.getCode())) {
            Set<Long> deptIds = userDeptManager.findDeptIdsByUser(userId)
                .stream()
                .map(UserDept::getDeptId)
                .collect(Collectors.toSet());
            return dataPermScope.setDeptScopeIds(deptIds);
        }
        // 自己所属的部门和下级部门
        else if (Objects.equals(dataRole.getType(), DataScopeEnum.SELF_DEPT_AND_SUB.getCode())) {
            Set<Long> deptIds = this.findSelfDeptAndSub(userId);
            return dataPermScope.setDeptScopeIds(deptIds);
        }
        else {
            // 默认是只能查看自己的数据
            return dataPermScope;
        }
    }

    /**
     * 查找自己及子级部门
     */
    private Set<Long> findSelfDeptAndSub(Long userId) {
        Set<Long> deptIds = userDeptManager.findDeptIdsByUser(userId)
            .stream()
            .map(UserDept::getDeptId)
            .collect(Collectors.toSet());
        Map<Long, Dept> deptMap = deptManager.findAll()
            .stream()
            .collect(Collectors.toMap(MpIdEntity::getId, Function.identity()));
        Set<String> deptOrgCodes = deptIds.stream().map(deptMap::get).map(Dept::getOrgCode).collect(Collectors.toSet());
        return deptMap.values()
                .stream()
                .filter(dept -> this.judgeSubDept(dept.getOrgCode(), deptOrgCodes))
                .map(MpIdEntity::getId)
                .collect(Collectors.toSet());
    }


    /**
     * 查找部门及子级部门
     */
    private Set<Long> findDeptAndSub(Set<Long> deptIds) {
        Map<Long, Dept> deptMap = deptManager.findAll()
                .stream()
                .collect(Collectors.toMap(MpIdEntity::getId, Function.identity()));
        Set<String> deptOrgCodes = deptIds.stream()
                .map(deptMap::get)
                .map(Dept::getOrgCode)
                .collect(Collectors.toSet());
        return deptMap.values()
                .stream()
                .filter(dept -> this.judgeSubDept(dept.getOrgCode(), deptOrgCodes))
                .map(MpIdEntity::getId)
                .collect(Collectors.toSet());
    }

    /**
     * 判断是否是子部门
     */
    private boolean judgeSubDept(String orgCode, Set<String> orgCodes) {
        return orgCodes.stream().anyMatch(s -> StrUtil.startWith(orgCode,s) && orgCode.length()>=s.length());
    }

}
