package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.core.exception.ValidationFailedException;
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
import cn.bootx.platform.iam.service.permission.PermCodeService;
import cn.bootx.platform.iam.service.role.RoleQueryService;
import cn.hutool.core.collection.CollUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
    private final PermCodeService permCodeService;
    private final UserRoleService userRoleService;

    /**
     * 保存角色路径授权
     */
    @Cacheable(value = "cache:permCode", key = "#param.roleId")
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(PermCodeAssignParam param) {
        Long roleId = param.getRoleId();
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        // 判断是否有上级角色的权限
        if (!userRoleService.checkUserRole(role.getPid())){
            throw new ValidationFailedException("你没有权限操作该角色");
        }
        List<Long> codeIds = param.getCodeIds();

        // 先删后增
        List<RoleCode> allRoleCodes = roleCodeManager.findAllByRole(roleId);
        List<Long> allRoleCodeIds = allRoleCodes.stream().map(RoleCode::getId).toList();
        // ------------------------ 需要删除的权限码 -----------------------------------------------------------
        List<RoleCode> deleteRoleCodes = allRoleCodes.stream()
                .filter(RoleCode -> !codeIds.contains(RoleCode.getId()))
                .toList();
        List<Long> deleteIds = deleteRoleCodes.stream().map(RoleCode::getId).collect(Collectors.toList());
        roleCodeManager.deleteByIds(deleteIds);

        // ------------------------ 需要新增的权限关系 -----------------------------------------------------------
        //过滤掉目录权限
        List<Long> catalogIds = permCodeManager.findAllByLeaf(false)
                .stream()
                .map(PermCode::getId)
                .toList();
        List<RoleCode> addRoleCode = codeIds.stream()
                .filter(id -> !allRoleCodeIds.contains(id))
                .filter(id -> !catalogIds.contains(id))
                .map(codeId -> new RoleCode(roleId, codeId))
                .collect(Collectors.toList());
        // 新增时验证是否超过了父级角色所拥有的权限
        if (Objects.nonNull(role.getPid())){
            List<Long> collect = roleCodeManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RoleCode::getId)
                    .toList();
            addRoleCode = addRoleCode.stream()
                    .filter(o->collect.contains(o.getId()))
                    .toList();
        }
        roleCodeManager.saveAll(addRoleCode);

        // ----------------------------------- 级联更新子孙角色 ------------------------------------------
        if (param.isUpdateChildren()) {
            // 新增的进行追加
            List<Long> addCodeIds = addRoleCode.stream()
                    .map(RoleCode::getId)
                    .collect(Collectors.toList());
            this.updateChildren(roleId, addCodeIds, deleteIds);
        } else {
            // 新增的不进行追加
            this.updateChildren(roleId, null, deleteIds);
        }
    }

    /**
     * 更新子孙角色关联关系
     */
    private void updateChildren(Long roleId, List<Long> addCodeIds, List<Long> deleteIds) {
        if (CollUtil.isNotEmpty(addCodeIds) && CollUtil.isNotEmpty(deleteIds)){
            return;
        }
        // 获取当前角色的子孙角色
        List<RoleResult> childrenRoles = roleQueryService.findChildren(roleId);
        // 级联新增
        if (CollUtil.isNotEmpty(addCodeIds) && CollUtil.isNotEmpty(childrenRoles)){
            List<RoleCode> addRoleCodes = new ArrayList<>();
            for (var addCode : addCodeIds) {
                for (RoleResult childrenRole : childrenRoles) {
                    addRoleCodes.add(new RoleCode(childrenRole.getId(), addCode));
                }
            }
            roleCodeManager.saveAll(addRoleCodes);
        }
        // 级联删除
        if (CollUtil.isNotEmpty(deleteIds) && CollUtil.isNotEmpty(childrenRoles)) {
            // 子孙角色的id
            List<Long> childrenRoleIds = childrenRoles.stream()
                    .map(RoleResult::getId)
                    .toList();
            for (Long childrenId : childrenRoleIds) {
                roleCodeManager.deleteByCodeIds(childrenId,deleteIds);
            }
        }
    }

    /*------------------------------  管理端查看和配置使用  ------------------------------------*/

    /**
     * 获取当前用户角色下可见的权限码信息(即上级菜单被分配的权限), 并转换成树返回, 分配时使用
     * 如果是顶级角色, 可以查看所有的权限
     * 如果是子角色, 查询父角色关联的权限
     * 如果是子角色且用户不拥有子角色的上级角色, 返回空
     */
    public List<PermCodeResult> treeByRoleAssign(Long roleId) {
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        // 判断是否有上级角色的权限, 没有权限返回空
        if (!userRoleService.checkUserRole(role.getPid())){
            return new ArrayList<>(0);
        }
        // 查询全部的权限码, 后续生成树时也会使用
        List<PermCode> allPermCodes = permCodeManager.findAll();
        // 权限码列表
        List<PermCode> permCodes = allPermCodes.stream()
                .filter(PermCode::isLeaf)
                .toList();
        // 如果有有上级角色, 显示上级角色已分配的权限
        if (Objects.nonNull(role.getPid())){
            List<Long> codeIds = roleCodeManager.findAllByRole(role.getPid())
                    .stream()
                    .map(RoleCode::getCodeId)
                    .toList();
            permCodes = allPermCodes.stream()
                    .filter(o->codeIds.contains(o.getId()))
                    .toList();
        }
        // 获取目录节点
        List<PermCode> catalogCodes = allPermCodes.stream()
                .filter(o -> !o.isLeaf())
                .toList();
        permCodes = new ArrayList<>(permCodes);
        permCodes.addAll(catalogCodes);
        List<PermCodeResult> codeResultList = permCodes.stream()
                .map(PermCode::toResult)
                .toList();
        // 根据查询出来的数据生成树
        return permCodeService.buildTree(codeResultList);
    }

    /**
     * 获取当前用户角色被分配权限码权限信息
     */
    public List<PermCodeResult> findAllByRole(Long roleId) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAll(PermCode.class)
                // 角色权限码关联
                .innerJoin(RoleCode.class, RoleCode::getRoleId, Role::getId)
                // 权限码信息
                .innerJoin(PermCode.class, PermCode::getId, RoleCode::getCodeId)
                .eq(RoleCode::getRoleId, roleId);
        List<PermCode> permCodes = roleManager.selectJoinList(PermCode.class, wrapper);
        return permCodes.stream().map(PermCode::toResult).toList();
    }


    /**
     * 根据角色查询出选中的权限码
     */
    public List<Long> findCodeIdsByRole(Long roleId) {
        return roleCodeManager.findAllByRole(roleId).stream()
                .map(RoleCode::getCodeId)
                .toList();
    }
    /*------------------------------  运行时使用  ------------------------------------*/

    /**
     * 根据角色查询出权限码 需要进行缓存
     */
    @Cacheable(value = "cache:permCode", key = "#roleId")
    public List<String> findCodesByRole(Long roleId) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .select(PermCode::getCode)
                // 角色权限码关联
                .innerJoin(RoleCode.class, RoleCode::getRoleId, Role::getId)
                // 权限码信息
                .innerJoin(PermCode.class, PermCode::getId, RoleCode::getCodeId)
                .eq(RoleCode::getRoleId, roleId);
        return roleManager.selectJoinList(String.class, wrapper);
    }

}
