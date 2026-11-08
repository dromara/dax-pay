package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.dao.alloc.MockAllocReceiverManager;
import cn.daxpay.open.channel.mock.dao.credential.MockDirectCredentialManager;
import cn.daxpay.open.channel.mock.entity.alloc.MockAllocReceiver;
import cn.daxpay.open.channel.mock.entity.credential.MockDirectCredential;
import cn.daxpay.open.payment.strategy.merchant.ChannelMerchantCleanupStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 通道商户清理策略基类
///
/// mock 一通道双产品(mock_pay 直连 / mock_isv 服务商)共享两份按通道商户号挂靠的私有数据:
/// 直连商户级接入密钥(mock_direct_credential)与分账接收方台账(mock_alloc_receiver, 两产品共用一张表)。
/// 本类封装共享删除逻辑, 由各产品子类实现 [cn.daxpay.open.payment.strategy.PaymentStrategy#getProduct]
/// 注册为独立 Spring Bean: 清理 SPI 按 product 一对一查找, 每个产品都必须有实现,
/// 否则该产品通道商户删除时不触发清理(范式同 UMS 多产品共享扩展表的 `UmsDirectChannelMerchantCleanupStrategy`)。
///
/// @see MockPayChannelMerchantCleanupStrategy
/// @see MockIsvChannelMerchantCleanupStrategy
@RequiredArgsConstructor
public abstract class AbsMockChannelMerchantCleanupStrategy implements ChannelMerchantCleanupStrategy {

    protected final MockDirectCredentialManager mockDirectCredentialManager;
    protected final MockAllocReceiverManager mockAllocReceiverManager;

    /// 清理指定通道商户号下两产品共享的私有数据(直连接入密钥 + 分账接收方台账), 按通道商户号条件删除, 幂等
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        mockDirectCredentialManager.deleteByField(MockDirectCredential::getChannelMchNo, channelMchNo);
        mockAllocReceiverManager.deleteByField(MockAllocReceiver::getChannelMchNo, channelMchNo);
    }
}
