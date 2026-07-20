package cn.daxpay.open.channel.hkrt.strategy.merchant;

import cn.daxpay.open.channel.hkrt.dao.isv.HkrtIsvChannelMerchantManager;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 海科融通通道商户清理策略
///
/// 在通道商户删除时清理海科融通的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final HkrtIsvChannelMerchantManager hkrtIsvChannelMerchantManager;

    /// 对应产品: 海科融通
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HKRT_PAY;
    }

    /// 清理指定通道商户号下海科融通的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        hkrtIsvChannelMerchantManager.deleteByField(HkrtIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
