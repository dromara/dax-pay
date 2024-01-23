package cn.bootx.platform.daxpay.service.core.channel.alipay.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付宝支付订单
 *
 * @author xxm
 * @since 2021/2/26
 */
@Deprecated
@Repository
@RequiredArgsConstructor
public class AliPayOrderManager extends BaseManager<AliPayOrderMapper, AliPayOrder> {

    public Optional<AliPayOrder> findByPaymentId(Long paymentId) {
        return this.findByField(BasePayOrder::getPaymentId, paymentId);
    }

}
