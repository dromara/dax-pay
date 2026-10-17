package cn.daxpay.open.channel.fuyou.strategy.merchant;

import cn.daxpay.open.channel.fuyou.dao.rsa.FuyouRsaKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友 H5 聚合收银台通道商户清理策略
///
/// 在通道商户删除时清理富友 H5 聚合收银台的扩展数据(RSA 密钥配置行)。
/// 互联网直连与 H5 收银台共用 fuyou_rsa_key_config 表(以行内 product 区分),
/// 按通道商户号清理天然产品隔离(通道商户号全局唯一)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouH5ChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final FuyouRsaKeyConfigManager fuyouRsaKeyConfigManager;

    /// 对应产品: 富友 H5 聚合收银台
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_H5;
    }

    /// 清理指定通道商户号下富友 H5 收银台的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fuyouRsaKeyConfigManager.deleteByField(FuyouRsaKeyConfig::getChannelMchNo, channelMchNo);
    }
}
