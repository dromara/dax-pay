package cn.daxpay.open.plugin.easypay.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EasyPayOrderManager extends BaseManager<EasyPayOrderMapper, EasyPayOrder> {

    public Optional<EasyPayOrder> findByOutTradeNo(String outTradeNo) {
        return findByField(EasyPayOrder::getOutTradeNo, outTradeNo);
    }

    public Optional<EasyPayOrder> findByTradeNo(String tradeNo) {
        return findByField(EasyPayOrder::getTradeNo, tradeNo);
    }

    public Optional<EasyPayOrder> findByOrderId(Long orderId) {
        return findByField(EasyPayOrder::getOrderId, orderId);
    }

    @IgnoreTenant
    public Optional<EasyPayOrder> findByIdNotTenant(Long id) {
        return findById(id);
    }

    @IgnoreTenant
    public Optional<EasyPayOrder> findByOrderIdNotTenant(Long orderId) {
        return findByField(EasyPayOrder::getOrderId, orderId);
    }
}
