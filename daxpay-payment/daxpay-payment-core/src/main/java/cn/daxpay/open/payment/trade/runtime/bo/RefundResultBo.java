package cn.daxpay.open.payment.trade.runtime.bo;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 退款结果业务对象
///
/// 策略层与服务层之间传递的退款结果。
/// 结算层只消费 status / outRefundNo / relationOrderNo / finishTime / sync*；
/// complete 仅作通道语义标记，编排不依赖。
@Data
@Accessors(chain = true)
public class RefundResultBo {

    /// 退款是否已终态完成(通道语义标记，编排不依赖)
    private boolean complete;

    /// 退款状态
    /// @see RefundOrderStatusEnum
    private RefundOrderStatusEnum status;

    /// 通道退款交易号(网关返回的退款流水号)
    private String outRefundNo;

    /// 实际上送通道的商户退款关联号(特殊通道变形后回写; 普通通道可空, 结算沿用建单默认值)
    private String relationOrderNo;

    /// 退款完成时间
    private OffsetDateTime finishTime;

    /// 退款金额(通道回传, 编排不依赖)
    private Long refundAmount;

    /// 同步是否成功(退款同步查询用)
    private boolean syncSuccess = true;

    /// 同步错误码
    private String syncErrorCode;

    /// 同步错误信息
    private String syncErrorMsg;
}
