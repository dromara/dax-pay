package cn.bootx.platform.iam.dao.permission;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.permission.PermPath;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求路径权限
 *
 * @author xxm
 * @since 2020/5/10 23:27
 */
@Repository
@RequiredArgsConstructor
public class PermPathManager extends BaseManager<PermPathMapper, PermPath> {

    /**
     * 根据节点类型查询查询
     */
    public List<PermPath> findAllByLeafAndClient(boolean isLeaf, String clientCode) {
        return lambdaQuery()
                .eq(PermPath::isLeaf, isLeaf)
                .eq(PermPath::getClientCode, clientCode)
                .list();
    }

    /**
     * 根据终端类型查询
     */
    public List<PermPath> findAllByClient(String clientCode) {
        return lambdaQuery()
                .eq(PermPath::getClientCode, clientCode)
                .list();
    }

    /**
     * 删除非子节点
     */
    public void deleteNotChild(String clientCode) {
        lambdaUpdate()
                .eq(PermPath::getClientCode, clientCode)
                .eq(PermPath::isLeaf, false)
                .remove();
    }


    /**
     * 查询简单的请求路径权限子节点, 只包括主键、路径和请求方式
     */
    public List<PermPath> findSimpleByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        return lambdaQuery()
                .select(PermPath::getId,PermPath::getPath,PermPath::getMethod)
                .eq(PermPath::isLeaf, true)
                .in(PermPath::getId, ids)
                .list();
    }
}
