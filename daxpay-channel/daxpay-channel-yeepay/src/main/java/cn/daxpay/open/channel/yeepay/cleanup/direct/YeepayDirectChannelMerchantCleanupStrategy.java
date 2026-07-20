package cn.daxpay.open.channel.yeepay.cleanup.direct;

import cn.daxpay.open.channel.yeepay.dao.direct.YeepayDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易宝直连通道商户清理策略
///
/// 在通道商户删除时清理易宝直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final YeepayDirectKeyConfigManager yeepayDirectKeyConfigManager;

    /// 通道编码（对应 [ChannelEnum#YEE_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.YEE_PAY.getCode();
    }

    /// 清理指定通道商户号下易宝直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        yeepayDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
