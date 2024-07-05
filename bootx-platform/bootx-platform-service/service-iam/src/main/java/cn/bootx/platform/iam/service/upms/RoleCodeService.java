package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.iam.dao.upms.RoleCodeManager;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.upms.RoleCode;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.param.permission.PermCodeAssignParam;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.role.RoleQueryService;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色权限码关联关系
 * @author xxm
 * @since 2024/7/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleCodeService {
    private final RoleCodeManager roleCodeManager;
    private final RoleManager roleManager;
    private final RoleQueryService roleQueryService;

    /**
     * 保存角色路径授权
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(PermCodeAssignParam param) {

        Long roleId = param.getRoleId();
        List<String> codes = param.getCodes();

        // 先删后增
        List<RoleCode> roleCodes = roleCodeManager.findAllByRole(roleId);
        List<String> RoleCodes = roleCodes.stream().map(RoleCode::getCode).toList();
        // 需要删除的请求权限
        List<RoleCode> deleteRoleCodes = roleCodes.stream()
                .filter(RoleCode -> !codes.contains(RoleCode.getCode()))
                .toList();
        // 需要删除的关联ID
        List<Long> deleteIds = deleteRoleCodes.stream().map(RoleCode::getId).collect(Collectors.toList());
        roleCodeManager.deleteByIds(deleteIds);

        // 需要新增的权限关系
        List<RoleCode> addRoleCode = codes.stream()
                .filter(id -> !RoleCodes.contains(id))
                .map(code -> new RoleCode(roleId, code))
                .collect(Collectors.toList());
        // 新增时验证是否超过了父级角色所拥有的权限
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        if (Objects.nonNull(role.getPid())){
            List<String> collect = roleCodeManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RoleCode::getCode)
                    .toList();
            addRoleCode = addRoleCode.stream()
                    .filter(o->collect.contains(o.getCode()))
                    .toList();
        }
        roleCodeManager.saveAll(addRoleCode);

        List<String> deleteCodes = deleteRoleCodes.stream()
                .map(RoleCode::getCode)
                .collect(Collectors.toList());
        // 级联更新子孙角色
        if (param.isUpdateChildren()) {
            // 新增的进行追加
            List<String> addCodes = addRoleCode.stream()
                    .map(RoleCode::getCode)
                    .collect(Collectors.toList());
            this.updateChildren(roleId, addCodes, deleteCodes);
        } else {
            // 新增的不进行追加
            this.updateChildren(roleId, null, deleteCodes);
        }
    }

    /**
     * 更新子孙角色关联关系
     */
    private void updateChildren(Long roleId, List<String> addCodes, List<String> deleteCodes) {
        if (CollUtil.isNotEmpty(addCodes) && CollUtil.isNotEmpty(deleteCodes)){
            return;
        }
        // 获取当前角色的子孙角色
        List<RoleResult> childrenRoles = roleQueryService.findChildren(roleId);
        // 新增
        if (CollUtil.isNotEmpty(addCodes) && CollUtil.isNotEmpty(childrenRoles)){
            List<RoleCode> addRoleCodes = new ArrayList<>();
            for (var addCode : addCodes) {
                for (RoleResult childrenRole : childrenRoles) {
                    addRoleCodes.add(new RoleCode(childrenRole.getId(), addCode));
                }
            }
            roleCodeManager.saveAll(addRoleCodes);
        }
        // 删除
        if (CollUtil.isNotEmpty(deleteCodes) && CollUtil.isNotEmpty(childrenRoles)) {
            // 子孙角色的id
            List<Long> childrenRoleIds = childrenRoles.stream()
                    .map(RoleResult::getId)
                    .toList();
            for (Long childrenId : childrenRoleIds) {
                roleCodeManager.deleteByCodes(childrenId,deleteCodes);
            }
        }
    }


}
