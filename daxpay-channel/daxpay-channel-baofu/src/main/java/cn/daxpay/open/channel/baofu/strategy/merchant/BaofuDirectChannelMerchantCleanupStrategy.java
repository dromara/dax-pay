package cn.daxpay.open.channel.baofu.strategy.merchant;

import cn.daxpay.open.channel.baofu.dao.direct.BaofuDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 宝付直连通道商户清理策略
///
/// 在通道商户删除时清理宝付直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final BaofuDirectKeyConfigManager baofuDirectKeyConfigManager;

    /// 对应产品: 宝付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.BAOFU_PAY;
    }

    /// 清理指定通道商户号下宝付直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        baofuDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
