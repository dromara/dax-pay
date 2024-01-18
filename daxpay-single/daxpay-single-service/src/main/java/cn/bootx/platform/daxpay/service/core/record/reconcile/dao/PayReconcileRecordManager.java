package cn.bootx.platform.daxpay.service.core.record.reconcile.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.record.reconcile.entity.PayReconcileRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/1/18
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayReconcileRecordManager extends BaseManager<PayReconcileRecordMapper, PayReconcileRecord> {
}
