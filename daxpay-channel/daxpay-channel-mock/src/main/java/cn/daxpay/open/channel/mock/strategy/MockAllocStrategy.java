package cn.daxpay.open.channel.mock.strategy;

import cn.daxpay.open.channel.mock.service.MockAllocService;
import cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyContext;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.enums.AllocCapabilityEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/// # Mock 模拟通道分账策略
///
/// 按 channel=mock 注册(与支付宝 [cn.daxpay.open.channel.alipay.strategy.direct.alloc.AlipayAllocStrategy] 同构,
/// 分账策略按通道而非产品索引)。mock 零资料接入无通道凭证可组装, 执行委托给 [MockAllocService]。
///
/// mock 分账为纯查询式(无异步回调), 状态推进靠同步查询与 mock 确认页/GUI 人工推终态;
/// 支持多次分账与独立完结(A 档通道, 能力声明对齐支付宝口径)。
/// mock 不感知接收方类型(零资料接入, doValidateParam 走基类默认不校验)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockAllocStrategy extends AbsAllocStrategy {

    private final MockAllocService mockAllocService;

    @Override
    public String getChannel() {
        return "mock";
    }

    /// 增量能力: 多次分账 + 独立完结(alloc-finish 独立端点解冻)
    @Override
    public Set<AllocCapabilityEnum> getCapabilities() {
        return Set.of(AllocCapabilityEnum.MULTI, AllocCapabilityEnum.FINISH);
    }

    @Override
    public AllocResultBo doAlloc(AllocStrategyContext context) {
        return mockAllocService.alloc(context.getAllocOrder(), context.getDetails());
    }

    /// 发起完结(解冻剩余冻结资金给商户本身, 无接收方明细)
    @Override
    public AllocResultBo doFinish(AllocStrategyContext context) {
        return mockAllocService.finish(context.getAllocOrder());
    }

    @Override
    public AllocResultBo doSync(AllocStrategyContext context) {
        // 完结单无明细, 按整体完结状态推进; 普通单按整单状态映射全部明细
        return mockAllocService.sync(context.getAllocOrder(), context.getDetails());
    }
}
