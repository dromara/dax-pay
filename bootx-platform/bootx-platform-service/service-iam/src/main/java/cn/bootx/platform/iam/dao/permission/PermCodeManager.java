package cn.bootx.platform.iam.dao.permission;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.entity.permission.PermCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限编码
 * @author xxm
 * @since 2024/7/3
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PermCodeManager extends BaseManager<PermCodeMapper, PermCode> {
    /**
     * 根据节点类型查询查询
     */
    public List<PermCode> findAllByLeaf(boolean isLeaf) {
        return findAllByField(PermCode::isLeaf,isLeaf);
    }
}
