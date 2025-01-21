package org.dromara.daxpay.single.sdk.model.allocation;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.code.AllocationResultEnum;
import org.dromara.daxpay.single.sdk.code.AllocationStatusEnum;

/**
 * 分账结果
 * @author xxm
 * @since 2024/12/18
 */
@Data
@Accessors(chain = true)
public class AllocationModel {

    /** 分账订单号 */
    private String allocNo;

    /** 商户分账订单号 */
    private String bizAllocNo;

    /**
     * 分账状态
     * @see AllocationStatusEnum
     */
    private String status;

    /**
     * 分账结果
     * @see AllocationResultEnum
     */
    private String result;
}
