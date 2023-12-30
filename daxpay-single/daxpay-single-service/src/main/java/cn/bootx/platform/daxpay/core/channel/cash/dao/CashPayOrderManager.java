package cn.bootx.platform.daxpay.core.channel.cash.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.channel.cash.entity.CashPayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 现金支付
 *
 * @author xxm
 * @since 2021/6/23
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashPayOrderManager extends BaseManager<CashPayOrderMapper, CashPayOrder> {

    public Optional<CashPayOrder> findByPaymentId(Long paymentId) {
        return findByField(CashPayOrder::getPaymentId, paymentId);
    }

}
