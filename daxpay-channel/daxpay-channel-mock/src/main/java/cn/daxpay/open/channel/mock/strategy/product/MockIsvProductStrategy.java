package cn.daxpay.open.channel.mock.strategy.product;

import cn.daxpay.open.channel.mock.strategy.MockProductCapabilityCatalog;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # Mock 模拟通道服务商产品策略
///
/// 模拟支付服务商产品(ProductEnum.MOCK_ISV)的支付产品策略, 与直连产品
/// [MockDirectProductStrategy] 共用 mock 通道(一通道双产品): 方式→能力声明与渠道声明
/// 均取自 [MockProductCapabilityCatalog], 两产品口径恒等。
/// 差异仅在形态声明: 服务商调用模式(ISV)+恒沙箱, 通道商户须先归属模拟服务商
/// ([cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo])。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvProductStrategy extends AbsProductStrategy {

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    /// 服务商模式: 支付以服务商身份发起, 子商户号即可定位调用配置
    @Override
    public boolean isIsv() {
        return true;
    }

    /// mock 恒为沙箱语义(业务数据标记, 不承担装配防线)
    @Override
    public boolean isSandbox() {
        return true;
    }

    /// 仅沙箱: 生效环境固定 sandbox, 不允许切换到生产
    @Override
    public boolean isSandboxOnly() {
        return true;
    }

    /// API 调用模式: 服务商
    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.ISV;
    }

    @Override
    public List<PayProviderEnum> supportedPayProviders() {
        return MockProductCapabilityCatalog.SUPPORTED_PROVIDERS;
    }

    @Override
    public Map<PayMethodEnum, List<PayCapabilityEnum>> methodCapabilityMapping() {
        return MockProductCapabilityCatalog.METHOD_CAP_MAP;
    }
}
