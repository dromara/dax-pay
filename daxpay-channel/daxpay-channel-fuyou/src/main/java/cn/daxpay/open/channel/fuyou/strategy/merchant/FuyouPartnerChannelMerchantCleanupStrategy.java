package cn.daxpay.open.channel.fuyou.strategy.merchant;

import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvChannelMerchantManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友合作方通道商户清理策略
///
/// 在通道商户删除时清理富友合作方的扩展数据。
/// 合作方与服务商共用 fuyou_isv_channel_merchant 绑定表(以行内 product 区分),
/// 按通道商户号清理天然产品隔离(通道商户号全局唯一)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPartnerChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final FuyouIsvChannelMerchantManager fuyouIsvChannelMerchantManager;

    /// 对应产品: 富友合作方
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PARTNER;
    }

    /// 清理指定通道商户号下富友合作方的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fuyouIsvChannelMerchantManager.deleteByField(FuyouIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
