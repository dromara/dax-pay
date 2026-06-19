package cn.daxpay.open.platform.iam.service.permission.resource;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.util.StrUtil;
import cn.daxpay.open.platform.core.enums.perm.MenuTypeEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.TreeBuildUtil;
import cn.daxpay.open.platform.iam.convert.permission.PermMenuConvert;
import cn.daxpay.open.platform.iam.dao.permission.PermMenuManager;
import cn.daxpay.open.platform.iam.dao.upms.RoleMenuManager;
import cn.daxpay.open.platform.iam.entity.permission.PermMenu;
import cn.daxpay.open.platform.iam.param.permission.resource.PermMenuParam;
import cn.daxpay.open.platform.iam.result.permission.resource.PermMenuResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 菜单权限
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PermMenuService {

    private final PermMenuManager permMenuManager;

    private final RoleMenuManager roleMenuManager;

    /// 添加菜单权限
    @Transactional(rollbackFor = Exception.class)
    public void add(PermMenuParam param) {
        // 校验父级菜单类型
        validateParentType(param.getPid(), param.getMenuType());
        // 检查菜单编码是否重复（仅菜单类型需要）
        if (MenuTypeEnum.MENU.equalsCode(param.getMenuType()) && StrUtil.isNotEmpty(param.getMenuCode())) {
            if (permMenuManager.existsByMenuCodeAndClient(param.getMenuCode(), param.getClientCode(), null)) {
                // 权限: 菜单编码已存在
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.codeExists");
            }
        }
        if (MenuTypeEnum.SUBPAGE.equalsCode(param.getMenuType())) {
            param.setHidden(true);
        }
        PermMenu permMenu = PermMenu.init(param);
        permMenuManager.save(permMenu);
    }

    /// 更新
    @Transactional(rollbackFor = Exception.class)
    public void update(PermMenuParam param) {
        PermMenu permMenu = permMenuManager.findById(param.getId())
                // 权限: 菜单不存在
                .orElseThrow(() -> new DataNotExistException("error.iam.menu.notExist"));

        // 校验父级菜单类型
        validateParentType(param.getPid(), param.getMenuType());
        // 检查上级菜单是否出现了循环依赖
        if (this.wouldCreateCycle(param.getId(), param.getPid())) {
            // 权限: 上级菜单不能是自身或下级菜单
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.cycleDependency");
        }
        // 检查菜单编码是否重复（仅菜单类型需要）
        if (MenuTypeEnum.MENU.equalsCode(param.getMenuType()) && StrUtil.isNotEmpty(param.getMenuCode())) {
            if (permMenuManager.existsByMenuCodeAndClient(param.getMenuCode(), param.getClientCode(), param.getId())) {
                // 权限: 菜单编码已存在
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.codeExists");
            }
        }
        if (MenuTypeEnum.SUBPAGE.equalsCode(param.getMenuType()) && permMenuManager.existsByPid(param.getId())) {
            // 权限: 子页面不能有下级菜单
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.subpageNoChildren");
        }
        if (MenuTypeEnum.SUBPAGE.equalsCode(param.getMenuType())) {
            param.setHidden(true);
        }
        PermMenuConvert.CONVERT.copy(param, permMenu);
        permMenuManager.updateById(permMenu);
    }

    /// 根据id查询
    public PermMenuResult findById(Long id) {
        return permMenuManager.findById(id).map(PermMenu::toResult).orElseThrow(DataNotExistException::new);
    }

    /// 列表
    public List<PermMenuResult> findAll() {
        return MpUtil.toListResult(permMenuManager.findAll());
    }

    /// 列表(根据应用code)
    public List<PermMenuResult> findAllByClientCode(String clientCode) {
        return MpUtil.toListResult(permMenuManager.findAllByClient(clientCode));
    }

    /// 根据id集合查询
    public List<PermMenuResult> findByIds(List<Long> permissionIds) {
        return MpUtil.toListResult(permMenuManager.findAllByIds(permissionIds));
    }

    /// 删除
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 有子菜单不可以删除
        if (permMenuManager.existsByPid(id)) {
            // 权限: 有子菜单不可以删除
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.hasChildrenCannotDelete");
        }
        roleMenuManager.deleteByMenuId(id);
        permMenuManager.deleteById(id);
    }

    /// 检查设置父菜单是否会形成循环依赖
    /// 沿着父菜单链向上遍历，检查是否会出现当前菜单ID
    ///
    /// @param currentMenuId 当前菜单ID
    /// @param newPid 要设置的父菜单ID
    /// @return 是否会形成循环
    private boolean wouldCreateCycle(Long currentMenuId, Long newPid) {
        if (newPid == null) {
            return false;
        }
        // 如果父菜单是自己，形成循环
        if (Objects.equals(currentMenuId, newPid)) {
            return true;
        }
        // 沿着父菜单链向上遍历
        Long pid = newPid;
        Set<Long> visited = new HashSet<>();
        while (pid != null) {
            // 防止无限循环（数据异常情况）
            if (visited.contains(pid)) {
                return true;
            }
            visited.add(pid);

            // 如果父菜单链中出现当前菜单ID，形成循环
            if (Objects.equals(currentMenuId, pid)) {
                return true;
            }

            // 获取父菜单的pid
            PermMenu parent = permMenuManager.findById(pid).orElse(null);
            if (parent == null) {
                break;
            }
            pid = parent.getPid();
        }
        return false;
    }

    /// 菜单树(查看全部)
    public List<PermMenuResult> tree(String clientCode) {
        List<PermMenuResult> menus = permMenuManager.findAllByClient(clientCode).stream()
                .map(PermMenu::toResult)
                .toList();
        return TreeBuildUtil.build(menus, null, PermMenuResult::getId, PermMenuResult::getPid, PermMenuResult::setChildren, Comparator.comparing(PermMenuResult::getSortNo));
    }

    /// 校验父级菜单类型
    /// - 目录类型可以选择目录作为父级，也可以不选（作为根目录）
    /// - 菜单、内嵌页面、外链的上级必须是目录类型
    /// - 子页面的上级必须是菜单类型
    ///
    /// @param pid 父级菜单ID
    /// @param menuType 当前菜单类型
    private void validateParentType(Long pid, String menuType) {
        MenuTypeEnum typeEnum = MenuTypeEnum.getByCode(menuType);
        if (typeEnum == null) {
            // 权限: 无效的菜单类型
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.invalidType");
        }
        if (MenuTypeEnum.CATALOG.equals(typeEnum)) {
            return;
        }
        if (MenuTypeEnum.SUBPAGE.equals(typeEnum)) {
            if (pid == null) {
                // 权限: 子页面必须选择上级菜单
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.subpageNeedParent");
            }
            PermMenu parent = permMenuManager.findById(pid).orElse(null);
            if (parent == null) {
                // 权限: 上级菜单不存在
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.parentNotExist");
            }
            if (!MenuTypeEnum.MENU.equalsCode(parent.getMenuType())) {
                // 权限: 子页面的上级菜单必须是菜单类型
                throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.subpageParentMustBeMenu");
            }
            return;
        }
        if (pid == null) {
            // 权限: 菜单、内嵌页面、外链必须选择上级目录
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.needParentCatalog");
        }
        PermMenu parent = permMenuManager.findById(pid).orElse(null);
        if (parent == null) {
            // 权限: 上级菜单不存在
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.parentNotExist");
        }
        if (!MenuTypeEnum.CATALOG.equalsCode(parent.getMenuType())) {
            // 权限: 菜单、内嵌页面、外链的上级菜单必须是目录类型
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.menu.parentMustBeCatalog");
        }
    }

    /// 检查菜单编码是否存在(同一终端下)
    /// @param menuCode 菜单编码
    /// @param clientCode 终端编码
    /// @param excludeId 排除的菜单ID(编辑时使用)
    /// @return 是否存在
    public boolean checkMenuCodeExists(String menuCode, String clientCode, Long excludeId) {
        return permMenuManager.existsByMenuCodeAndClient(menuCode, clientCode, excludeId);
    }
}


