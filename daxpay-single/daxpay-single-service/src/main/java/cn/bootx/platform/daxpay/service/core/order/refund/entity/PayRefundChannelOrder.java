package cn.bootx.platform.daxpay.service.core.order.refund.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.refund.convert.RefundOrderChannelConvert;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundChannelOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付退款订单关联通道信息
 * @author xxm
 * @since 2024/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付退款通道订单")
@Accessors(chain = true)
@TableName("pay_refund_channel_order")
public class PayRefundChannelOrder extends MpCreateEntity implements EntityBaseFunction<RefundChannelOrderDto> {

    @DbColumn(comment = "关联退款id")
    private Long refundId;

    @DbColumn(comment = "通道支付单id")
    private Long payChannelId;

    @DbColumn(comment = "通道")
    private String channel;

    @DbColumn(comment = "异步支付方式")
    private boolean async;

    @DbColumn(comment = "订单金额")
    private Integer orderAmount;

    @DbColumn(comment = "退款金额")
    private Integer amount;

    @DbColumn(comment = "剩余可退余额")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer refundableAmount;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @DbColumn(comment = "退款状态")
    private String status;

    @DbColumn(comment = "退款完成时间")
    private LocalDateTime refundTime;

    /**
     * 转换
     */
    @Override
    public RefundChannelOrderDto toDto() {
        return RefundOrderChannelConvert.CONVERT.convert(this);
    }
}
