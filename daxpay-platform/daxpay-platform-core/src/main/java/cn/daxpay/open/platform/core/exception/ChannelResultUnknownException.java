package cn.daxpay.open.platform.core.exception;

/// # 通道结果未知异常
///
/// 通道调用结果无法明确判定时抛出(如网络超时、连接异常、付款码 USER_PAYING / authCode 已使用等)。
/// 支付/退款主流程据此保持中间态(PROCESSING / PROGRESS), 交由定时同步纠正,
/// 避免误判 FAIL 导致"资金已动但订单失败"的悬挂。
///
/// 各通道 service 在遇到"结果未知"语义时, 应将原始异常包装为本异常抛出,
/// 主流程通过 `instanceof ChannelResultUnknownException` 判定, 不直接置 FAIL。
///
/// @see PayFailureException
public class ChannelResultUnknownException extends PayFailureException {

    /// 子应用"结果未知"错误码(与 channel-one [cn.daxpay.open.platform.core.exception.ChannelErrorCode#RESULT_UNKNOWN] 跨应用约定一致)
    public static final int RESULT_UNKNOWN_CODE = 10009;

    /// 指定 messageKey
    public ChannelResultUnknownException(String messageKey) {
        super(messageKey);
    }

    /// 指定 messageKey 并保留原始异常堆栈(用于通道 HTTP 异常等场景)
    public ChannelResultUnknownException(String messageKey, Throwable cause) {
        super(messageKey);
        // BizException 构造器未设置 cause, 此处安全调用 initCause 保留堆栈
        if (cause != null) {
            initCause(cause);
        }
    }
}
