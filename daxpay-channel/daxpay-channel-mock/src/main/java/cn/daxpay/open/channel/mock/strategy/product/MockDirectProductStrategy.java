package cn.daxpay.open.channel.mock.strategy.product;

import cn.daxpay.open.channel.mock.strategy.MockProductCapabilityCatalog;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelApiCallMode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelPayIdType;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # Mock 模拟通道直连产品策略
///
/// 模拟支付直连产品(ProductEnum.MOCK_PAY)的支付产品策略: 方式→能力声明取自
/// [MockProductCapabilityCatalog](平台 21 项真实支付方式全量, 四渠道),
/// 覆盖异步(扫码/H5/PC/JSAPI/小程序挂起确认页)与同步(付款码即时)两种时序,
/// 子应用支付方式矩阵按 provider×way 拟真。
/// 收银台按四渠道任意方式下单皆可路由到 mock, 收银台零改动。
///
/// 与 [MockIsvProductStrategy] 共用同一套方式→能力声明(一通道双产品, 口径恒等),
/// 差异仅在形态声明: 本类为直连调用模式(MCH)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockDirectProductStrategy extends AbsProductStrategy {

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_PAY;
    }

    /// mock 恒为沙箱语义(业务数据标记, 不承担装配防线)
    @Override
    public boolean isSandbox() {
        return true;
    }

    /// 直连模式: 按通道商户维度调用子应用
    @Override
    public ChannelApiCallMode getApiCallMode() {
        return ChannelApiCallMode.MCH;
    }

    /// 支付标识类型: 通道商户号
    @Override
    public ChannelPayIdType getPayIdType() {
        return ChannelPayIdType.MCH;
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
