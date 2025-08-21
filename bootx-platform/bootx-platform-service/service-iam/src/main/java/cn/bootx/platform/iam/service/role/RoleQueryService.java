package cn.bootx.platform.iam.service.role;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.core.rest.dto.KeyValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.upms.UserRoleManager;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.upms.UserRole;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.starter.auth.exception.NotLoginException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色查询
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleQueryService {

    private final RoleManager roleManager;

    private final UserRoleManager userRoleManager;

    /**
     * 角色树
     */
    public List<RoleResult> tree(){
        UserDetail userDetail = SecurityUtil.getCurrentUser().orElseThrow(NotLoginException::new);
        // 查询所有的角色
        List<Role> allRoles = roleManager.findAll();
        List<RoleResult> roleList = allRoles.stream()
                .map(Role::toResult)
                .collect(Collectors.toList());
        // 全部角色的数据树
        List<RoleResult> tree = this.recursiveBuildTree(roleList);
        // 系统管理员，获取全部的角色
        if (userDetail.isAdmin()) {
            return tree;
        } else {
            // 普通用户, 查询已经分配的角色和下级角色,然后重新构建树
            return this.findRoleByUser(userDetail.getId(),tree);
        }
    }

    /**
     * 获取子孙角色
     */
    public List<RoleResult> findChildren(Long ...ids) {
        if (Objects.nonNull(ids)){
            ArrayList<Long> roleIds = ListUtil.toList(ids);
            // 平铺树
            List<RoleResult> tree = this.tree();
            List<RoleResult> roleDtoList = TreeBuildUtil.unfold(tree, RoleResult::getChildren);
            // 找到对应角色的分支树, 然后通过二次平铺树, 获取所有的子孙角色
            List<RoleResult> collect = roleDtoList.stream()
                    .filter(roleDto -> roleIds.contains(roleDto.getPid()))
                    .toList();
            // 二次平铺树
            return TreeBuildUtil.unfold(collect, RoleResult::getChildren);
        }
        return new ArrayList<>();
    }

    /**
     * 角色列表
     */
    public List<RoleResult> findAll() {
        return MpUtil.toListResult(roleManager.findAll());
    }

    /**
     * 角色分页
     */
    public PageResult<RoleResult> page(PageParam pageParam, RoleParam roleParam) {
        return MpUtil.toPageResult(roleManager.page(pageParam, roleParam));
    }

    /**
     * 角色下拉框
     */
    public List<KeyValue> dropdown() {
        return roleManager.findDropdown();
    }

    /**
     * 详情
     */
    public RoleResult findById(Long id) {
        return roleManager.findById(id).map(Role::toResult).orElseThrow(RoleNotExistedException::new);
    }

    /**
     * code是否存在
     */
    public boolean existsByCode(String code) {
        return roleManager.existsByCode(code);
    }

    /**
     * code是否存在
     */
    public boolean existsByCode(String code, Long id) {
        return roleManager.existsByCode(code, id);
    }

    /**
     * name是否存在
     */
    public boolean existsByName(String name) {
        return roleManager.existsByName(name);
    }

    /**
     * name是否存在
     */
    public boolean existsByName(String name, Long id) {
        return roleManager.existsByName(name, id);
    }

    /**
     * 获取用户可以管理的角色
     */
    private List<RoleResult> findRoleByUser(Long userId,List<RoleResult> tree){
        // 查找子级拥有的角色
        List<Long> roleIds = userRoleManager.findAllByUser(userId).stream()
                .map(UserRole::getRoleId)
                .toList();
        // 获取关联的角色和子角色
        List<RoleResult> unfold = TreeBuildUtil.unfold(tree, RoleResult::getChildren).stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toList());
        var list = new ArrayList<>(unfold);
        // 将子孙级别的角色移除, 只保留根角色
        for (var out : unfold) {
            for (var in : unfold) {
                // out 是 in 的子级, 从list中移除, 并进行下一个元素的判断
                if (this.isChildOf(unfold,out.getId(),in.getId())){
                    list.remove(out);
                    break;
                }
            }
        }
        return list;
    }

    /**
     * 递归建树
     * @return 递归后的树列表
     */
    private List<RoleResult> recursiveBuildTree(List<RoleResult> roles) {
        return TreeBuildUtil.build(roles, null, RoleResult::getId, RoleResult::getPid,
                RoleResult::setChildren, Comparator.comparingLong(RoleResult::getId));

    }

    /**
     * 判断是否是别的元素的子级
     */
    public boolean isChildOf(List<RoleResult> roles, Long id, Long pid) {
        // 先找到id元素的上级元素
        for (var e : roles) {
            if (Objects.equals(e.getId(), id)) {
                if (Objects.equals(e.getPid(), pid)) {
                    return true;  // 找到直接父级
                } else if (Objects.nonNull(e.getPid())) {
                    // 递归查找id的上级是否为pid的子级
                    return isChildOf(roles, e.getPid(), pid);
                }
            }
        }
        return false;  // 如果找不到id或者pid都不匹配，返回false
    }
}
