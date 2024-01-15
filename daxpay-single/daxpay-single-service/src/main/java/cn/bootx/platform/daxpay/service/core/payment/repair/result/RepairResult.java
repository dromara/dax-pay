package cn.bootx.platform.daxpay.service.core.payment.repair.result;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 修复结果
 * @author xxm
 * @since 2024/1/4
 */
@Data
@Accessors(chain = true)
public class RepairResult {
    /** 修复记录ID */
    private Long id;
    /** 修复前状态 */
    private PayStatusEnum beforeStatus;
    /** 修复后状态 */
    private PayStatusEnum repairStatus;
}
