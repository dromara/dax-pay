package cn.daxpay.single.service.core.payment.repair.result;

import cn.daxpay.single.core.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退款修复结果
 * @author xxm
 * @since 2024/1/26
 */
@Data
@Accessors(chain = true)
public class RefundRepairResult {

    /** 修复号 */
    private String repairNo;
    /** 退款修复前状态 */
    private RefundStatusEnum beforeRefundStatus;
    /** 退款修复后状态 */
    private RefundStatusEnum afterRefundStatus;
    /** 支付修复前状态 */
    private PayOrderRefundStatusEnum beforePayStatus;
    /** 支付修复后状态 */
    private PayOrderRefundStatusEnum afterPayStatus;
}
