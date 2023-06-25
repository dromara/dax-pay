package cn.bootx.platform.daxpay.core.channel.alipay.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayment;
import cn.bootx.platform.daxpay.core.channel.base.entity.BasePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付宝
 *
 * @author xxm
 * @since 2021/2/26
 */
@Repository
@RequiredArgsConstructor
public class AliPaymentManager extends BaseManager<AliPaymentMapper, AliPayment> {

    public Optional<AliPayment> findByPaymentId(Long paymentId) {
        return this.findByField(BasePayment::getPaymentId, paymentId);
    }

}
