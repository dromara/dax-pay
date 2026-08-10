package cn.daxpay.open.payment.trade.alloc.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

/// # 分账结果业务对象
///
/// 策略层与编排层之间传递的分账结果(发起/同步共用)。
/// 分账是"多接收方"操作, 通道返回的是逐明细结果, 编排层据此聚合订单状态。
@Data
@Accessors(chain = true)
public class AllocResultBo {

    /// 通道分账单号(支付宝 settle_no / 微信 transaction_id / 抖音 orderId)
    private String outAllocNo;

    /// 逐明细结果(发起时各接收方的处理状态, 同步时从通道查询回填)
    private List<DetailResult> details;

    /// 同步是否成功(同步查询用, 发起时默认 true)
    private boolean syncSuccess = true;

    /// 同步错误码
    private String syncErrorCode;

    /// 同步错误信息
    private String syncErrorMsg;

    /// 单个明细的通道返回结果
    @Data
    @Accessors(chain = true)
    public static class DetailResult {

        /// 匹配明细用的接收方账号(加密前的原始值, 用于回查明细)
        private String receiverAccount;

        /// 通道侧明细ID
        private String outDetailId;

        /// 明细结果(pending/success/fail)
        /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum
        private String result;

        /// 错误码
        private String errorCode;

        /// 错误信息
        private String errorMsg;

        /// 完成时间
        private OffsetDateTime finishTime;
    }
}
