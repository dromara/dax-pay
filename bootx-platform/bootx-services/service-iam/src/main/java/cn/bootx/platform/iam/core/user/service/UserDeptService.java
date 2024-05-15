package cn.bootx.platform.iam.core.user.service;

import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.iam.core.dept.dao.DeptManager;
import cn.bootx.platform.iam.core.dept.event.DeptDeleteEvent;
import cn.bootx.platform.iam.core.user.dao.UserDeptManager;
import cn.bootx.platform.iam.core.user.entity.UserDept;
import cn.bootx.platform.iam.dto.dept.DeptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户部门关联关系
 *
 * @author xxm
 * @since 2021/9/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeptService {

    private final UserDeptManager userDeptManager;

    private final DeptManager deptManager;

    /**
     * 给用户分配部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(Long userId, List<Long> deptIds) {

        // 先删除用户拥有的部门
        userDeptManager.deleteByUser(userId);
        // 然后给用户添加部门
        List<UserDept> userDeptList = this.createUserDepots(userId, deptIds);
        userDeptManager.saveAll(userDeptList);
    }

    /**
     * 给用户分配部门 批量
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssignBatch(List<Long> userIds, List<Long> deptIds) {
        // 先删除用户拥有的部门
        userDeptManager.deleteByUsers(userIds);
        // 然后给用户添加部门
        List<UserDept> userDeptList = userIds.stream()
            .map(userId -> this.createUserDepots(userId, deptIds))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        userDeptManager.saveAll(userDeptList);
    }

    /**
     * 根据用户Id查询部门id
     */
    public List<Long> findDeptIdsByUser(Long userId) {
        return userDeptManager.findDeptIdsByUser(userId)
            .stream()
            .map(UserDept::getDeptId)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * 查询用户所对应的部门
     */
    public List<DeptDto> findDeptListByUser(Long userId) {
        return ResultConvertUtil.dtoListConvert(deptManager.findAllByIds(this.findDeptIdsByUser(userId)));
    }

    /**
     * 处理部门被删除的情况
     */
    @EventListener
    public void DeptDeleteEventListener(DeptDeleteEvent event) {
        userDeptManager.deleteByDeptIds(event.getDeptIds());
    }

    /**
     * 创建用户部门关联
     */
    private List<UserDept> createUserDepots(Long userId, List<Long> deptIds) {
        return deptIds.stream().map(deptId -> new UserDept(userId, deptId)).collect(Collectors.toList());
    }

}
