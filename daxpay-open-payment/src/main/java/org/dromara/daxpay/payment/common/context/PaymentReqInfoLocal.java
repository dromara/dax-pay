package org.dromara.daxpay.payment.common.context;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import org.dromara.daxpay.payment.merchant.enums.MchAppStatusEnum;
import org.dromara.daxpay.payment.merchant.enums.MerchantStatusEnum;

import java.math.BigDecimal;

/**
 * 调用请求和配置及相关信息
 * @author xxm
 * @since 2024/6/27
 */
@Data
@Accessors(chain = true)
public class PaymentReqInfoLocal {

    /* 公共配置 */
    /** 支付网关地址 */
    private String gatewayServiceUrl;

    /** 网关H5端地址 */
    private String gatewayH5Url;

    /* 商户相关信息 */
    /** 服务商号 */
    private String isvNo;

    /**
     * 服务商状态
     * @see IsvStatusEnum
     */
    private String isvStatus;


    /* 商户信息 */
    /** 商户号 */
    private String mchNo;

    /**
     * 商户状态
     * @see MerchantStatusEnum
     */
    private String mchStatus;

    /** 商户公钥 */
    private String publicKey;

    /* 商户应用信息 */
    /** 应用号 */
    private String appId;

    /**
     * 应用状态
     * @see MchAppStatusEnum
     */
    private String appStatus;

    /** 单笔支付限额 */
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    private Integer orderTimeout;

    /** 是否验证请求时间是否超时 */
    private boolean reqTimeout;

    /**
     * 请求有效时长(秒)
     * 如果传输的请求时间与当前服务时间差值超过配置的时长, 将会请求失败
     */
    private Integer reqTimeoutSecond;

}
