package cn.daxpay.open.payment.trade.runtime.bo;

import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/// # 回调数据(函数传参,非线程上下文)
///
/// 通道回调解析后的结果数据,由回调入口创建并显式传递给回调处理服务。
/// 不挂 ThreadLocal:它是"流程数据流"而非"线程身份",避免线程池复用时的脏数据残留。
@Data
@Accessors(chain = true)
public class CallbackData {

    /// 回调数据内容
    private Map<String, ?> callbackData = new HashMap<>();

    /// 交易号
    private String tradeNo;

    /// 通道交易号
    private String outTradeNo;

    /// 交易状态
    /// @see PayStatusEnum 支付状态
    private String tradeStatus;

    /// 交易错误信息
    private String tradeErrorMsg;

    /// 完成时间(UTC)
    private OffsetDateTime finishTime;

    /// 回调信息错误信息
    private String callbackErrorMsg;

    /// 回调处理状态
    /// @see CallbackStatusEnum
    private CallbackStatusEnum callbackStatus = CallbackStatusEnum.SUCCESS;
}
