package cn.daxpay.open.payment.trade.alloc.runtime.service;

import cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy;
import cn.daxpay.open.payment.strategy.alloc.AllocStrategyFactory;
import cn.daxpay.open.payment.trade.alloc.enums.AllocCapabilityEnum;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 分账能力判定服务
///
/// 下单链路的分账能力收口: 判定支付产品所属通道是否支持分账。
/// 判定依据为通道白名单(该通道是否已实现 [AbsAllocStrategy] 策略,
/// 当前为微信/支付宝/抖音三通道)。
///
/// 产品不支持时的处理策略为降级不阻断: 交易分账状态记为
/// [cn.daxpay.open.payment.trade.alloc.enums.TradeAllocStatusEnum#UNSUPPORTED],
/// 出站参数不再透传分账标识, 支付按普通收款完成。
///
/// 增量能力(多次分账 [AllocCapabilityEnum#MULTI] / 独立完结 [AllocCapabilityEnum#FINISH])
/// 由策略 [AbsAllocStrategy#getCapabilities] 声明: 未声明 MULTI 的通道为"单次即完结"档,
/// 分账发起后剩余资金即解冻, 不可追加分账。
///
/// 注意: 白名单是必要条件而非充分条件——通道商户还须在通道侧开通分账权限
/// (如微信 profit_sharing 权限), 否则带分账标识的支付请求会被通道拒绝;
/// 该权限属商户自身配置, 系统不做链路级校验(设计决策, 见
/// `_doc/design/码牌分账与分账降级设计.md`)。
@Service
public class AllocCapabilityService {

    /// 通道是否支持分账(基础能力: 存在策略实现)
    public boolean supports(String channel) {
        return AllocStrategyFactory.findOptionally(channel).isPresent();
    }

    /// 通道是否具备指定增量能力(多次分账/独立完结等; 无策略 = 不具备任何能力)
    public boolean supports(String channel, AllocCapabilityEnum capability) {
        Optional<AbsAllocStrategy> strategyOpt = AllocStrategyFactory.findOptionally(channel);
        return strategyOpt.map(strategy -> strategy.supports(capability)).orElse(false);
    }
}
