package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.dao.alloc.MockAllocReceiverManager;
import cn.daxpay.open.channel.mock.dao.credential.MockDirectCredentialManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # Mock 模拟支付(直连)通道商户清理策略
///
/// 对应 [ProductEnum#MOCK_PAY], 复用 [AbsMockChannelMerchantCleanupStrategy] 的共享删除逻辑
/// (直连商户级接入密钥 + 分账接收方台账); 直连产品无其他私有表。
@Service
public class MockPayChannelMerchantCleanupStrategy extends AbsMockChannelMerchantCleanupStrategy {

    public MockPayChannelMerchantCleanupStrategy(MockDirectCredentialManager mockDirectCredentialManager,
            MockAllocReceiverManager mockAllocReceiverManager) {
        super(mockDirectCredentialManager, mockAllocReceiverManager);
    }

    /// 对应产品: 模拟支付(直连)
    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_PAY;
    }
}
