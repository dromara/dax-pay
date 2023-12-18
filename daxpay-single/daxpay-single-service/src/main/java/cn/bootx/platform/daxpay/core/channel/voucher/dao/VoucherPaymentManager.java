package cn.bootx.platform.daxpay.core.channel.voucher.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherPaymentManager extends BaseManager<VoucherPaymentMapper, VoucherPayment> {

    /**
     * 根据支付id
     */
    public Optional<VoucherPayment> findByPaymentId(Long paymentId) {
        return this.findByField(VoucherPayment::getPaymentId, paymentId);
    }

}
