package cn.daxpay.open.platform.iam.dao.upms;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.upms.RoleCode;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 角色权限码关联关系
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class RoleCodeManager extends BaseManager<RoleCodeMapper, RoleCode> {

    /// 删除权限码关联关系(按角色)
    public void deleteByCodeIds(Long roleId, List<Long> deleteCodeIds) {
        if (CollUtil.isEmpty(deleteCodeIds)){
            return;
        }
        lambdaUpdate()
                .eq(RoleCode::getRoleId, roleId)
                .in(RoleCode::getCodeId, deleteCodeIds)
                .remove();
    }

    /// 删除权限码关联关系(全局)
    public void deleteByCodeIds(List<Long> deleteCodeIds) {
        if (CollUtil.isEmpty(deleteCodeIds)) {
            return;
        }
        lambdaUpdate()
                .in(RoleCode::getCodeId, deleteCodeIds)
                .remove();
    }

    /// 根据角色删除关联关系
    public void deleteByRole(Long roleId) {
        deleteByField(RoleCode::getRoleId, roleId);
    }

    public List<RoleCode> findAllByRole(Long roleId) {
        return findAllByField(RoleCode::getRoleId, roleId);
    }
}
