package cn.daxpay.open.payment.pay.order.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.payment.pay.order.entity.PayNormalOrder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 普通支付业务单管理器
///
@Repository
public class PayNormalOrderManager extends BaseManager<PayNormalOrderMapper, PayNormalOrder> {

    /// 根据业务单号查询（按商户号自动租户隔离）
    public Optional<PayNormalOrder> findByBizOrderNo(String bizOrderNo) {
        return lambdaQuery()
                .eq(PayNormalOrder::getBizOrderNo, bizOrderNo)
                .oneOpt();
    }
}
