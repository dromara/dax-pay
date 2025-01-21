package org.dromara.daxpay.single.sdk.model.allocation;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.code.AllocationResultEnum;
import org.dromara.daxpay.single.sdk.code.AllocationStatusEnum;

/**
 * 分账同步结果返回类
 * @author xxm
 * @since 2024/5/20
 */
@Data
@Accessors(chain = true)
public class AllocSyncModel {

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
