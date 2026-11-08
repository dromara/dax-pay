package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.dao.alloc.MockAllocReceiverManager;
import cn.daxpay.open.channel.mock.dao.credential.MockDirectCredentialManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvChannelMerchantManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 服务商通道商户清理策略
///
/// 在通道商户删除时清理服务商归属绑定私有表(mock_isv_channel_merchant), 并复用
/// [AbsMockChannelMerchantCleanupStrategy] 清理两产品共享的直连接入密钥与分账接收方台账,
/// 挂通用删除链形态对齐微信/支付宝服务商清理策略。
/// 直连产品(mock_pay)的清理由 [MockPayChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
public class MockIsvChannelMerchantCleanupStrategy extends AbsMockChannelMerchantCleanupStrategy {

    private final MockIsvChannelMerchantManager mockIsvChannelMerchantManager;

    public MockIsvChannelMerchantCleanupStrategy(MockDirectCredentialManager mockDirectCredentialManager,
            MockAllocReceiverManager mockAllocReceiverManager,
            MockIsvChannelMerchantManager mockIsvChannelMerchantManager) {
        super(mockDirectCredentialManager, mockAllocReceiverManager);
        this.mockIsvChannelMerchantManager = mockIsvChannelMerchantManager;
    }

    /// 对应产品: 模拟支付(服务商)
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    /// 清理指定通道商户号下的共享私有数据与服务商归属绑定
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        // 两产品共享: 直连接入密钥 + 分账接收方台账
        super.deleteByChannelMchNo(channelMchNo);
        // 服务商产品私有: 归属绑定
        mockIsvChannelMerchantManager.deleteByField(MockIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }
}
