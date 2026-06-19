package cn.daxpay.open.platform.iam.service.upms;

import cn.daxpay.open.platform.iam.dao.permission.PermCodeManager;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.upms.RoleCodeManager;
import cn.daxpay.open.platform.iam.entity.permission.PermCodeData;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.entity.upms.RoleCode;
import cn.daxpay.open.platform.iam.exception.role.RoleNotExistedException;
import cn.daxpay.open.platform.iam.result.permission.resource.PermCodeResult;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 角色权限码关联关系
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleCodeService {
    private final RoleCodeManager roleCodeManager;
    private final RoleManager roleManager;
    private final PermCodeManager permCodeManager;

    /*------------------------------  管理端查看和配置使用  ------------------------------------*/

    /// 获取当前用户角色下可见的权限码信息, 返回列表
    public List<PermCodeResult> findAllForAssign(Long roleId) {
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        // 查询全部的权限码
        List<PermCodeData> allPermCodes = permCodeManager.findAll();
        return allPermCodes.stream()
                .map(PermCodeData::toResult)
                .toList();
    }

    /// 获取当前用户角色被分配权限码权限信息
    public List<PermCodeResult> findAllByRole(Long roleId) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAll(PermCodeData.class)
                // 角色权限码关联
                .innerJoin(RoleCode.class, RoleCode::getRoleId, Role::getId)
                // 权限码信息
                .innerJoin(PermCodeData.class, PermCodeData::getId, RoleCode::getCodeId)
                .eq(RoleCode::getRoleId, roleId);
        List<PermCodeData> permCodes = roleManager.selectJoinList(PermCodeData.class, wrapper);
        return permCodes.stream().map(PermCodeData::toResult).toList();
    }

    /// 根据角色查询出选中的权限码
    public List<Long> findCodeIdsByRole(Long roleId) {
        MPJLambdaWrapper<RoleCode> wrapper = new MPJLambdaWrapper<RoleCode>()
                .select(RoleCode::getCodeId)
                .innerJoin(PermCodeData.class, PermCodeData::getId, RoleCode::getCodeId)
                .eq(RoleCode::getRoleId, roleId);
        return roleCodeManager.selectJoinList(Long.class, wrapper);
    }
    /*------------------------------  运行时使用  ------------------------------------*/

    /// 根据角色查询出权限码 需要进行缓存
    public List<String> findCodesByRole(Long roleId) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .select(PermCodeData::getCode)
                // 角色权限码关联
                .innerJoin(RoleCode.class, RoleCode::getRoleId, Role::getId)
                // 权限码信息
                .innerJoin(PermCodeData.class, PermCodeData::getId, RoleCode::getCodeId)
                .eq(RoleCode::getRoleId, roleId);
        return roleManager.selectJoinList(String.class, wrapper);
    }

}
