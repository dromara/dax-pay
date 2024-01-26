package cn.bootx.platform.daxpay.service.core.channel.voucher.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherPayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Deprecated
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherPaymentManager extends BaseManager<VoucherPayOrderMapper, VoucherPayOrder> {

    /**
     * 根据支付id查询
     */
    public Optional<VoucherPayOrder> findByPaymentId(Long paymentId) {
        return this.findByField(VoucherPayOrder::getPaymentId, paymentId);
    }

}
