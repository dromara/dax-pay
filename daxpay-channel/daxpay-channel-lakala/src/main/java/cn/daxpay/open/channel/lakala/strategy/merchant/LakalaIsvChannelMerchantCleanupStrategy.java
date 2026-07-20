package cn.daxpay.open.channel.lakala.strategy.merchant;

import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvChannelMerchantManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 拉卡拉通道商户清理策略
///
/// 在通道商户删除时清理拉卡拉的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final LakalaIsvChannelMerchantManager lakalaIsvChannelMerchantManager;

    /// 对应产品: 拉卡拉
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_PAY;
    }

    /// 清理指定通道商户号下拉卡拉的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        lakalaIsvChannelMerchantManager.deleteByField(LakalaIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
