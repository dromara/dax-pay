package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.util.TreeBuildUtil;
import cn.bootx.platform.iam.dao.permission.PermMenuManager;
import cn.bootx.platform.iam.dao.upms.RoleMenuManager;
import cn.bootx.platform.iam.entity.permission.PermMenu;
import cn.bootx.platform.iam.param.permission.PermMenuParam;
import cn.bootx.platform.iam.result.permission.PermMenuResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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

    /**
     * 添加菜单权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(PermMenuParam param) {
        // 判断是否是一级菜单，是的话清空父菜单
        if (param.isRoot()) {
            param.setPid(null);
        }
        PermMenu permMenu = PermMenu.init(param);
        permMenuManager.save(permMenu);
    }

    /**
     * 更新
     */
    @CacheEvict(value = "cache:permMenu", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(PermMenuParam param) {
        PermMenu permMenu = permMenuManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("菜单不存在"));
        BeanUtil.copyProperties(param, permMenu, CopyOptions.create().ignoreNullValue());

        // 判断是否是一级菜单，是的话清空父菜单ID
        if (param.isRoot()) {
            permMenu.setPid(null);
        }
        // TODO 检查上级菜单是否出现了循环依赖
        permMenuManager.updateById(permMenu);
    }

    /**
     * 根据id查询
     */
    public PermMenuResult findById(Long id) {
        return permMenuManager.findById(id).map(PermMenu::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 列表
     */
    public List<PermMenuResult> findAll() {
        return MpUtil.toListResult(permMenuManager.findAll());
    }

    /**
     * 列表(根据应用code)
     */
    public List<PermMenuResult> findAllByClientCode(String clientCode) {
        return MpUtil.toListResult(permMenuManager.findAllByClient(clientCode));
    }

    /**
     * 根据id集合查询
     */
    public List<PermMenuResult> findByIds(List<Long> permissionIds) {
        return MpUtil.toListResult(permMenuManager.findAllByIds(permissionIds));
    }

    /**
     * 删除
     */
    @CacheEvict(value = "cache:permMenu", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 有子菜单不可以删除
        if (permMenuManager.existsByPid(id)) {
            throw new BizException("有子菜单不可以删除");
        }
        roleMenuManager.deleteByMenuId(id);
        permMenuManager.deleteById(id);
    }

    /**
     * 菜单树(查看全部)
     */
    public List<PermMenuResult> tree(String clientCode) {
        List<PermMenuResult> menus = permMenuManager.findAllByClient(clientCode).stream()
                .map(PermMenu::toResult)
                .toList();
        return TreeBuildUtil.build(menus, null, PermMenuResult::getId, PermMenuResult::getPid, PermMenuResult::setChildren, Comparator.comparing(PermMenuResult::getSortNo));
    }
}
