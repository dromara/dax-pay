package org.dromara.daxpay.payment.common.context;

import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/// # 回调信息上下文
///
@Data
@Accessors(chain = true)
public class CallbackInfo {

    /// 回调数据内容
    private Map<String, ?> callbackData = new HashMap<>();

    /// 交易号
    private String tradeNo;

    /// 通道交易号
    private String outTradeNo;

    /// 交易状态
    /// @see PayStatusEnum 支付状态
    /// @see RefundStatusEnum 退款状态
    /// @see TransferStatusEnum 转账状态
    private String tradeStatus;

    /// 交易错误信息
    private String tradeErrorMsg;

    /// 完成时间(支付/退款)
    private LocalDateTime finishTime;

    /// 回调信息错误信息
    private String callbackErrorMsg;

    /// 回调处理状态
    /// @see CallbackStatusEnum
    private CallbackStatusEnum callbackStatus = CallbackStatusEnum.SUCCESS;
}

