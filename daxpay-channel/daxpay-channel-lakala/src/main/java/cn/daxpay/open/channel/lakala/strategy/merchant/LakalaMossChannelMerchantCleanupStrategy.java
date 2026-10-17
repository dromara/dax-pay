package cn.daxpay.open.channel.lakala.strategy.merchant;

import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvChannelMerchantManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 拉卡拉 MOSS 通道商户清理策略
///
/// 在通道商户删除时清理拉卡拉MOSS产品的扩展数据。
///
/// MOSS 与服务商模式共用 `lakala_isv_channel_merchant` 绑定表(以 product 区分),
/// channelMchNo 全局唯一(LAKALA+雪花), 删除范围天然按通道商户隔离, 与服务商模式清理策略同构。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final LakalaIsvChannelMerchantManager lakalaIsvChannelMerchantManager;

    /// 对应产品: 拉卡拉MOSS
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_MOSS;
    }

    /// 清理指定通道商户号下拉卡拉MOSS的扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        lakalaIsvChannelMerchantManager.deleteByField(LakalaIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
