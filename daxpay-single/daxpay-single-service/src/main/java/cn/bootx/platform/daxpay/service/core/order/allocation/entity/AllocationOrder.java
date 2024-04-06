package cn.bootx.platform.daxpay.service.core.order.allocation.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账订单
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocationOrder extends MpBaseEntity {

    /**
     * 支付订单ID
     */
    @DbColumn(comment = "支付订单ID")
    private Long paymentId;

    /**
     * 网关订单号
     */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;


    /**
     * 总分账金额
     */
    @DbColumn(comment = "总分账金额")
    private Integer amount;

}
