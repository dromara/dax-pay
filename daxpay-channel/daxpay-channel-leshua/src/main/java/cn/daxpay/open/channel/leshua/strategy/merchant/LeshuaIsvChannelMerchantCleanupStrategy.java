package cn.daxpay.open.channel.leshua.strategy.merchant;

import cn.daxpay.open.channel.leshua.dao.isv.LeshuaIsvChannelMerchantManager;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 乐刷通道商户清理策略
///
/// 在通道商户删除时清理乐刷的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final LeshuaIsvChannelMerchantManager leshuaIsvChannelMerchantManager;

    /// 对应产品: 乐刷
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LESHUA_PAY;
    }

    /// 清理指定通道商户号下乐刷的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        leshuaIsvChannelMerchantManager.deleteByField(LeshuaIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
