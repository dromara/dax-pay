package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.permission.PermCodeManager;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.RoleCodeManager;
import cn.bootx.platform.iam.entity.permission.PermCode;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.upms.RoleCode;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.param.permission.PermCodeAssignParam;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
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
    private final PermCodeManager permCodeManager;

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

    /**
     * 管理端配置使用
     * 获取当前用户角色下可见的权限码信息, 并转换成树返回
     * 如果是顶级角色, 可以查看所有的权限
     * 如果是子角色, 查询分配给自身的权限
     */
    public List<PermCodeResult> treeByRoleAssign(Long roleId) {
        // 查询全部的请求权限, 后续生成树时也会使用
        List<PermCode> allPermCodes = permCodeManager.findAll();
        // 只保留叶子节点的数据, 如果是顶级角色, 直接可以使用, 不是的话需要进行过滤
        List<PermCode> permCodes = allPermCodes.stream()
                .filter(PermCode::isLeaf)
                .toList();
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        // 如果有有上级角色, 只可以显示分配给自身的权限
        if (Objects.nonNull(role.getPid())){
            List<String> codes = roleCodeManager.findAllByRole(role.getId())
                    .stream()
                    .map(RoleCode::getCode)
                    .toList();
            permCodes = permCodes.stream()
                    .filter(o->codes.contains(o.getCode()))
                    .toList();
        }
        // 根据查询出来的数据生成树
        return this.buildPathTree(permCodes, allPermCodes);
    }

    /**
     * 获取当前用户角色被分配请求权限信息
     */
    public List<PermCodeResult> treeByRole(Long roleId) {

        return null;
    }


    /**
     * 运行和管理时都会使用
     * 根据角色和请求方式进行查询出请求路径 需要进行缓存
     */
    public List<String> findCodesByRole(Long roleId) {
        return roleCodeManager.findAllByRole(roleId).stream()
                .map(RoleCode::getCode)
                .toList();
    }

    /**
     * 根据查询出来的请求权限信息数据生成树
     * TODO 需要找到叶子节点往前的节点
     */
    private List<PermCodeResult> buildPathTree(List<PermCode> permCodes, List<PermCode> allPermCodes){
        List<PermCode> catalogMap = allPermCodes.stream()
                .filter(o -> !o.isLeaf())
                .toList();

        // 进行合并并转为树状结构
        List<PermCodeResult> list = permCodes.stream()
                .map(PermCode::toResult)
                .toList();
        return TreeBuildUtil.build(list, null, PermCodeResult::getId, PermCodeResult::getPid, PermCodeResult::setChildren);
    }


}
