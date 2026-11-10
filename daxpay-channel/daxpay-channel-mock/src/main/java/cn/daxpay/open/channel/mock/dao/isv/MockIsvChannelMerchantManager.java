package cn.daxpay.open.channel.mock.dao.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # Mock 模拟服务商通道商户绑定
///
/// 仅承载 mock_isv 通道商户与全局唯一模拟服务商的归属登记; 多服务商模式取消后
/// 支付/凭证装配不再按本表反查(直接取单例 isvNo), 本表保留建档留痕与通道商户删除清理用途
/// ([cn.daxpay.open.channel.mock.strategy.MockIsvChannelMerchantCleanupStrategy])。
@Repository
public class MockIsvChannelMerchantManager extends BaseManager<MockIsvChannelMerchantMapper, MockIsvChannelMerchant> {
}
