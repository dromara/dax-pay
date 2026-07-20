package cn.daxpay.open.channel.vbill.strategy.merchant;

import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvChannelMerchantManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 随行付通道商户清理策略
///
/// 在通道商户删除时清理随行付的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final VbillIsvChannelMerchantManager vbillIsvChannelMerchantManager;

    /// 对应产品: 随行付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.VBILL_PAY;
    }

    /// 清理指定通道商户号下随行付的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        vbillIsvChannelMerchantManager.deleteByField(VbillIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
