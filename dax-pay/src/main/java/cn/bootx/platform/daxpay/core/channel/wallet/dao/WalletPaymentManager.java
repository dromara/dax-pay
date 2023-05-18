package cn.bootx.platform.daxpay.core.channel.wallet.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletPayment;
import cn.bootx.platform.daxpay.core.channel.base.entity.BasePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WalletPaymentManager extends BaseManager<WalletPaymentMapper, WalletPayment> {

    public Optional<WalletPayment> findByPaymentId(Long paymentId) {
        return findByField(BasePayment::getPaymentId, paymentId);
    }

}
