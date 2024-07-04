package cn.bootx.platform.iam.dao.permission;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.permission.PermPath;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限
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


    public List<PermPath> findSimpleByIds(List<Long> ids) {
        return lambdaQuery()
                .select(PermPath::getId,PermPath::getPath)
                .eq(PermPath::isLeaf, true)
                .list();
    }
}
