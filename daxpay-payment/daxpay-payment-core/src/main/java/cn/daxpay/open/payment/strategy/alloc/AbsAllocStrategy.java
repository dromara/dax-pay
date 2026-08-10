package cn.daxpay.open.payment.strategy.alloc;

/// # 分账抽象策略基类
///
/// 策略为单例无状态, 运行时数据通过 [AllocStrategyContext] 显式传递。
/// 按 [getChannel] 注册, 不实现支付域 PaymentStrategy 接口(避免被支付工厂按 product 索引扫入),
/// 与 [cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy] 同模式。
public abstract class AbsAllocStrategy {

    /// 所属通道编码(alipay/wechat/douyin)
    public abstract String getChannel();

    /// 发起前校验(各通道可选实现, 如校验接收方类型是否该通道支持)
    public void doValidateParam(AllocStrategyContext context) {
    }

    /// 发起分账
    ///
    /// 返回 [cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo], 含逐明细结果。
    /// 各明细 result 为 pending(异步处理中) / success(即时成功) / fail(即时失败)。
    /// 整体异常(非逐明细)请抛出, 编排层会置分账单为 fail。
    public abstract cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo doAlloc(AllocStrategyContext context);

    /// 同步查询分账状态
    ///
    /// 返回 [cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo], 含逐明细最新结果。
    /// 同步失败(通道不可达等)请抛出异常, 编排层保持 processing 由定时任务兜底。
    public abstract cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo doSync(AllocStrategyContext context);
}
