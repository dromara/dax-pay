package cn.daxpay.open.channel.jlpay.strategy.merchant;

import cn.daxpay.open.channel.jlpay.dao.direct.JlpayDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 嘉联直连通道商户清理策略
///
/// 在通道商户删除时清理嘉联直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final JlpayDirectKeyConfigManager jlpayDirectKeyConfigManager;

    /// 对应产品: 嘉联
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.JL_PAY;
    }

    /// 清理指定通道商户号下嘉联直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        jlpayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
