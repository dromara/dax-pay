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
    public void deleteByCodes(Long roleId, List<String> deleteCodes) {
        if (deleteCodes.isEmpty()){
            return;
        }
        lambdaUpdate()
                .eq(RoleCode::getRoleId, roleId)
                .in(RoleCode::getCode, deleteCodes)
                .remove();
    }

    /**
     * 更新权限码
     */
    public void updateCodes(String oldCode, String newCode) {
        lambdaUpdate()
                .eq(RoleCode::getCode, oldCode)
                .set(RoleCode::getCode, newCode)
                .update();
    }

    public List<RoleCode> findAllByRole(Long roleId) {
        return findAllByField(RoleCode::getRoleId, roleId);
    }
}
