package cn.daxpay.open.channel.fuyou.strategy.merchant;

import cn.daxpay.open.channel.fuyou.dao.rsa.FuyouRsaKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友互联网直连通道商户清理策略
///
/// 在通道商户删除时清理富友互联网直连的扩展数据(RSA 密钥配置行)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final FuyouRsaKeyConfigManager fuyouRsaKeyConfigManager;

    /// 对应产品: 富友互联网直连
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_INTERNET;
    }

    /// 清理指定通道商户号下富友互联网直连的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fuyouRsaKeyConfigManager.deleteByField(FuyouRsaKeyConfig::getChannelMchNo, channelMchNo);
    }
}
