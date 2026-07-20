package cn.daxpay.open.channel.ums.cleanup.direct;

import cn.daxpay.open.channel.ums.dao.direct.UmsDirectKeyConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 银联商务直连通道商户清理策略
///
/// 在通道商户删除时清理银联商务直连的所有扩展数据（直连配置表）。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final UmsDirectKeyConfigManager umsDirectKeyConfigManager;

    /// 通道编码（对应 [ChannelEnum#UMS_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.UMS_PAY.getCode();
    }

    /// 清理指定通道商户号下银联商务直连的所有扩展数据（直连配置表）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        umsDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
