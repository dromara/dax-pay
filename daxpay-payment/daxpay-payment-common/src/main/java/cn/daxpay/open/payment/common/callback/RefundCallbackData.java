package cn.daxpay.open.payment.common.callback;

import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/// # 退款回调数据(函数传参,非线程上下文)
///
/// 通道退款回调解析后的结果数据,由退款回调入口创建并显式传递给 [RefundCallbackService]。
/// 与 [CallbackData] 对称:支付回调用 tradeNo 反查支付单,退款回调用 refundNo 反查退款单。
/// 不挂 ThreadLocal,避免线程池复用时的脏数据残留。
@Data
@Accessors(chain = true)
public class RefundCallbackData {

    /// 回调数据内容(原始解析结果,供日志/溯源)
    private Map<String, ?> callbackData = new HashMap<>();

    /// 平台退款号(对应通道 out_request_no,反查退款单的主键)
    private String refundNo;

    /// 通道退款流水号(三方通道返回的退款流水号)
    private String outRefundNo;

    /// 退款交易状态(抽象态, success 表示退款成功)
    /// @see CallbackStatusEnum
    private String tradeStatus;

    /// 退款交易错误信息
    private String tradeErrorMsg;

    /// 退款完成时间(UTC)
    private OffsetDateTime finishTime;

    /// 回调信息错误信息
    private String callbackErrorMsg;

    /// 回调处理状态
    /// @see CallbackStatusEnum
    private CallbackStatusEnum callbackStatus = CallbackStatusEnum.SUCCESS;
}
