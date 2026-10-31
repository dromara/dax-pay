package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.dao.isv.MockIsvChannelMerchantManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 服务商通道商户清理策略
///
/// 在通道商户删除时清理服务商归属绑定私有表(mock_isv_channel_merchant),
/// 挂通用删除链形态对齐微信/支付宝服务商清理策略。
/// 与直连产品(mock_pay)互不影响: 直连产品无私有表, 无清理策略(未实现策略的 product 静默跳过)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    private final MockIsvChannelMerchantManager mockIsvChannelMerchantManager;

    /// 对应产品: 模拟支付(服务商)
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    /// 清理指定通道商户号下的服务商归属绑定
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        mockIsvChannelMerchantManager.deleteByField(MockIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
