package cn.daxpay.open.channel.yizhifubj.strategy.merchant;

import cn.daxpay.open.channel.yizhifubj.dao.direct.YizhifubjDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 一直付直连通道商户清理策略
///
/// 在通道商户删除时清理一直付直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final YizhifubjDirectKeyConfigManager yizhifubjDirectKeyConfigManager;

    /// 对应产品: 一直付
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YIZHIFU_PAY;
    }

    /// 清理指定通道商户号下一直付直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        yizhifubjDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
