package cn.daxpay.open.channel.alipay.cleanup.isv;

import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 支付宝服务商通道商户清理策略
///
/// 在通道商户删除时清理支付宝服务商相关的扩展表（子商户授权关系）。
///
/// 注意：与 [cn.daxpay.open.channel.alipay.cleanup.direct.AlipayDirectChannelMerchantCleanupStrategy]
/// 共享同一通道编码 [ChannelEnum#ALIPAY]，由 [ChannelMerchantCleanupStrategyFactory] 按 channel
/// 过滤后遍历调用各自实现，互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#ALIPAY]，与直连共享）
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }

    /// 清理指定通道商户号下支付宝服务商的扩展数据（子商户授权关系）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        alipayIsvChannelMerchantManager.deleteByField(AlipayIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
