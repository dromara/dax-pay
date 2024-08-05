package cn.daxpay.multi.service.dao.reconcile;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.reconcile.ReconcileDiscrepancy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileDiscrepancyManager extends BaseManager<ReconcileDiscrepancyMapper, ReconcileDiscrepancy> {
}
