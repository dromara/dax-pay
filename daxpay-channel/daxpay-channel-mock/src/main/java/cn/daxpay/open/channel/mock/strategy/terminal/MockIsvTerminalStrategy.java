package cn.daxpay.open.channel.mock.strategy.terminal;

import cn.daxpay.open.channel.mock.service.MockTerminalService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟支付(服务商)终端报备策略
///
/// ProductEnum.MOCK_ISV 的终端报备策略: 共用 [MockTerminalStrategySupport] 的报备/注销/同步行为,
/// 仅声明产品归属; 服务商凭证由 [MockTerminalService] 经
/// [cn.daxpay.open.channel.mock.service.MockCredentialSupport] 按 product 反解填充。
@Slf4j
@Service
public class MockIsvTerminalStrategy extends MockTerminalStrategySupport {

    public MockIsvTerminalStrategy(MockTerminalService mockTerminalService) {
        super(mockTerminalService);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }
}
