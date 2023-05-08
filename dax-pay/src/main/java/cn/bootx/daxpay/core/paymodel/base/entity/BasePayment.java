package cn.bootx.daxpay.core.paymodel.base.entity;

import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.daxpay.code.pay.PayStatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 基础支付记录类
 *
 * @author xxm
 * @date 2021/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BasePayment extends MpBaseEntity {

    /** 交易记录ID */
    private Long paymentId;

    /** 用户ID */
    private Long userId;

    /** 交易金额 */
    private BigDecimal amount;

    /** 可退款金额 */
    private BigDecimal refundableBalance;

    /** 关联的业务id */
    private String businessId;

    /**
     * 支付状态
     * @see PayStatusCode
     */
    private Integer payStatus;

    /** 支付时间 */
    private LocalDateTime payTime;

}
