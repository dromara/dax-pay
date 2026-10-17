package cn.daxpay.open.channel.duolabao.strategy.merchant;

import cn.daxpay.open.channel.duolabao.dao.direct.DuolabaoDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 哆啦宝直连通道商户清理策略
///
/// 在通道商户删除时清理哆啦宝直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final DuolabaoDirectKeyConfigManager duolabaoDirectKeyConfigManager;

    /// 对应产品: 哆啦宝
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DUOLABAO_PAY;
    }

    /// 清理指定通道商户号下哆啦宝直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        duolabaoDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
