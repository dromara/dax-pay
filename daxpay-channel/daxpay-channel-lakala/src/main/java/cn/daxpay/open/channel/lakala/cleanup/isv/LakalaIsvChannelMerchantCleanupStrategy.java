package cn.daxpay.open.channel.lakala.cleanup.isv;

import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvChannelMerchantManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 拉卡拉通道商户清理策略
///
/// 在通道商户删除时清理拉卡拉的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final LakalaIsvChannelMerchantManager lakalaIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#LAKALA_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.LAKALA_PAY.getCode();
    }

    /// 清理指定通道商户号下拉卡拉的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        lakalaIsvChannelMerchantManager.deleteByField(LakalaIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
