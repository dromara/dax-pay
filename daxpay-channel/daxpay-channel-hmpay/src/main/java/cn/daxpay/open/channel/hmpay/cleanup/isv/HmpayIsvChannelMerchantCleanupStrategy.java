package cn.daxpay.open.channel.hmpay.cleanup.isv;

import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvChannelMerchantManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 河马付通道商户清理策略
///
/// 在通道商户删除时清理河马付的所有扩展数据。
///
/// 注意：河马付底层走杉德通道，[getChannel] 返回 [ChannelEnum#SAND_PAY]。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final HmpayIsvChannelMerchantManager hmpayIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#SAND_PAY]，河马付底层走杉德通道）
    @Override
    public String getChannel() {
        return ChannelEnum.SAND_PAY.getCode();
    }

    /// 清理指定通道商户号下河马付的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        hmpayIsvChannelMerchantManager.deleteByField(HmpayIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
