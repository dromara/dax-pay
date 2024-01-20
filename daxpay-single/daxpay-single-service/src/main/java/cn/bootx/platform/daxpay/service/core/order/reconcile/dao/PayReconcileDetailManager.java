package cn.bootx.platform.daxpay.service.core.order.reconcile.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayReconcileDetailManager extends BaseManager<PayReconcileDetailMapper, PayReconcileDetail> {
}
