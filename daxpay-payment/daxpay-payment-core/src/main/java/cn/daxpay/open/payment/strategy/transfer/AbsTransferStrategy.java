package cn.daxpay.open.payment.strategy.transfer;

import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;

/// # 转账抽象策略
///
/// 转账按通道独立实现策略（微信/支付宝/抖音），策略为单例无状态，
/// 运行时数据通过 [TransferStrategyContext] 显式传递（与支付策略 [cn.daxpay.open.payment.strategy.pay.PayStrategyContext] 对称）。
/// 与支付策略不同：转账无 product 维度，按 [getChannel] 注册，不实现支付域
/// [cn.daxpay.open.payment.strategy.PaymentStrategy] 接口（避免被支付工厂按 product 索引扫入）。
public abstract class AbsTransferStrategy {

    /// 通道编码
    ///
    /// @return 通道编码, 如 wechat/alipay/douyin
    public abstract String getChannel();

    /// 转账前处理(各通道可选实现, 如通道特有参数校验/配置校验)
    ///
    /// @param param 转账参数
    public void doValidateParam(TransferParam param) {
    }

    /// 发起转账
    ///
    /// @param context 转账策略上下文(由编排层从具体容器装配; 通道特有字段可经回写)
    /// @return 转账结果(含映射后的状态)
    public abstract TransferResultBo doTransfer(TransferStrategyContext context);

    /// 查询通道转账状态
    ///
    /// @param context 转账策略上下文
    /// @return 转账同步结果(含映射后的状态)
    public abstract TransferResultBo doSync(TransferStrategyContext context);

    /// 关闭转账(各通道可选实现, 如支付宝撤销; 默认不支持, 抛业务异常)
    ///
    /// @param context 转账策略上下文
    public void doClose(TransferStrategyContext context) {
        // 该通道暂不支持关闭转账
        throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.channelNotSupport");
    }
}
