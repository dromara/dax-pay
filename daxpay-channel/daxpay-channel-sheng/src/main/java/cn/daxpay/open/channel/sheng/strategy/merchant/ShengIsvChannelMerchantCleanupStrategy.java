package cn.daxpay.open.channel.sheng.strategy.merchant;

import cn.daxpay.open.channel.sheng.dao.ShengIsvChannelMerchantManager;
import cn.daxpay.open.channel.sheng.entity.ShengIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 盛付通服务商通道商户清理策略
///
/// 在通道商户删除时清理服务商模式的扩展数据(sheng_isv_channel_merchant 子商户绑定行)。
/// 服务商密钥(sheng_isv_key_config)为产品级全局共享, 不随通道商户删除。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final ShengIsvChannelMerchantManager shengIsvChannelMerchantManager;

    /// 对应产品: 盛付通服务商模式
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_ISV;
    }

    /// 清理指定通道商户号下的子商户绑定行(不动产品级全局密钥)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        shengIsvChannelMerchantManager.deleteByField(ShengIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
