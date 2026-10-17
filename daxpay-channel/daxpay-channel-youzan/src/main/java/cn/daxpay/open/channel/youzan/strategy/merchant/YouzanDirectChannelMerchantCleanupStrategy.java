package cn.daxpay.open.channel.youzan.strategy.merchant;

import cn.daxpay.open.channel.youzan.dao.direct.YouzanDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 有赞直连通道商户清理策略
///
/// 在通道商户删除时清理有赞直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final YouzanDirectKeyConfigManager youzanDirectKeyConfigManager;

    /// 对应产品: 有赞
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YOUZAN_PAY;
    }

    /// 清理指定通道商户号下有赞直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        youzanDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
