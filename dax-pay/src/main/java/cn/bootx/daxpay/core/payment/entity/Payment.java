package cn.bootx.daxpay.core.payment.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.JacksonRawTypeHandler;
import cn.bootx.daxpay.code.pay.PayStatusCode;
import cn.bootx.daxpay.core.payment.convert.PaymentConvert;
import cn.bootx.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.daxpay.dto.payment.PaymentDto;
import cn.bootx.daxpay.dto.payment.RefundableInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付记录
 *
 * @author xxm
 * @date 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName(value = "pay_payment", autoResultMap = true)
public class Payment extends MpBaseEntity implements EntityBaseFunction<PaymentDto> {

    /** 关联的业务id */
    private String businessId;

    /** 用户ID */
    private Long userId;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 是否是异步支付 */
    private boolean asyncPayMode;

    /** 异步支付通道 */
    private Integer asyncPayChannel;

    /** 金额 */
    private BigDecimal amount;

    /** 可退款余额 */
    private BigDecimal refundableBalance;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    /**
     * 支付通道信息列表
     * @see PayChannelInfo
     */
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    @BigField
    private List<PayChannelInfo> payChannelInfo;

    /**
     * 退款信息列表
     * @see RefundableInfo
     */
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    @BigField
    private List<RefundableInfo> refundableInfo;

    /**
     * 支付状态
     * @see PayStatusCode#TRADE_PROGRESS
     */
    private Integer payStatus;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 支付终端ip */
    private String clientIp;

    /** 过期时间 */
    private LocalDateTime expiredTime;

    @Override
    public PaymentDto toDto() {
        return PaymentConvert.CONVERT.convert(this);
    }

}
