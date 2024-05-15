package cn.bootx.platform.iam.core.permission.service;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.iam.param.permission.PermMenuParam;
import cn.bootx.platform.starter.auth.exception.NotLoginException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.iam.code.PermissionCode;
import cn.bootx.platform.iam.core.permission.dao.PermMenuManager;
import cn.bootx.platform.iam.core.permission.entity.PermMenu;
import cn.bootx.platform.iam.core.upms.dao.RoleMenuManager;
import cn.bootx.platform.iam.core.upms.entity.RoleMenu;
import cn.bootx.platform.iam.core.upms.service.UserRoleService;
import cn.bootx.platform.iam.dto.permission.PermMenuDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.USER_PERM_CODE;

/**
 * 菜单权限
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermMenuService {

    private final PermMenuManager permMenuManager;

    private final RoleMenuManager roleMenuManager;

    private final UserRoleService userRoleService;

    /**
     * 添加菜单权限
     */
    @Transactional(rollbackFor = Exception.class)
    public PermMenuDto add(PermMenuParam param) {
        // 判断是否是一级菜单，是的话清空父菜单
        if (PermissionCode.MENU_TYPE_TOP.equals(param.getMenuType())) {
            param.setParentId(null);
        }
        // 增加判断是否循环依赖情况
        PermMenu permission = PermMenu.init(param);
        return permMenuManager.save(permission).toDto();
    }

    /**
     * 更新
     */
    @CacheEvict(value = { USER_PERM_CODE }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public PermMenuDto update(PermMenuParam param) {
        PermMenu permMenu = permMenuManager.findById(param.getId()).orElseThrow(() -> new BizException("菜单权限不存在"));
        permMenu.setClientCode(null);
        BeanUtil.copyProperties(param, permMenu, CopyOptions.create().ignoreNullValue());

        // 判断是否是一级菜单，是的话清空父菜单ID
        if (PermissionCode.MENU_TYPE_TOP.equals(permMenu.getMenuType())) {
            permMenu.setParentId(null);
        }
        // TODO 检查上级菜单是否出现了循环依赖

        return permMenuManager.updateById(permMenu).toDto();
    }

    /**
     * 根据id查询
     */
    public PermMenuDto findById(Long id) {
        return permMenuManager.findById(id).map(PermMenu::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 列表
     */
    public List<PermMenuDto> findAll() {
        return ResultConvertUtil.dtoListConvert(permMenuManager.findAll());
    }

    /**
     * 列表(根据应用code)
     */
    public List<PermMenuDto> findAllByClientCode(String clientCode) {
        return ResultConvertUtil.dtoListConvert(permMenuManager.findAllByClientCode(clientCode));
    }

    /**
     * 根据id集合查询
     */
    public List<PermMenuDto> findByIds(List<Long> permissionIds) {
        return ResultConvertUtil.dtoListConvert(permMenuManager.findAllByIds(permissionIds));
    }

    /**
     * 删除
     */
    @CacheEvict(value = { USER_PERM_CODE }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 有子菜单不可以删除
        if (permMenuManager.existsByParentId(id)) {
            throw new BizException("有子菜单或下属权限不可以删除");
        }
        roleMenuManager.deleteByPermission(id);
        permMenuManager.deleteById(id);
    }

    /**
     * 根据菜单id获取资源(权限码)列表
     */
    public List<PermMenuDto> findResourceByMenuId(Long menuId) {
        UserDetail userDetail = SecurityUtil.getCurrentUser().orElseThrow(NotLoginException::new);
        List<PermMenu> resources = permMenuManager.findAllByParentId(menuId)
            .stream()
            .filter(permMenu -> Objects.equals(permMenu.getMenuType(), PermissionCode.MENU_TYPE_RESOURCE))
            .collect(Collectors.toList());
        // 管理员返回全部
        if (userDetail.isAdmin()) {
            return resources.stream().map(PermMenu::toDto).collect(Collectors.toList());
        }
        // 普通用户只能看到自己有权限的
        List<Long> roleIds = userRoleService.findRoleIdsByUser(userDetail.getId());
        List<Long> roleMenuIds = roleMenuManager.findAllByRoles(roleIds)
            .stream()
            .map(RoleMenu::getPermissionId)
            .collect(Collectors.toList());
        return resources.stream()
            .filter(permMenu -> roleMenuIds.contains(permMenu.getId()))
            .map(PermMenu::toDto)
            .collect(Collectors.toList());
    }

    /**
     * 权限编码是否被使用
     */
    public boolean existsByPermCode(String permCode) {
        return permMenuManager.existsByPermCode(permCode);
    }

    /**
     * 权限编码是否被使用
     * @param id
     * @return
     */
    public boolean existsByPermCode(String permCode, Long id) {
        return permMenuManager.existsByPermCode(permCode, id);
    }

}
