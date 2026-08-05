package cn.daxpay.open.payment.trade.transfer.bo;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 转账结果业务对象
///
/// 策略层与编排层之间传递的转账结果（发起/同步共用）。
/// 编排层只消费 status / outTransferNo / relationNo / finishTime / sync*；
/// complete 仅作通道语义标记，编排不依赖。
@Data
@Accessors(chain = true)
public class TransferResultBo {

    /// 转账是否已终态完成(通道语义标记, 编排不依赖)
    private boolean complete;

    /// 转账状态(仅 PROCESSING / SUCCESS; 失败由异常路径处理)
    /// @see PayFundStatusEnum
    private PayFundStatusEnum status;

    /// 通道转账单号(微信 paymentNo / 支付宝 order_id / 抖音 orderId)
    private String outTransferNo;

    /// 实际上送通道的商户转账号(特殊通道变形后回写; 普通通道可空, 结算沿用建单默认值)
    private String relationNo;

    /// 转账完成时间
    private OffsetDateTime finishTime;

    /// 拉起转账确认参数(微信二次确认, 其余通道可空)
    private String transferBody;

    /// 同步是否成功(同步查询用)
    private boolean syncSuccess = true;

    /// 同步错误码
    private String syncErrorCode;

    /// 同步错误信息
    private String syncErrorMsg;
}
