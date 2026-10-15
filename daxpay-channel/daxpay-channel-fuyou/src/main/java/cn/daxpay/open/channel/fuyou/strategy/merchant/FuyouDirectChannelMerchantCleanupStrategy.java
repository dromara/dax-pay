package cn.daxpay.open.channel.fuyou.strategy.merchant;

import cn.daxpay.open.channel.fuyou.dao.direct.FuyouDirectKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.direct.FuyouDirectKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友直连通道商户清理策略
///
/// 在通道商户删除时清理富友直连的扩展数据(直连密钥配置行)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final FuyouDirectKeyConfigManager fuyouDirectKeyConfigManager;

    /// 对应产品: 富友商户前置
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_DIRECT;
    }

    /// 清理指定通道商户号下富友直连的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fuyouDirectKeyConfigManager.deleteByField(FuyouDirectKeyConfig::getChannelMchNo, channelMchNo);
    }
}
