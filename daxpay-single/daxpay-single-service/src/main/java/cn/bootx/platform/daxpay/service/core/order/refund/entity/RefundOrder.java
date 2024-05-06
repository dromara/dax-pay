package cn.bootx.platform.daxpay.service.core.order.refund.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.refund.convert.RefundOrderConvert;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款订单
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "退款订单")
@TableName(value = "pay_refund_order", autoResultMap = true)
public class RefundOrder extends MpBaseEntity implements EntityBaseFunction<RefundOrderDto> {

    /** 支付订单ID */
    @DbColumn(comment = "支付订单ID")
    private Long orderId;

    /** 支付订单号 */
    @DbColumn(comment = "支付订单号")
    private String orderNo;

    /** 商户支付订单号 */
    @DbColumn(comment = "商户支付订单号")
    private String bizOrderNo;

    /** 通道支付订单号 */
    @DbColumn(comment = "商户支付订单号")
    private String outOrderNo;

    /** 支付标题 */
    @DbColumn(comment = "支付标题")
    private String title;

    /** 退款号 */
    @DbColumn(comment = "退款号")
    private String refundNo;

    @DbColumn(comment = "商户退款号")
    private String bizRefundNo;


    /** 三方支付系统退款交易号 */
    @DbColumn(comment = "三方支付系统退款交易号")
    private String outRefundNo;

    /**
     * 退款通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "支付通道")
    private String channel;

    /** 订单金额 */
    @DbColumn(comment = "订单金额")
    private Integer orderAmount;

    /** 退款金额 */
    @DbColumn(comment = "退款金额")
    private Integer amount;

    /** 退款原因 */
    @DbColumn(comment = "退款原因")
    private String reason;

    /** 退款完成时间 */
    @DbColumn(comment = "退款完成时间")
    private LocalDateTime finishTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @DbColumn(comment = "退款状态")
    private String status;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    private String errorMsg;

    @Override
    public RefundOrderDto toDto() {
        return RefundOrderConvert.CONVERT.convert(this);
    }

}
