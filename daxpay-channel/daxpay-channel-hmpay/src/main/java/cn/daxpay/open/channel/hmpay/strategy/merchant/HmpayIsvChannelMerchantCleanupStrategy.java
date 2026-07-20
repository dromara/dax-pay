package cn.daxpay.open.channel.hmpay.strategy.merchant;

import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvChannelMerchantManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 河马付通道商户清理策略
///
/// 在通道商户删除时清理河马付的所有扩展数据。
///
/// 河马付底层走杉德通道(对应 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum#SAND_PAY]),
/// 但产品维度的清理策略按 [ProductEnum#HM_PAY] 注册, 与支付策略体系一致。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final HmpayIsvChannelMerchantManager hmpayIsvChannelMerchantManager;

    /// 对应产品: 河马付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    /// 清理指定通道商户号下河马付的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        hmpayIsvChannelMerchantManager.deleteByField(HmpayIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
