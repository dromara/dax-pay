package cn.daxpay.open.channel.fuyou.cleanup.isv;

import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvChannelMerchantManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友通道商户清理策略
///
/// 在通道商户删除时清理富友的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final FuyouIsvChannelMerchantManager fuyouIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#FUYOU_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.FUYOU_PAY.getCode();
    }

    /// 清理指定通道商户号下富友的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fuyouIsvChannelMerchantManager.deleteByField(FuyouIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
