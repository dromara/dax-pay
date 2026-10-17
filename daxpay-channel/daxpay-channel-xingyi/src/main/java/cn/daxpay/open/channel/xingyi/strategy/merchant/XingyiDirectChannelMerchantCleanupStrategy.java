package cn.daxpay.open.channel.xingyi.strategy.merchant;

import cn.daxpay.open.channel.xingyi.dao.direct.XingyiDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 星驿付直连通道商户清理策略
///
/// 在通道商户删除时清理星驿付直连的所有扩展数据（直连配置表, 两环境记录一并清理）。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final XingyiDirectKeyConfigManager xingyiDirectKeyConfigManager;

    /// 对应产品: 星驿付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.XINGYI_PAY;
    }

    /// 清理指定通道商户号下星驿付直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        xingyiDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
