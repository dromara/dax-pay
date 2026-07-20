package cn.daxpay.open.channel.fuyou.strategy.merchant;

import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvChannelMerchantManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友通道商户清理策略
///
/// 在通道商户删除时清理富友的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final FuyouIsvChannelMerchantManager fuyouIsvChannelMerchantManager;

    /// 对应产品: 富友
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PAY;
    }

    /// 清理指定通道商户号下富友的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fuyouIsvChannelMerchantManager.deleteByField(FuyouIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
