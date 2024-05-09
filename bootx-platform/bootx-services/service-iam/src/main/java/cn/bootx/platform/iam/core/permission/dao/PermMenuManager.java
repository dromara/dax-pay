package cn.bootx.platform.iam.core.permission.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.core.permission.entity.PermMenu;
import cn.bootx.platform.iam.code.PermissionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限配置
 *
 * @author xxm
 * @since 2021/8/3
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PermMenuManager extends BaseManager<PermMenuMapper, PermMenu> {

    public boolean existsByParentId(Long pid) {
        return existedByField(PermMenu::getParentId, pid);
    }

    public boolean existsByPermCode(String permCode) {
        return existedByField(PermMenu::getPermCode, permCode);
    }

    public boolean existsByPermCode(String permCode, Long id) {
        return existedByField(PermMenu::getPermCode, permCode, id);
    }

    public List<PermMenu> findAllByParentId(Long parentId) {
        return findAllByField(PermMenu::getParentId, parentId);
    }

    public List<PermMenu> findAllByClientCode(String clientCode) {
        return findAllByField(PermMenu::getClientCode, clientCode);
    }

    public List<PermMenu> findAllByResource() {
        return findAllByField(PermMenu::getMenuType, PermissionCode.MENU_TYPE_RESOURCE);
    }

}
