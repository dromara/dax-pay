package cn.bootx.platform.daxpay.core.order.pay.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付订单通道信息
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_order_channel")
public class PayOrderChannel extends MpBaseEntity {

    @DbColumn(comment = "支付id")
    private Long paymentId;

    @DbColumn(comment = "通道")
    private String channel;

    @DbColumn(comment = "金额")
    private Integer amount;

}
