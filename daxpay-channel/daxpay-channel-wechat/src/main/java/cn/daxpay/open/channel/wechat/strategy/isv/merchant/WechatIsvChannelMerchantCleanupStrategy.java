package cn.daxpay.open.channel.wechat.strategy.isv.merchant;

import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.payment.wx.service.channel.WxChannelAppCapabilityService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信服务商通道商户清理策略
///
/// 在通道商户删除时清理:
/// - 服务商特约商户扩展表
/// - 主数据通道能力绑([WxChannelAppCapabilityService#deleteByChannelMchNo])
///
/// 与 [cn.daxpay.open.channel.wechat.strategy.direct.merchant.WechatDirectChannelMerchantCleanupStrategy]
/// 分属不同 product(`WECHAT_ISV` vs `WECHAT_PAY`), 通过 [cn.daxpay.open.payment.strategy.PaymentStrategyFactory#findOptionallyByProduct]
/// 按 product 一对一查找, 互不影响。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final WxChannelAppCapabilityService wxChannelAppCapabilityService;

    /// 对应产品: 微信支付服务商
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.WECHAT_ISV;
    }

    /// 清理指定通道商户号下微信服务商的扩展与主数据能力绑
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        wechatIsvChannelMerchantManager.deleteByField(WechatIsvChannelMerchant::getChannelMchNo, channelMchNo);
        // 主数据: 通道商户 × 能力绑
        wxChannelAppCapabilityService.deleteByChannelMchNo(channelMchNo);
    }
}
