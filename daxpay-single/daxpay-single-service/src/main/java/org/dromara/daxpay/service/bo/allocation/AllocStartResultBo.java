package org.dromara.daxpay.service.bo.allocation;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;

/**
 * 分账操作结果
 * @author xxm
 * @since 2024/11/15
 */
@Data
@Accessors(chain = true)
public class AllocStartResultBo {

    /** 通道分账号 */
    private String outAllocNo;
    /**
     * 分账状态
     * @see AllocationStatusEnum
     */
    private String status;
}
