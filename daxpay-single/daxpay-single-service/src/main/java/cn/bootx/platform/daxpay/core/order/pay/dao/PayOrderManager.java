package cn.bootx.platform.daxpay.core.order.pay.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付订单
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderManager extends BaseManager<PayOrderMapper, PayOrder> {
    public Optional<PayOrder> findByBusinessNo(String businessNo) {
        return findByField(PayOrder::getBusinessNo,businessNo);
    }
}
