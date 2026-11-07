package cn.daxpay.open.channel.mock.strategy.terminal;

import cn.daxpay.open.channel.mock.service.MockTerminalService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟支付(直连)终端报备策略
///
/// ProductEnum.MOCK_PAY 的终端报备策略: 共用 [MockTerminalStrategySupport] 的报备/注销/同步行为,
/// 仅声明产品归属(策略工厂按 product 索引, 双产品各一个 Bean)。
@Slf4j
@Service
public class MockPayTerminalStrategy extends MockTerminalStrategySupport {

    public MockPayTerminalStrategy(MockTerminalService mockTerminalService) {
        super(mockTerminalService);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_PAY;
    }
}
