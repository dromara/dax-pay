package org.dromara.daxpay.service.pay.bo.trade;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付结果业务类
 * @author xxm
 * @since 2024/7/23
 */
@Data
@Accessors(chain = true)
public class PayResultBo {

    /**
     * 第三方支付网关生成的订单号, 用与将记录关联起来
     * 1. 如付款码支付直接成功时会出现
     * 2. 部分通道创建订单是会直接返回
     */
    private String outOrderNo;

    /** 是否支付完成 */
    private boolean complete;

    /** 实收金额 */
    private BigDecimal realAmount;

    /** 完成时间 */
    private LocalDateTime finishTime;

    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;

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
