package org.dromara.daxpay.payment.pay.order.dao;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.pay.order.entity.PayNormalOrder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 普通支付业务单管理器
///
@Repository
public class PayNormalOrderManager extends BaseManager<PayNormalOrderMapper, PayNormalOrder> {

    /// 根据业务单号查询
    public Optional<PayNormalOrder> findByBizOrderNo(String bizOrderNo, String appId) {
        return lambdaQuery()
                .eq(PayNormalOrder::getBizOrderNo, bizOrderNo)
                .eq(PayNormalOrder::getAppId, appId)
                .oneOpt();
    }
}
