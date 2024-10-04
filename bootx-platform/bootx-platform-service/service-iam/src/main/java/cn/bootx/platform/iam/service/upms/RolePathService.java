package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.permission.PermPathManager;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.RolePathManager;
import cn.bootx.platform.iam.entity.permission.PermPath;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.upms.RolePath;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.param.permission.PermPathAssignParam;
import cn.bootx.platform.iam.result.permission.PermPathResult;
import cn.bootx.platform.iam.result.permission.SimplePermPathResult;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.role.RoleQueryService;
import cn.hutool.core.collection.CollUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色请求权限关系
 *
 * @author xxm
 * @since 2021/6/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RolePathService {

    private final RolePathManager rolePathManager;

    private final PermPathManager permPathManager;

    private final RoleManager roleManager;

    private final RoleQueryService roleQueryService;

    private final UserRoleService userRoleService;

    /**
     * 保存角色路径授权
     */
    @CacheEvict(value = "cache:permPath", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void saveAssign(PermPathAssignParam param) {
        Long roleId = param.getRoleId();
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);

        // 判断是否有上级角色的权限
        if (!userRoleService.checkUserRole(role.getPid())){
            throw new ValidationFailedException("你没有权限操作该角色");
        }

        String clientCode = param.getClientCode();
        List<Long> pathIds = param.getPathIds();

        // 已经保存的关联信息
        List<RolePath> rolePaths = rolePathManager.findAllByRoleAndClient(roleId, clientCode);
        List<Long> rolePathIds = rolePaths.stream().map(RolePath::getPathId).toList();
        // 需要删除的请求权限
        List<RolePath> deleteRolePaths = rolePaths.stream()
                .filter(rolePath -> !pathIds.contains(rolePath.getPathId()))
                .toList();

        // 删除的关联ID
        List<Long> deleteIds = deleteRolePaths.stream().map(RolePath::getId).collect(Collectors.toList());
        rolePathManager.deleteByIds(deleteIds);

        // 需要新增的权限关系
        List<RolePath> addRolePath = this.saveAddAssign(param, rolePathIds, role);


        // 需要进行级联删除的权限码
        List<Long> deletePermIds = deleteRolePaths.stream()
                .map(RolePath::getPathId)
                .collect(Collectors.toList());
        // 级联更新子孙角色
        if (param.isUpdateChildren()) {
            // 新增的进行追加
            List<Long> addPermIds = addRolePath.stream()
                    .map(RolePath::getPathId)
                    .collect(Collectors.toList());
            this.updateChildren(roleId, clientCode, addPermIds, deletePermIds);
        } else {
            // 新增的不进行追加
            this.updateChildren(roleId, clientCode, null, deletePermIds);
        }
    }

    /**
     * 保存并返回需要新增的权限关系
     *
     * @param param       分配参数
     * @param rolePathIds 已经保存的关联信息
     * @param role        角色
     * @return 添加的权限关系
     */
    private List<RolePath> saveAddAssign(PermPathAssignParam param, List<Long> rolePathIds, Role role){

        Long roleId = param.getRoleId();
        String clientCode = param.getClientCode();
        List<Long> pathIds = param.getPathIds();

        // 需要新增的权限关系
        List<RolePath> addRolePath = pathIds.stream()
                .filter(id -> !rolePathIds.contains(id))
                .map(permissionId -> new RolePath(roleId, param.getClientCode(), permissionId))
                .collect(Collectors.toList());
        // 验证是否超过了父级角色所拥有的权限, 如果超过进行过滤
        if (Objects.nonNull(role.getPid())){
            List<Long> collect = rolePathManager.findAllByRoleAndClient(role.getPid(), clientCode)
                    .stream()
                    .map(RolePath::getPathId)
                    .toList();
            addRolePath = addRolePath.stream()
                    .filter(o->collect.contains(o.getPathId()))
                    .toList();
        }
        // 去除其中的非叶子节点
        List<Long> addPathIds = addRolePath.stream()
                .map(RolePath::getPathId)
                .toList();
        List<Long> permPathIds = permPathManager.findSimpleByIds(addPathIds).stream()
                .map(MpIdEntity::getId)
                .toList();
        addRolePath = addRolePath.stream()
                .filter(o->permPathIds.contains(o.getPathId()))
                .toList();

        // 保存新增的
        rolePathManager.saveAll(addRolePath);
        return addRolePath;
    }

    /**
     * 更新子孙角色关联关系
     */
    private void updateChildren(Long roleId, String clientCode, List<Long> addPermIds, List<Long> deleteIds) {
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(deleteIds)){
            return;
        }
        // 当前角色的子孙角色
        List<RoleResult> children = roleQueryService.findChildren(roleId);
        // 新增
        if (CollUtil.isNotEmpty(addPermIds) && CollUtil.isNotEmpty(children)){
            List<RolePath> addRolePaths = new ArrayList<>();
            for (Long addPermId : addPermIds) {
                for (RoleResult childrenRole : children) {
                    addRolePaths.add(new RolePath(childrenRole.getId(), clientCode, addPermId));
                }
            }
            rolePathManager.saveAll(addRolePaths);
        }
        // 删除
        if (CollUtil.isNotEmpty(deleteIds) && CollUtil.isNotEmpty(children)) {
            // 子孙角色
            List<Long> childrenIds = children.stream()
                    .map(RoleResult::getId)
                    .toList();
            for (Long childrenId : childrenIds) {
                rolePathManager.deleteByPathIds(childrenId,clientCode,deleteIds);
            }
        }
    }

    /*------------------------------  管理端查看和配置使用  ------------------------------------*/
    /**
     * 查询当前角色已经选择的请求路径
     */
    public List<Long> findIdsByRole(Long roleId, String clientCode) {
        List<RolePath> rolePermissions = rolePathManager.findAllByRoleAndClient(roleId,clientCode);
        return rolePermissions.stream().map(RolePath::getPathId).collect(Collectors.toList());
    }

    /**
     * 获取当前用户角色下可见的请求权限信息(即上级菜单被分配的权限), 并转换成树返回, 分配时使用
     * 如果是顶级角色, 可以查看所有的权限
     * 如果是子角色, 查询父角色关联的权限
     * 如果是子角色且用户不拥有子角色的上级角色, 返回空
     */
    public List<SimplePermPathResult> treeByRoleAssign(Long roleId, String clientCode) {
        Role role = roleManager.findById(roleId).orElseThrow(RoleNotExistedException::new);
        // 判断是否有上级角色的权限, 没有权限返回空
        if (!userRoleService.checkUserRole(role.getPid())){
            return new ArrayList<>(0);
        }
        // 查询该终端全部的请求权限
        List<PermPath> allPermPaths = permPathManager.findAllByClient(clientCode);
        // 只保留叶子节点的数据, 如果是顶级角色, 直接可以使用, 不是的话需要进行过滤
        List<PermPath> permPaths = allPermPaths.stream()
                .filter(PermPath::isLeaf)
                .toList();
        // 如果有有上级角色, 只可以显示分配给自身的权限
        if (Objects.nonNull(role.getPid())){
            List<Long> pathIds = rolePathManager.findAllByRoleAndClient(role.getPid(),clientCode)
                    .stream()
                    .map(RolePath::getPathId)
                    .toList();
            permPaths = permPaths.stream()
                    .filter(o->pathIds.contains(o.getId()))
                    .toList();
        }
        // 根据查询出来的数据生成树
        List<PermPathResult> permPathResults = this.buildPathTree(permPaths, allPermPaths);
        // 平铺树并转换类型
        List<SimplePermPathResult> list = TreeBuildUtil.unfold(permPathResults, PermPathResult::getChildren)
                .stream()
                .map(PermPathResult::toSimple)
                .toList();
        // 设置上下级 id 关联
        Map<String, Long> codeIdMap = allPermPaths.stream()
                .filter(o->!o.isLeaf())
                .collect(Collectors.toMap(PermPath::getCode, MpIdEntity::getId));
        for (SimplePermPathResult pathResult : list) {
            pathResult.setPid(codeIdMap.get(pathResult.getParentCode()));
        }
        // 重新转换成树
        return TreeBuildUtil.build(list, null, SimplePermPathResult::getId, SimplePermPathResult::getPid, SimplePermPathResult::setChildren);
    }

    /**
     * 获取当前用户角色被分配权限码权限信息
     */
    public List<PermPath> findAllByRole(Long roleId, String clientCode) {
        MPJLambdaWrapper<PermPath> wrapper = new MPJLambdaWrapper<PermPath>()
                .selectAll(PermPath.class)
                // 角色路径关联
                .innerJoin(RolePath.class, RolePath::getPathId, PermPath::getId,
                        o->o.eq(RolePath::getRoleId, roleId)
                                .eq(RolePath::getClientCode, clientCode));
        return permPathManager.selectJoinList(PermPath.class, wrapper);
    }

    /**
     * 根据查询出来的请求权限信息数据生成树
     */
    public List<PermPathResult> buildPathTree(List<PermPath> permPaths, List<PermPath> catalogPath){
        // 生成请求权限目录映射表
        Map<String, PermPath> pathCatalogMap = catalogPath.stream()
                .filter(path -> !path.isLeaf())
                .collect(Collectors.toMap(PermPath::getCode, Function.identity()));

        // 获取分组
        List<PermPath> groupList = permPaths.stream()
                .map(PermPath::getParentCode)
                .distinct()
                .map(pathCatalogMap::get)
                .filter(Objects::nonNull)
                .toList();

        // 获取模块
        List<PermPath> moduleList = groupList.stream()
                .map(PermPath::getParentCode)
                .distinct()
                .map(pathCatalogMap::get)
                .filter(Objects::nonNull)
                .toList();
        // 进行合并并转为树状结构
        permPaths = new ArrayList<>(permPaths);
        permPaths.addAll(groupList);
        permPaths.addAll(moduleList);
        List<PermPathResult> list = permPaths.stream()
                .filter(Objects::nonNull)
                .map(PermPath::toResult)
                .toList();
        return TreeBuildUtil.build(list, null, PermPathResult::getCode, PermPathResult::getParentCode, PermPathResult::setChildren);
    }

    /*------------------------------  运行时使用  ------------------------------------*/


    /**
     * 根据角色和请求方式进行查询出请求路径 需要进行缓存
     */
    @Cacheable(value = "cache:permPath", key = "#method+':'+#clientCode+':'+#roleId")
    public List<String> findPathsByRoleAndMethod(Long roleId, String method, String clientCode) {
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .select(PermPath::getPath)//查询user表全部字段
                // 关联角色请求权限
                .innerJoin(RolePath.class, RolePath::getRoleId, Role::getId)
                // 关联请求权限
                .innerJoin(PermPath.class, PermPath::getId, RolePath::getPathId)
                .eq(Role::getId, roleId)
                .eq(RolePath::getClientCode, clientCode)
                .eq(PermPath::getMethod,method.toUpperCase());

        return roleManager.selectJoinList(String.class, wrapper);
    }
}
