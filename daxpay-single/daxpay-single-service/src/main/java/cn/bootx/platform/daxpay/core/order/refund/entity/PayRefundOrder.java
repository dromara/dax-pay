package cn.bootx.platform.daxpay.core.order.refund.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.JacksonRawTypeHandler;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.core.order.refund.convert.RefundConvert;
import cn.bootx.platform.daxpay.dto.order.refund.PayRefundOrderDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款记录
 *
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_refund_order", autoResultMap = true)
public class PayRefundOrder extends MpBaseEntity implements EntityBaseFunction<PayRefundOrderDto> {

    /** 支付单号 */
    private Long paymentId;

    /** 关联的业务号 */
    private String businessNo;

    /** 异步方式关联退款请求号(部分退款情况) */
    private String refundRequestNo;

    /** 标题 */
    private String title;

    /** 退款金额 */
    private Integer amount;

    /** 剩余可退 */
    private Integer refundableBalance;

    /** 退款终端ip */
    private String clientIp;

    /** 退款时间 */
    private LocalDateTime refundTime;

    /**
     * 退款信息列表
     */
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    private List<OrderRefundableInfo> refundableInfo;

    /**
     * 退款状态
     * @see PayRefundStatusEnum
     */
    private String status;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    @Override
    public PayRefundOrderDto toDto() {
        return RefundConvert.CONVERT.convert(this);
    }

}
