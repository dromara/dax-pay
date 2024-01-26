package cn.bootx.platform.daxpay.service.core.payment.repair.result;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付修复结果
 * @author xxm
 * @since 2024/1/4
 */
@Data
@Accessors(chain = true)
public class PayRepairResult {
    /** 修复记录ID */
    private Long repairId;
    /** 修复前状态 */
    private PayStatusEnum beforeStatus;
    /** 修复后状态 */
    private PayStatusEnum afterPayStatus;
}
