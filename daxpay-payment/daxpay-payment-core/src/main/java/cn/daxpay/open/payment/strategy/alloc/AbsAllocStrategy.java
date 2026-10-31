package cn.daxpay.open.payment.strategy.alloc;

import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.enums.AllocCapabilityEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;

import java.util.Set;

/// # 分账抽象策略基类
///
/// 策略为单例无状态, 运行时数据通过 [AllocStrategyContext] 显式传递。
/// 按 [getChannel] 注册, 不实现支付域 PaymentStrategy 接口(避免被支付工厂按 product 索引扫入),
/// 与 [cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy] 同模式。
///
/// 能力模型: 实现 [doAlloc] 即具备基础订单分账能力(下单链路降级判定依据);
/// 增量能力(多次分账/独立完结)由 [getCapabilities] 声明, 未声明的通道视为"单次即完结"档。
public abstract class AbsAllocStrategy {

    /// 所属通道编码(alipay/wechat/douyin)
    public abstract String getChannel();

    /// 增量能力声明(默认空 = 单次即完结档: 首次分账即解冻剩余, 不可追加)
    ///
    /// 多次分账档需声明 [AllocCapabilityEnum#MULTI] + [AllocCapabilityEnum#FINISH] 并实现 [doFinish]。
    public Set<AllocCapabilityEnum> getCapabilities() {
        return Set.of();
    }

    /// 是否具备指定能力
    public final boolean supports(AllocCapabilityEnum capability) {
        return getCapabilities().contains(capability);
    }

    /// 发起前校验(各通道可选实现, 如校验接收方类型是否该通道支持)
    public void doValidateParam(AllocStrategyContext context) {
    }

    /// 发起分账
    ///
    /// 返回 [AllocResultBo], 含逐明细结果。
    /// 各明细 result 为 pending(异步处理中) / success(即时成功) / fail(即时失败)。
    /// 整体异常(非逐明细)请抛出, 编排层会置分账单为 fail。
    ///
    /// 完结模式: 读取分账单的完结模式([cn.daxpay.open.payment.trade.alloc.entity.AllocOrder#getFinishMode]),
    /// auto 随单解冻剩余(单次即完结), manual 保持冻结(多次分账)。
    public abstract AllocResultBo doAlloc(AllocStrategyContext context);

    /// 发起完结(解冻剩余冻结资金给商户本身)
    ///
    /// 仅声明 [AllocCapabilityEnum#FINISH] 能力的通道需实现; 默认抛不支持。
    /// 完结单(allocType=finish)无接收方明细, 返回 [AllocResultBo#getFinishState] 推进状态。
    public AllocResultBo doFinish(AllocStrategyContext context) {
        // 该通道不支持独立完结分账
        throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE, "pay.error.alloc.finishChannelNotSupport");
    }

    /// 同步查询分账状态
    ///
    /// 返回 [AllocResultBo], 含逐明细最新结果; 完结单返回 [AllocResultBo#getFinishState]。
    /// 同步失败(通道不可达等)请抛出异常, 编排层保持 processing 由定时任务兜底。
    public abstract AllocResultBo doSync(AllocStrategyContext context);
}
