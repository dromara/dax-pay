package cn.bootx.platform.daxpay.service.core.channel.wallet.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.WalletPayOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Deprecated
public class WalletPaymentManager extends BaseManager<WalletPaymentMapper, WalletPayOrder> {

    public Optional<WalletPayOrder> findByPaymentId(Long paymentId) {
        return findByField(BasePayOrder::getPaymentId, paymentId);
    }

}
