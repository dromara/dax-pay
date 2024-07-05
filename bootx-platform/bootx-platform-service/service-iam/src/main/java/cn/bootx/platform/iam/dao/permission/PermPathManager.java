package cn.bootx.platform.iam.dao.permission;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.permission.PermPath;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
     * 删除非子节点
     */
    public void deleteNotChild() {
        lambdaUpdate().eq(PermPath::isLeaf, true).remove();
    }


    /**
     * 查询简单的请求路径权限子节点, 只包括主键、路径和请求方式
     */
    public List<PermPath> findSimpleByIds(List<Long> ids) {
        return lambdaQuery()
                .select(PermPath::getId,PermPath::getPath,PermPath::getMethod)
                .eq(PermPath::isLeaf, true)
                .in(PermPath::getId, ids)
                .list();
    }
}
