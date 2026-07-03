package cn.daxpay.open.payment.core.trade.bo;

import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 退款结果业务对象
///
/// 策略层与服务层之间传递的退款结果
@Data
@Accessors(chain = true)
public class RefundResultBo {

    /// 退款是否已终态完成(true 表示通道同步返回成功, 无需再查询)
    private boolean complete;

    /// 退款状态
    /// @see RefundOrderStatusEnum
    private RefundOrderStatusEnum status;

    /// 通道退款交易号(网关返回的退款流水号)
    private String outRefundNo;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 退款金额(最小货币单位, 分)
    private Long refundAmount;

    /// 买家用户标识
    private String buyerId;

    /// 同步是否成功(退款同步查询用)
    private boolean syncSuccess = true;

    /// 同步错误码
    private String syncErrorCode;

    /// 同步错误信息
    private String syncErrorMsg;
}
