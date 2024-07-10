package cn.bootx.platform.iam.dao.permission;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.permission.PermMenu;
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

    public boolean existsByPid(Long pid) {
        return existedByField(PermMenu::getPid, pid);
    }

    public List<PermMenu> findAllByPid(Long parentId) {
        return findAllByField(PermMenu::getPid, parentId);
    }

    public List<PermMenu> findAllByClient(String clientCode) {
        return lambdaQuery()
                .eq(PermMenu::getClientCode, clientCode)
                .orderByAsc(PermMenu::getId)
                .list();
    }

}
