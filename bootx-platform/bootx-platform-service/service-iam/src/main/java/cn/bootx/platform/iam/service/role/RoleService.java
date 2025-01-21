package cn.bootx.platform.iam.service.role;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.RoleMenuManager;
import cn.bootx.platform.iam.dao.upms.RolePathManager;
import cn.bootx.platform.iam.dao.upms.UserRoleManager;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.exception.role.RoleAlreadyExistedException;
import cn.bootx.platform.iam.exception.role.RoleAlreadyUsedException;
import cn.bootx.platform.iam.exception.role.RoleHasChildrenException;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.iam.service.upms.UserRoleService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色操作服务
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleManager roleManager;

    private final UserRoleManager userRoleManager;

    private final RolePathManager rolePathManager;

    private final RoleMenuManager roleMenuManager;

    private final UserRoleService userRoleService;


    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(RoleParam roleParam) {

        // 判断是否有权限修改
        if (!userRoleService.checkUserRole(roleParam.getPid())){
            throw new ValidationFailedException("你没有权限创建该角色");
        }
        // name和code唯一性校验
        if (roleManager.existsByCode(roleParam.getCode())) {
            throw new RoleAlreadyExistedException();
        }
        if (roleManager.existsByName(roleParam.getName())) {
            throw new RoleAlreadyExistedException();
        }
        Role role = Role.init(roleParam);
        roleManager.save(role);
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleParam roleParam) {
        Long id = roleParam.getId();
        Role role = roleManager.findById(id).orElseThrow(RoleNotExistedException::new);

        // 判断是否有权限修改
        if (!userRoleService.checkUserRole(role.getPid())){
            throw new ValidationFailedException("你没有权限编辑该角色");
        }

        // 角色的层级不可以被改变
        roleParam.setPid(null);
        // name和code唯一性校验
        if (roleManager.existsByCode(roleParam.getCode(), id)) {
            throw new RoleAlreadyExistedException();
        }
        if (roleManager.existsByName(roleParam.getName(), id)) {
            throw new RoleAlreadyExistedException();
        }

        BeanUtil.copyProperties(roleParam, role, CopyOptions.create().ignoreNullValue());
        roleManager.updateById(role);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId) {
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        //  判断是否有权限修改
        if (!userRoleService.checkUserRole(role.getPid())){
            throw new ValidationFailedException("你没有权限删除该角色");
        }

        // 有下级角色不允许删除
        if (roleManager.existsByPid(roleId)) {
            throw new RoleHasChildrenException();
        }
        // 存在当前角色用户的场合不允许删除
        if (userRoleManager.existsByRoleId(roleId)) {
            throw new RoleAlreadyUsedException();
        }
        // 删除角色信息
        roleManager.deleteById(roleId);
        // 删除关联的各项权限配置
        rolePathManager.deleteByRole(roleId);
        roleMenuManager.deleteByRole(roleId);
        rolePathManager.deleteByRole(roleId);
    }
}
