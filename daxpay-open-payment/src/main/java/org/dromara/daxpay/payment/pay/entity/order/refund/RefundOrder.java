package org.dromara.daxpay.payment.pay.entity.order.refund;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.pay.convert.order.refund.RefundOrderConvert;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PaymentVendorEnum;
import org.dromara.daxpay.payment.pay.enums.RefundStatusEnum;
import org.dromara.daxpay.payment.pay.enums.SettleStatusEnum;
import org.dromara.daxpay.payment.pay.result.order.refund.RefundOrderVo;
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

    /** 退款标题 */
    private String title;

    /** 退款号 */
    private String refundNo;

    /**
     * 特殊通道关联订单号
     * 部分通道对订单号有要求, 需要使用指定前缀且有长度限制, 无法使用系统订单关联, 使用这个进行关联
     */
    private String relationOrderNo;

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

    /**
     * 结算状态, 为空不进行结算
     * @see SettleStatusEnum
     */
    private String settleStatus;

    /**
     * 支付厂商 微信/支付宝/银联
     * @see PaymentVendorEnum
     */
    private String paymentVendor;

    /** 错误码 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /** 错误信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /** 异步通知地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String attach;

    /**
     * 附加参数
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
