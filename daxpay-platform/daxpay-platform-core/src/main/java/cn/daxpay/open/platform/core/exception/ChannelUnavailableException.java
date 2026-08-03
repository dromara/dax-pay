package cn.daxpay.open.platform.core.exception;

/// # 通道服务不可用异常
///
/// 通道子应用连接未建立时抛出(如连接被拒绝/主机不可达/DNS 解析失败),
/// 表示请求根本未到达通道子应用, 通道真实结果确定(未受理),
/// 支付/退款主流程据此将订单置为 FAIL(资金确定未动)。
///
/// 与 [ChannelResultUnknownException] 的区别:
/// 后者用于请求已发出但响应未收到(读超时/响应中断)或通道主动报结果未知的场景,
/// 资金可能已动, 主流程保持中间态交由定时同步纠正。
///
/// @see PayFailureException
public class ChannelUnavailableException extends PayFailureException {

    /// 指定 messageKey
    public ChannelUnavailableException(String messageKey) {
        super(messageKey);
    }

    /// 指定 messageKey 并保留原始异常堆栈(用于连接异常等场景)
    public ChannelUnavailableException(String messageKey, Throwable cause) {
        super(messageKey);
        // BizException 构造器未设置 cause, 此处安全调用 initCause 保留堆栈
        if (cause != null) {
            initCause(cause);
        }
    }
}
