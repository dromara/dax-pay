package cn.daxpay.multi.sdk.result.allocation;

import cn.daxpay.multi.sdk.code.AllocOrderStatusEnum;
import lombok.Data;

/**
 * 分账结果
 * @author xxm
 * @since 2024/4/18
 */
@Data
public class AllocResult{

    /** 分账订单号 */
    private String allocNo;

    /** 分账订单号 */
    private String bizAllocNo;

    /**
     * 分账状态
     * @see AllocOrderStatusEnum
     */
    private String status;
}
