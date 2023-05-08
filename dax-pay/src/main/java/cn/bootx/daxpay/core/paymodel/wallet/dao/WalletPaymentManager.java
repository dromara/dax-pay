package cn.bootx.daxpay.core.paymodel.wallet.dao;

import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.daxpay.core.paymodel.base.entity.BasePayment;
import cn.bootx.daxpay.core.paymodel.wallet.entity.WalletPayment;
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
