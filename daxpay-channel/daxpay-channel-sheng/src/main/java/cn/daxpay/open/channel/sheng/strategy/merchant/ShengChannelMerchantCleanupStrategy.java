package cn.daxpay.open.channel.sheng.strategy.merchant;

import cn.daxpay.open.channel.sheng.dao.ShengKeyConfigManager;
import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 盛付通通道商户清理策略
///
/// 在通道商户删除时清理盛付通的所有扩展数据(sheng_key_config)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final ShengKeyConfigManager shengKeyConfigManager;

    /// 对应产品: 盛付通
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_PAY;
    }

    /// 清理指定通道商户号下盛付通的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        shengKeyConfigManager.deleteByField(ShengKeyConfig::getChannelMchNo, channelMchNo);
    }
}
