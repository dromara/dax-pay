package cn.daxpay.open.channel.leshua.cleanup.isv;

import cn.daxpay.open.channel.leshua.dao.isv.LeshuaIsvChannelMerchantManager;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 乐刷通道商户清理策略
///
/// 在通道商户删除时清理乐刷的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final LeshuaIsvChannelMerchantManager leshuaIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#LESHUA_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.LESHUA_PAY.getCode();
    }

    /// 清理指定通道商户号下乐刷的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        leshuaIsvChannelMerchantManager.deleteByField(LeshuaIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
