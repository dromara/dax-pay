package org.dromara.daxpay.core.result.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;

/**
 * 分账同步接口返回类
 * @author xxm
 * @since 2024/5/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账同步接口")
public class AllocSyncResult {

    /**
     * 分账状态
     * @see AllocationStatusEnum
     */
    @Schema(description = "分账状态")
    private String status;

    /**
     * 分账结果
     * @see AllocationResultEnum
     */
    @Schema(description = "分账结果")
    private String result;
}
