package cn.daxpay.open.channel.vbill.cleanup.isv;

import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvChannelMerchantManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 随行付通道商户清理策略
///
/// 在通道商户删除时清理随行付的所有扩展数据。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final VbillIsvChannelMerchantManager vbillIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#VBILL_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.VBILL_PAY.getCode();
    }

    /// 清理指定通道商户号下随行付的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        vbillIsvChannelMerchantManager.deleteByField(VbillIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
