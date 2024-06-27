package cn.daxpay.single.sdk.model.allocation;

import cn.daxpay.single.sdk.code.AllocOrderStatusEnum;
import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分账结果
 * @author xxm
 * @since 2024/4/18
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AllocationModel extends DaxPayResponseModel {

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
