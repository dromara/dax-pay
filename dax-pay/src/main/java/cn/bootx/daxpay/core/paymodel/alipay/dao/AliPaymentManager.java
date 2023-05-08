package cn.bootx.daxpay.core.paymodel.alipay.dao;

import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.daxpay.core.paymodel.alipay.entity.AliPayment;
import cn.bootx.daxpay.core.paymodel.base.entity.BasePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付宝
 *
 * @author xxm
 * @date 2021/2/26
 */
@Repository
@RequiredArgsConstructor
public class AliPaymentManager extends BaseManager<AliPaymentMapper, AliPayment> {

    public Optional<AliPayment> findByPaymentId(Long paymentId) {
        return this.findByField(BasePayment::getPaymentId, paymentId);
    }

}
