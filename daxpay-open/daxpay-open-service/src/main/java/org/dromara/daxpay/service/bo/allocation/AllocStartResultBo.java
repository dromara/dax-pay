package org.dromara.daxpay.service.bo.allocation;

import org.dromara.daxpay.core.enums.AllocationResultEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
     * 状态
     * @see AllocationResultEnum
     */
    private AllocationResultEnum result = AllocationResultEnum.ALL_PENDING;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 是否需要完结
     */
    private boolean needFinish = true;
}
