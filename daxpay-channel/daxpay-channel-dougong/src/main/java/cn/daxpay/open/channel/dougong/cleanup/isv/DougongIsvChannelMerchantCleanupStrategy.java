package cn.daxpay.open.channel.dougong.cleanup.isv;

import cn.daxpay.open.channel.dougong.dao.isv.DougongIsvChannelMerchantManager;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 斗拱通道商户清理策略
///
/// 在通道商户删除时清理斗拱的所有扩展数据。
///
/// 注意：斗拱与 [cn.daxpay.open.channel.adapay.cleanup.direct.AdapayDirectChannelMerchantCleanupStrategy]
/// 共享同一通道编码 [ChannelEnum#HUIFU]，由 [ChannelMerchantCleanupStrategyFactory] 按 channel
/// 过滤后遍历调用各自实现，互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final DougongIsvChannelMerchantManager dougongIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#HUIFU]，与 Adapay 共享）
    @Override
    public String getChannel() {
        return ChannelEnum.HUIFU.getCode();
    }

    /// 清理指定通道商户号下斗拱的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        dougongIsvChannelMerchantManager.deleteByField(DougongIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
