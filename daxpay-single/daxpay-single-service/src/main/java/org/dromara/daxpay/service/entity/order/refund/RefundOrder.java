package org.dromara.daxpay.service.entity.order.refund;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.order.refund.RefundOrderConvert;
import org.dromara.daxpay.service.result.order.refund.RefundOrderVo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款订单
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_refund_order")
public class RefundOrder extends MchAppBaseEntity implements ToResult<RefundOrderVo> {

    /** 支付订单ID */
    private Long orderId;

    /** 支付订单号 */
    private String orderNo;

    /** 商户支付订单号 */
    private String bizOrderNo;

    /** 通道支付订单号 */
    private String outOrderNo;

    /** 支付标题 */
    private String title;

    /** 退款号 */
    private String refundNo;

    /** 商户退款号 */
    private String bizRefundNo;

    /** 通道退款交易号 */
    private String outRefundNo;

    /**
     * 退款通道
     * @see ChannelEnum
     */
    private String channel;

    /** 订单金额(元) */
    private BigDecimal orderAmount;

    /** 退款金额(元) */
    private BigDecimal amount;

    /** 退款原因 */
    private String reason;

    /** 退款完成时间 */
    private LocalDateTime finishTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /** 异步通知地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String attach;

    /**
     * 附加参数 以最后一次为准
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String extraParam;

    /** 请求时间 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime reqTime;

    /** 终端ip */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String clientIp;

    /**
     * 转换
     */
    @Override
    public RefundOrderVo toResult() {
        return RefundOrderConvert.CONVERT.toVo(this);
    }
}
