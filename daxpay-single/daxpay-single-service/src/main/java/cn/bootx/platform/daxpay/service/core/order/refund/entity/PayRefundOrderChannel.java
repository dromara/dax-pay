package cn.bootx.platform.daxpay.service.core.order.refund.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付退款订单关联通道信息
 * @author xxm
 * @since 2024/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付退款通道订单")
@Accessors(chain = true)
@TableName("pay_refund_order_channel")
public class PayRefundOrderChannel extends MpBaseEntity {

    @DbColumn(comment = "关联退款id")
    private Long refundId;

    @DbColumn(comment = "通道")
    private String channel;

    /** 关联支付网关退款请求号 */
    @DbColumn(comment = "异步方式关联退款请求号(部分退款情况)")
    private String refundRequestNo;

    @DbColumn(comment = "异步支付方式")
    private boolean async;

    @DbColumn(comment = "退款金额")
    private Integer amount;

}
