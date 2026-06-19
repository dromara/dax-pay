package cn.daxpay.open.platform.iam.service.role;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.convert.role.RoleConvert;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.upms.RoleCodeManager;
import cn.daxpay.open.platform.iam.dao.upms.RoleMenuManager;
import cn.daxpay.open.platform.iam.dao.upms.UserRoleManager;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.exception.role.RoleAlreadyExistedException;
import cn.daxpay.open.platform.iam.exception.role.RoleAlreadyUsedException;
import cn.daxpay.open.platform.iam.exception.role.RoleNotExistedException;
import cn.daxpay.open.platform.iam.param.role.RoleParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 角色操作服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleManager roleManager;

    private final UserRoleManager userRoleManager;

    private final RoleMenuManager roleMenuManager;

    private final RoleCodeManager roleCodeManager;

    /// 添加
    @Transactional(rollbackFor = Exception.class)
    public void add(RoleParam roleParam) {
        // code唯一性校验
        if (roleManager.existsByCode(roleParam.getCode())) {
            throw new RoleAlreadyExistedException();
        }
        // nameCn唯一性校验
        if (roleManager.existsByNameCn(roleParam.getNameCn())) {
            throw new RoleAlreadyExistedException();
        }
        Role role = Role.init(roleParam);
        roleManager.save(role);
    }

    /// 修改（禁止修改终端归属）
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleParam roleParam) {
        Long id = roleParam.getId();
        Role role = roleManager.findById(id).orElseThrow(RoleNotExistedException::new);

        // code唯一性校验
        if (roleManager.existsByCode(roleParam.getCode(), id)) {
            throw new RoleAlreadyExistedException();
        }
        // nameCn唯一性校验
        if (roleManager.existsByNameCn(roleParam.getNameCn(), id)) {
            throw new RoleAlreadyExistedException();
        }
        // 禁止修改终端归属
        roleParam.setClientCode(role.getClientCode());
        RoleConvert.CONVERT.copy(role, roleParam);
        roleManager.updateById(role);
    }

    /// 删除
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId) {
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);

        // 内置角色不允许删除
        if (role.isInternal()) {
            // 权限: 内置角色不允许删除
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.role.internalCannotDelete");
        }
        // 存在当前角色用户的场合不允许删除
        if (userRoleManager.existsByRoleId(roleId)) {
            throw new RoleAlreadyUsedException();
        }
        // 删除角色信息
        roleManager.deleteById(roleId);
        // 删除关联的各项权限配置
        roleMenuManager.deleteByRole(roleId);
        roleCodeManager.deleteByRole(roleId);
    }
}
