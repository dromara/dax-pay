package cn.daxpay.open.channel.alipay.strategy.isv.merchant;

import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 支付宝服务商通道商户清理策略
///
/// 在通道商户删除时清理支付宝服务商相关的扩展表（子商户授权关系）。
///
/// 与 [cn.daxpay.open.channel.alipay.strategy.direct.merchant.AlipayDirectChannelMerchantCleanupStrategy]
/// 分属不同 product(`ALIPAY_ISV` vs `ALIPAY`), 通过 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory#findOptionallyByProduct]
/// 按 product 一对一查找, 互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;

    /// 对应产品: 支付宝服务商
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    /// 清理指定通道商户号下支付宝服务商的扩展数据（子商户授权关系）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        alipayIsvChannelMerchantManager.deleteByField(AlipayIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
