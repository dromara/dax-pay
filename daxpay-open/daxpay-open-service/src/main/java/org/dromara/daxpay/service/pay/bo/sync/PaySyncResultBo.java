package org.dromara.daxpay.service.pay.bo.sync;

import org.dromara.daxpay.core.enums.PayStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author xxm
 * @since 2024/7/25
 */
@Data
@Accessors(chain = true)
public class PaySyncResultBo {


    /** 同步是否成功 */
    private boolean syncSuccess = true;

    /**
     * 支付网关订单状态
     */
    private PayStatusEnum payStatus;

    /** 支付通道对应系统的交易号, 用与和本地记录关联起来 */
    private String outOrderNo;

    /** 交易金额 */
    private BigDecimal amount;

    /** 实收金额 */
    private BigDecimal realAmount;

    /** 支付完成时间 */
    private LocalDateTime finishTime;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncData;

    /** 错误提示码 */
    private String syncErrorCode;

    /** 错误提示 */
    private String syncErrorMsg;

    /** 付款用户ID */
    private String buyerId;

    /** 用户标识 */
    private String userId;

    /**
     * 支付产品
     * 三方通道所使用的支付产品或类型
     */
    private String tradeProduct;

    /**
     * 交易方式
     */
    private String tradeWay;

    /**
     * 银行卡类型
     * 借记卡/贷记卡
     */
    private String bankType;

    /**
     * 透传账号
     * 三方通道使用微信/支付宝/银联支付时产生的订单号
     */
    private String transOrderNo;

    /** 参加活动类型 */
    private String promotionType;
}
