package cn.daxpay.single.service.core.order.reconcile.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/5/4
 */
@Slf4j
@Repository
public class ReconcileFileManager extends BaseManager<ReconcileFileMapper, ReconcileFile> {

    /**
     * 根据对账单ID查询对账单文件
     */
    public List<ReconcileFile> findAllByReconcileId(Long reconcileId) {
        return findAllByField(ReconcileFile::getReconcileId, reconcileId);
    }
}
