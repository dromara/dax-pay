package cn.bootx.platform.iam.core.user.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.user.entity.UserDept;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户部门关系
 *
 * @author xxm
 * @since 2021/9/29
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDeptManager extends BaseManager<UserDeptMapper, UserDept> {

    /**
     * 根据用户id删除关联关系
     */
    public void deleteByUser(Long userId) {
        this.deleteByField(UserDept::getUserId, userId);
    }

    /**
     * 根据用户id集合删除关联关系
     */
    public void deleteByUsers(List<Long> userIds) {
        this.deleteByFields(UserDept::getUserId, userIds);
    }

    /**
     * 根据部门id集合删除
     */
    public void deleteByDeptIds(List<Long> deptIds) {
        this.deleteByFields(UserDept::getDeptId, deptIds);
    }

    /**
     * 根据用户id 查询部门ids
     */
    public List<UserDept> findDeptIdsByUser(Long userId) {
        return findAllByField(UserDept::getUserId, userId);
    }

    /**
     * 批量保存
     */
    @Override
    public List<UserDept> saveAll(List<UserDept> list) {
        MpUtil.initEntityList(list, SecurityUtil.getUserIdOrDefaultId());
        MpUtil.executeBatch(list, baseMapper::saveAll, this.DEFAULT_BATCH_SIZE);
        return list;
    }

}
