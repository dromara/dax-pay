package org.dromara.daxpay.platform.iam.dao.permission;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.iam.entity.permission.PermMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 权限配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PermMenuManager extends BaseManager<PermMenuMapper, PermMenu> {

    /// 判断指定父节点下是否存在子菜单
    public boolean existsByPid(Long pid) {
        return existedByField(PermMenu::getPid, pid);
    }

    /// 根据父节点查询子菜单列表
    public List<PermMenu> findAllByPid(Long parentId) {
        return findAllByField(PermMenu::getPid, parentId);
    }

    /// 查询指定终端下的全部菜单
    public List<PermMenu> findAllByClient(String clientCode) {
        return lambdaQuery()
                .eq(PermMenu::getClientCode, clientCode)
                .orderByAsc(PermMenu::getId)
                .list();
    }

    /// 判断终端下菜单编码是否重复
    public boolean existsByMenuCodeAndClient(String menuCode, String clientCode, Long excludeId) {
        return lambdaQuery()
                .eq(PermMenu::getMenuCode, menuCode)
                .eq(PermMenu::getClientCode, clientCode)
                .ne(excludeId != null, PermMenu::getId, excludeId)
                .exists();
    }

}
