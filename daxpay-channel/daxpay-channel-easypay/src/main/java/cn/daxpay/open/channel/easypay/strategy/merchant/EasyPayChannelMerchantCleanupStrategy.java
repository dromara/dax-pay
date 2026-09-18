package cn.daxpay.open.channel.easypay.strategy.merchant;

import cn.daxpay.open.channel.easypay.dao.EasyPayKeyConfigManager;
import cn.daxpay.open.channel.easypay.entity.EasyPayKeyConfig;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易支付通道商户清理策略
///
/// 在通道商户删除时清理易支付的所有扩展数据(easy_pay_key_config)。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final EasyPayKeyConfigManager easyPayKeyConfigManager;

    /// 对应产品: 易支付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EASY_PAY;
    }

    /// 清理指定通道商户号下易支付的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        easyPayKeyConfigManager.deleteByField(EasyPayKeyConfig::getChannelMchNo, channelMchNo);
    }
}
