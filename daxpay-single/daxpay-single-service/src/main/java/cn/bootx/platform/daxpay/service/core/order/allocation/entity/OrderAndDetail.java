package cn.bootx.platform.daxpay.service.core.order.allocation.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分账订单和明细
 * @author xxm
 * @since 2024/4/7
 */
@Data
@Accessors(chain = true)
public class OrderAndDetail {

    /** 分账订单 */
    private AllocationOrder order;
    /** 分账订单明细 */
    private List<AllocationOrderDetail> details;
}
