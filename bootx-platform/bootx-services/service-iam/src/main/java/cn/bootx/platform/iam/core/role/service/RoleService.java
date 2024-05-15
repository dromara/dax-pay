package cn.bootx.platform.iam.core.role.service;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.core.util.TreeBuildUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.role.dao.RoleManager;
import cn.bootx.platform.iam.core.role.entity.Role;
import cn.bootx.platform.iam.core.upms.dao.RoleMenuManager;
import cn.bootx.platform.iam.core.upms.dao.RolePathManager;
import cn.bootx.platform.iam.core.upms.dao.UserRoleManager;
import cn.bootx.platform.iam.core.upms.entity.UserRole;
import cn.bootx.platform.iam.dto.role.RoleDto;
import cn.bootx.platform.iam.event.role.RoleCreateEvent;
import cn.bootx.platform.iam.event.role.RoleDeleteEvent;
import cn.bootx.platform.iam.event.role.RoleUpdateEvent;
import cn.bootx.platform.iam.exception.role.RoleAlreadyExistedException;
import cn.bootx.platform.iam.exception.role.RoleAlreadyUsedException;
import cn.bootx.platform.iam.exception.role.RoleHaschildrenException;
import cn.bootx.platform.iam.exception.role.RoleNotExistedException;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.starter.auth.exception.NotLoginException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.ListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.USER_PATH;

/**
 * 角色
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

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(RoleParam roleParam) {
        // name和code唯一性校验
        if (roleManager.existsByCode(roleParam.getCode())) {
            throw new RoleAlreadyExistedException();
        }
        if (roleManager.existsByName(roleParam.getName())) {
            throw new RoleAlreadyExistedException();
        }
        Role role = Role.init(roleParam);
        RoleDto roleDto = roleManager.save(role).toDto();
        eventPublisher.publishEvent(new RoleCreateEvent(this,roleDto));
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleParam roleParam) {
        Long id = roleParam.getId();
        // 角色的层级不可以被改变
        roleParam.setPid(null);
        // name和code唯一性校验
        if (roleManager.existsByCode(roleParam.getCode(), id)) {
            throw new RoleAlreadyExistedException();
        }
        if (roleManager.existsByName(roleParam.getName(), id)) {
            throw new RoleAlreadyExistedException();
        }

        Role role = roleManager.findById(id).orElseThrow(RoleNotExistedException::new);
        BeanUtil.copyProperties(roleParam, role, CopyOptions.create().ignoreNullValue());
        RoleDto roleDto = roleManager.updateById(role)
                .toDto();
        eventPublisher.publishEvent(new RoleUpdateEvent(this,roleDto));
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_PATH }, allEntries = true)
    public void delete(Long roleId) {
        if (Objects.isNull(roleId) || !roleManager.existsById(roleId)) {
            throw new RoleNotExistedException();
        }
        // 有下级角色不允许删除
        if (roleManager.existsByPid(roleId)) {
            throw new RoleHaschildrenException();
        }
        // 存在当前角色用户的场合不允许删除
        if (userRoleManager.existsByRoleId(roleId)) {
            throw new RoleAlreadyUsedException();
        }
        // 删除角色信息
        roleManager.deleteById(roleId);
        // 删除关联的请求和菜单权限
        rolePathManager.deleteByRole(roleId);
        roleMenuManager.deleteByRole(roleId);
        eventPublisher.publishEvent(new RoleDeleteEvent(this,roleId));
    }

    /**
     * 角色树
     */
    public List<RoleDto> tree(){
        UserDetail userDetail = SecurityUtil.getCurrentUser().orElseThrow(NotLoginException::new);
        // 查询所有的角色
        List<Role> allRoles = roleManager.findAll();
        List<RoleDto> roleList = allRoles.stream()
                .map(Role::toDto)
                .collect(Collectors.toList());
        // 全部角色的数据树
        List<RoleDto> tree = this.recursiveBuildTree(roleList);
        // 系统管理员，获取全部的角色
        if (userDetail.isAdmin()) {
            return tree;
        } else {
            // 普通用户, 查询已经分配的角色和下级角色,然后重新构建树
            return this.recursiveBuildTree(this.findRoleByUser(userDetail.getId(),tree));
        }
    }

    /**
     * 获取子孙角色
     */
    public List<RoleDto> findChildren(Long ...ids) {
        if (Objects.nonNull(ids)){
            ArrayList<Long> roleIds = ListUtil.toList(ids);
            // 平铺树
            List<RoleDto> tree = this.tree();
            List<RoleDto> roleDtoList = TreeBuildUtil.unfold(tree, RoleDto::getChildren);
            // 找到对应角色的分支树, 然后通过二次平铺树, 获取所有的子孙角色
            List<RoleDto> collect = roleDtoList.stream()
                    .filter(roleDto -> roleIds.contains(roleDto.getPid()))
                    .collect(Collectors.toList());
            // 二次平铺树
            return TreeBuildUtil.unfold(collect, RoleDto::getChildren);
        }
        return new ArrayList<>();
    }

    /**
     * 角色列表
     */
    public List<RoleDto> findAll() {
        return ResultConvertUtil.dtoListConvert(roleManager.findAll());
    }

    /**
     * 角色分页
     */
    public PageResult<RoleDto> page(PageParam pageParam, RoleParam roleParam) {
        return MpUtil.convert2DtoPageResult(roleManager.page(pageParam, roleParam));
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
    public RoleDto findById(Long id) {
        return ResultConvertUtil.dtoConvert(roleManager.findById(id));
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
    private List<RoleDto> findRoleByUser(Long userId,List<RoleDto> tree){
        // 查找子级拥有的角色
        List<Long> roleIds = userRoleManager.findAllByUser(userId).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        // 获取关联的角色和子角色
        List<RoleDto> unfold = TreeBuildUtil.unfold(tree, RoleDto::getChildren);
        return unfold.stream()
               .filter(role -> roleIds.contains(role.getId()))
               .collect(Collectors.toList());
    }

    /**
     * 递归建树
     * @return 递归后的树列表
     */
    private List<RoleDto> recursiveBuildTree(List<RoleDto> roles) {
        return TreeBuildUtil.build(roles, null, RoleDto::getId, RoleDto::getPid,
                RoleDto::setChildren, Comparator.comparingLong(RoleDto::getId));

    }
}
