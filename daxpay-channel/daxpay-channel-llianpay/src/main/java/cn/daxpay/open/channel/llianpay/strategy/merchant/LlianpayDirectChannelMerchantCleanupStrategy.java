package cn.daxpay.open.channel.llianpay.strategy.merchant;

import cn.daxpay.open.channel.llianpay.dao.direct.LlianpayDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 连连直连通道商户清理策略
///
/// 在通道商户删除时清理连连直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final LlianpayDirectKeyConfigManager llianpayDirectKeyConfigManager;

    /// 对应产品: 连连支付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LLIAN_PAY;
    }

    /// 清理指定通道商户号下连连直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        llianpayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
