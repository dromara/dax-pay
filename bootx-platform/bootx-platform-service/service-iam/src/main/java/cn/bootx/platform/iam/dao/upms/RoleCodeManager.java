package cn.bootx.platform.iam.dao.upms;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.upms.RoleCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限码关联关系
 * @author xxm
 * @since 2024/7/3
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RoleCodeManager extends BaseManager<RoleCodeMapper, RoleCode> {


    /**
     * 删除权限码关联关系
     */
    public void deleteByCodeIds(Long roleId, List<Long> deleteCodeIds) {
        if (deleteCodeIds.isEmpty()){
            return;
        }
        lambdaUpdate()
                .eq(RoleCode::getRoleId, roleId)
                .in(RoleCode::getCodeId, deleteCodeIds)
                .remove();
    }

    public List<RoleCode> findAllByRole(Long roleId) {
        return findAllByField(RoleCode::getRoleId, roleId);
    }
}
